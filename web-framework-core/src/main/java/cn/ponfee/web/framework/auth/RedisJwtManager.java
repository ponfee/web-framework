package cn.ponfee.web.framework.auth;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import code.ponfee.commons.jedis.JedisClient;
import code.ponfee.commons.json.Jsons;
import code.ponfee.commons.json.JacksonTypeReferences;
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
        super();
        this.jedisClient = jedisClient;
        this.redisKeyPrefix = "jti:".getBytes();
    }

    public RedisJwtManager(JedisClient jedisClient, String redisKeyPrefix,
                           SignatureAlgorithm algorithm, int expireSeconds, int refreshSeconds) {
        super(algorithm, expireSeconds, refreshSeconds);
        this.jedisClient = jedisClient;
        this.redisKeyPrefix = redisKeyPrefix.getBytes();
    }

    public String create(String subject) {
        byte[] jti    = ObjectUtils.uuid(), 
               secret = SecureRandoms.nextBytes(8);

        jedisClient.valueOps().set(withPrefix(jti), secret, getExpireSeconds());

        return create(subject, secret)
                .setHeaderParam(CLAIM_JTI, Base64UrlSafe.encode(jti))
                .compact();
    }

    public Pair<Jws<Claims>, String> verify(String jwt) throws InvalidJwtException {
        byte[] jti    = withPrefix(extractJti(jwt)),
               secret = jedisClient.valueOps().get(jti);

        if (secret == null || secret.length == 0) {
            throw new InvalidJwtException("Jti not found.");
        }

        Pair<Jws<Claims>, String> result = verify(jwt, secret);
        if (result.getRight() != null) {
            // revoke the oldness jwt
            jedisClient.keysOps().del(jti);
        }
        return result;
    }

    public void revoke(String jwt) {
        if (StringUtils.isBlank(jwt)) {
            return;
        }

        try {
            jedisClient.keysOps().del(withPrefix(extractJti(jwt)));
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
        Map<String, Object> header = Jsons.fromJson(headerBytes, JacksonTypeReferences.MAP_NORMAL);
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
