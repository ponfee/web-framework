package cn.ponfee.web.framework.auth;

import java.util.Date;

import com.google.common.base.Preconditions;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

/**
 * https://github.com/jwtk/jjwt
 * 
 * <pre>
 *   iss -- token的发行者
 *   sub -- 该JWT所面向的用户
 *   aud -- 接收该JWT的一方
 *   exp -- token的失效时间
 *   nbf -- 在此时间段之前,不会被处理
 *   iat -- jwt发布时间
 *   jti -- jwt唯一标识,防止重复使用
 *   rfh -- 刷新时间
 * </pre>
 * 
 * @author Ponfee
 */
public abstract class AbstractJwtManager {

    public static final String CLAIM_RFH = "rfh";
    public static final String RENEW_JWT = "renew-jwt";

    private final SignatureAlgorithm algorithm;
    private final int expireSeconds;
    private final int refreshSeconds;
    private final int expireMillis;
    private final int refreshMillis;

    public AbstractJwtManager() {
        // default expire 2hrs and refresh 1hrs
        this(SignatureAlgorithm.HS256, 7200, 3600);
    }

    public AbstractJwtManager(SignatureAlgorithm algorithm, 
                              int expireSeconds, int refreshSeconds) {
        Preconditions.checkArgument(
            expireSeconds > refreshSeconds, 
            "Expire time must greater than refresh time."
        );
        this.algorithm = algorithm;
        this.expireSeconds = expireSeconds;
        this.refreshSeconds = refreshSeconds;
        this.expireMillis = expireSeconds * 1000;
        this.refreshMillis = refreshSeconds * 1000;
    }

    /**
     * Creates jwt
     * 
     * @param alg           the signature algorithm
     * @param expireMillis  the expire millis
     * @param refreshMillis the refresh millis
     * @param subject       the subject
     * @return a new jwt
     */
    public abstract String create(String subject);

    /**
     * Verifies jwt
     * 
     * @param jwt the jwt
     * @return a Jws object if the jwt verify success
     * @throws InvalidJwtException  if verify fail
     */
    public abstract Jws<Claims> verify(String jwt) throws InvalidJwtException;

    /**
     * Revokes jwt such as logout and so on
     * 
     * @param jwt the jwt
     */
    public abstract void revoke(String jwt);

    protected final JwtBuilder build(String subject, byte[] secret) {
        return Jwts.builder()
                   .setSubject(subject)
                   .setExpiration(new Date(System.currentTimeMillis() + expireMillis))
                   .claim(CLAIM_RFH, System.currentTimeMillis() + refreshMillis)
                   .signWith(algorithm, secret)
                   //.setId(jti);
                   //.compressWith(CompressionCodecs.DEFLATE)
        ;
    }

    protected final Jws<Claims> parse(String jwt, byte[] secret) throws InvalidJwtException {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(jwt);
            // ok, we can trust this jwt
        } catch (SignatureException e) {
            // don't trust the jwt!
            throw new InvalidJwtException(e.getMessage());
        }
    }

    // ------------------------------------------------------------------getter
    public SignatureAlgorithm getAlgorithm() {
        return algorithm;
    }

    public int getExpireSeconds() {
        return expireSeconds;
    }

    public int getRefreshSeconds() {
        return refreshSeconds;
    }
}
