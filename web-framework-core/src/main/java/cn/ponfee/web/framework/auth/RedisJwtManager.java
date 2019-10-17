package cn.ponfee.web.framework.auth;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

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
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Jwt manager for redis cache
 * 
 * @author Ponfee
 */
public class RedisJwtManager extends AbstractJwtManager {

    public static final String CLAIM_JTI = "jti";

    private final byte[] redisKeyPrefix;
    private final JedisClient jedisClient;

    public RedisJwtManager(JedisClient jedisClient) {
        this.jedisClient = jedisClient;
        this.redisKeyPrefix = "jti".getBytes();
    }

    public RedisJwtManager(JedisClient jedisClient, String redisKeyPrefix,
                           SignatureAlgorithm algorithm, int expireSeconds, int refreshSeconds) {
        super(algorithm, expireSeconds, refreshSeconds);
        this.jedisClient = jedisClient;
        this.redisKeyPrefix = redisKeyPrefix.getBytes();
    }

    public String create(String subject) {
        byte[] jti    = ObjectUtils.uuid(), 
               secret = SecureRandoms.nextBytes(16);

        jedisClient.valueOps().set(withPrefix(jti), secret, getExpireSeconds());

        return build(subject, secret)
                .setHeaderParam(CLAIM_JTI, Base64UrlSafe.encode(jti))
                .compact();
    }

    public Jws<Claims> verify(String jwt) throws InvalidJwtException {
        byte[] jti    = extractJti(jwt),
               secret = jedisClient.valueOps().get(withPrefix(jti));

        if (secret == null || secret.length == 0) {
            throw new InvalidJwtException("Jti not found.");
        }

        Jws<Claims> jws = parse(jwt, secret);

        long refresh = (long) jws.getBody().get(CLAIM_RFH);
        if (refresh < System.currentTimeMillis()) {
            // need refresh the jwt
            jws.getBody().put(RENEW_JWT, create(jws.getBody().getSubject()));

            // revoke the oldness jwt
            jedisClient.keysOps().del(withPrefix(jti));
        }
        return jws;
    }

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

    // ------------------------------------------------------------------------private methods
    private byte[] extractJti(String jwt) throws InvalidJwtException {
        int index;
        if (jwt == null || (index = jwt.indexOf(JwtParser.SEPARATOR_CHAR)) == -1) {
            throw new InvalidJwtException("Invalid jwt.");
        }
        byte[] headerBytes = Base64UrlSafe.decode(jwt.substring(0, index));
        Map<String, Object> header = Jsons.fromJson(headerBytes, TypeReferences.MAP_NORMAL);
        byte[] jti = Base64UrlSafe.decode((String) header.get(CLAIM_JTI));
        if (jti == null || jti.length == 0) {
            throw new InvalidJwtException("Invalid Jti.");
        }
        return jti;
    }

    private byte[] withPrefix(byte[] jedisKey) {
        return Bytes.concat(redisKeyPrefix, jedisKey);
    }

}
