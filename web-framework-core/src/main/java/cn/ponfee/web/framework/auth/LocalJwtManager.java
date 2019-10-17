package cn.ponfee.web.framework.auth;

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
        return build(subject, secret).compact();
    }

    public Jws<Claims> verify(String jwt) throws InvalidJwtException {
        Jws<Claims> jws = parse(jwt, secret);

        long refresh = (long) jws.getBody().get(CLAIM_RFH);
        if (refresh < System.currentTimeMillis()) {
            // need refresh the jwt
            jws.getBody().put(RENEW_JWT, create(jws.getBody().getSubject()));
        }
        return jws;
    }

    public void revoke(String jwt) {
        // No thing to do
    }

}
