package ski.mashiro.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.Map;

/**
 * @author MashiroT
 * @since 2023-07-27
 */
public class JwtUtils {

    private static final String SALT = "ShiinaMashiro";
    private static final String ISSUER = "mashirot";
    public static final Long EXPIRE_TIME = 30 * 60 * 1000L;
    private static final Algorithm ALGORITHM = Algorithm.HMAC512(SALT);

    public static String createJwt(Map<String, Object> claims) {
        return JWT
                .create()
                .withPayload(claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .withIssuer(ISSUER)
                .withIssuedAt(new Date())
                .sign(ALGORITHM);
    }

    public static DecodedJWT parseJwt(String jwt) throws JWTVerificationException {
        return JWT
                .require(ALGORITHM)
                .withIssuer(ISSUER)
                .build()
                .verify(jwt);
    }

}
