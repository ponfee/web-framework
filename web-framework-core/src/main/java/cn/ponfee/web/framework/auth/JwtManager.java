package cn.ponfee.web.framework.auth;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;

import java.util.Base64;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;

import code.ponfee.commons.jedis.JedisClient;
import code.ponfee.commons.json.Jsons;
import code.ponfee.commons.json.TypeReferences;
import code.ponfee.commons.util.Base64UrlSafe;
import code.ponfee.commons.util.Bytes;
import code.ponfee.commons.util.ObjectUtils;
import code.ponfee.commons.util.SecureRandoms;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

/**
 * https://github.com/jwtk/jjwt
 *
 * @author Ponfee
 */
@Component("jwtManager")
public class JwtManager {

    public static final String CLAIM_RFH = "rfh";
    public static final String CLAIM_JTI = "jti";
    public static final String RENEW_JWT = "renew-jwt";
    private static final byte[] JTI_KEY_PREFIX = "jti:".getBytes();

    private @Value("${jwt.exp.seconds:7200}") int jwtExpSeconds; // default expire 2 hours
    private @Value("${jwt.rfh.seconds:3600}") int jwtRfhSeconds; // default refresh 1 hours

    private @Resource JedisClient jedisClient;

    /**
     * Creates the jwt with subject
     * 
     * @param subject
     * @return
     */
    public String create(String subject) {
        return create(HS256, jwtExpSeconds * 1000, 
                      jwtRfhSeconds * 1000, subject);
    }

    /**
     * Creates a jwt
     * 
     * @param alg
     * @param expireMillis
     * @param refreshMillis
     * @param subject
     * @return
     */
    public String create(SignatureAlgorithm alg, int expireMillis, 
                         int refreshMillis, String subject) {
        Preconditions.checkArgument(expireMillis > refreshMillis, "expire must than refresh.");

        byte[] jti    = ObjectUtils.uuid(), 
               secret = SecureRandoms.nextBytes(16);

        String jwt =  Jwts.builder()
                          .setHeaderParam(CLAIM_JTI, Base64UrlSafe.encode(jti))
                          .setSubject(subject)
                          .setExpiration(new Date(System.currentTimeMillis() + expireMillis))
                          .claim(CLAIM_RFH, System.currentTimeMillis() + refreshMillis)
                          .signWith(alg, secret)
                          //.setId(jti);
                          //.compressWith(CompressionCodecs.DEFLATE)
                          .compact();
        jedisClient.valueOps().set(withPrefix(jti), secret, expireMillis / 1000);
        return jwt;
    }

    /**
     * Verify the jwt
     * 
     * @param jwt
     * @return the jws of jwt if verify success
     * @throws InvalidJwtException  if verify fail
     */
    public Jws<Claims> verify(String jwt) throws InvalidJwtException {
        byte[] jti = extractJti(jwt);
        byte[] secret = jedisClient.valueOps().get(withPrefix(jti), false);
        if (secret == null || secret.length == 0) {
            throw new InvalidJwtException("Jti not found.");
        }

        try {
            // ok, we can trust this jwt
            Jws<Claims> jws = Jwts.parser()
                                  .setSigningKey(secret)
                                  .parseClaimsJws(jwt);

            long refresh = (long) jws.getBody().get(CLAIM_RFH);
            if (refresh < System.currentTimeMillis()) {
                // need refresh the jwt
                String newlyJwt = create(jws.getBody().getSubject());
                jws.getBody().put(RENEW_JWT, newlyJwt);

                // revoke the oldness jwt
                jedisClient.keysOps().del(withPrefix(jti));
            }
            return jws;
        } catch (SignatureException e) {
            // don't trust the jwt!
            throw new InvalidJwtException(e.getMessage());
        }
    }

    /**
     * Revokes the jwt when logout and so on
     * 
     * @param jwt
     */
    public void revoke(String jwt) {
        if (StringUtils.isBlank(jwt)) {
            return;
        }

        try {
            byte[] jti = withPrefix(extractJti(jwt));
            jedisClient.keysOps().del(jti);
        } catch (InvalidJwtException ignored) {
            ignored.printStackTrace();
        }
    }

    // ------------------------------------------------------------------------getter/setter
    public int getJwtExpSeconds() {
        return jwtExpSeconds;
    }

    public void setJwtExpSeconds(int jwtExpSeconds) {
        this.jwtExpSeconds = jwtExpSeconds;
    }

    public int getJwtRfhSeconds() {
        return jwtRfhSeconds;
    }

    public void setJwtRfhSeconds(int jwtRfhSeconds) {
        this.jwtRfhSeconds = jwtRfhSeconds;
    }

    // ------------------------------------------------------------------------private methods
    private byte[] extractJti(String jwt) throws InvalidJwtException {
        int index;
        if (jwt == null || (index = jwt.indexOf(JwtParser.SEPARATOR_CHAR)) == -1) {
            throw new InvalidJwtException("Invalid jwt.");
        }
        byte[] headerBytes = Base64.getUrlDecoder().decode(jwt.substring(0, index));
        Map<String, Object> header = Jsons.fromJson(headerBytes, TypeReferences.MAP_NORMAL);
        byte[] jti = Base64UrlSafe.decode((String) header.get(CLAIM_JTI));
        if (jti == null || jti.length == 0) {
            throw new InvalidJwtException("Invalid Jti.");
        }
        return jti;
    }

    private static byte[] withPrefix(byte[] jedisKey) {
        return Bytes.concat(JTI_KEY_PREFIX, jedisKey);
    }

}
