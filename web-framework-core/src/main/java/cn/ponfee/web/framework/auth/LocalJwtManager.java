package cn.ponfee.web.framework.auth;

import org.apache.commons.lang3.tuple.Pair;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Jwt manager for local secret
 * 
 * @author Ponfee
 */
public class LocalJwtManager extends AbstractJwtManager {

    private final byte[] secret;

    public LocalJwtManager(String secret) {
        this.secret = secret.getBytes();
    }

    public LocalJwtManager(String secret, SignatureAlgorithm algorithm, 
                           int expireSeconds, int refreshSeconds) {
        super(algorithm, expireSeconds, refreshSeconds);
        this.secret = secret.getBytes();
    }

    public String create(String subject) {
        return create(subject, secret).compact();
    }

    public Pair<Jws<Claims>, String> verify(String jwt) throws InvalidJwtException {
        return verify(jwt, secret);
    }

    public void revoke(String jwt) {
        // No thing to do
    }

}
