package cn.ponfee.web.framework.util;

import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtTest {

    public static void main(String[] args) throws Exception {
        String jwt =  Jwts.builder()
                          .setSubject("subject")
                          .setExpiration(new Date(System.currentTimeMillis() + 5000))
                          .signWith(SignatureAlgorithm.HS256, "secret")
                          .compact();
        //Thread.sleep(5000);
        Jwts.parser()
            .setSigningKey("secret")
            .parseClaimsJws(jwt);
    }
}
