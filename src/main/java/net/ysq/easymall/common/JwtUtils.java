package net.ysq.easymall.common;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;

/**
 * JWT的工具类，包括签发、验证、获取信息
 *
 * @author passerbyYSQ
 * @create 2020-08-22 11:13
 */
public class JwtUtils {
	
    // 有效时间：7天
//    public static final long EFFECTIVE_DURATION = 1000 * 60 * 60 * 24 * 7;
    // 发行者
    private static final String ISSUER = "net.ysq";
    // 默认密钥
    private static final String DEFAULT_SECRET = "ysqJYKL2010!";

    /**
     * 生成Jwt字符串
     *
     * @param claims    由于类库只支持基本类型的包装类、String、Date，我们最好使用String
     * @param secret    加密的密钥
     * @return
     */
    public static String generateJwt(Map<String, String> claims, String secret, long duration) {
        // 发行时间
        Date issueAt = new Date();
        // 过期时间
        Date expireAt = new Date(issueAt.getTime() + duration);
        // 加密算法
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes(StandardCharsets.UTF_8));

        JWTCreator.Builder builder = JWT.create()
                .withIssuer(ISSUER)
                .withIssuedAt(issueAt)
                .withExpiresAt(expireAt);

        // 设置Payload信息
        Set<String> keySet = claims.keySet();
        for (String key : keySet) {
            builder.withClaim(key, claims.get(key));
        }

        return builder.sign(algorithm);
    }

    public static String generateJwt(Map<String, String> claims, long duration) {
        return generateJwt(claims, DEFAULT_SECRET, duration);
    }

    public static String generateJwt(String username, String secret, long duration) {
        Map<String, String> claims = new HashMap<>();
        claims.put("username", username);
        return generateJwt(claims, secret, duration);
    }
    
    public static String generateJwt(String username, long duration) {
        return generateJwt(username, DEFAULT_SECRET, duration);
    }

    /**
     * 校验jwt是否合法
     * 对异常进行全局的统一捕获
     * 父类异常：JWTVerificationException
     *
     * 子类异常
     * 过期：TokenExpiredException
     * ...
     *
     * @param jwt
     * @param claims
     * @return
     */
    public static DecodedJWT verifyJwt(String jwt, String secret) {
        // 解密算法
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes(StandardCharsets.UTF_8));
        Verification verification = JWT.require(algorithm).withIssuer(ISSUER);
        
        // 验证不需要传入claims
//        Set<String> keySet = claims.keySet();
//        for (String key : keySet) {
//            verification.withClaim(key, claims.get(key));
//        }

        JWTVerifier verifier = verification.build();
        // 验证不通过会抛出异常
        // 如果验证通过，可以通过返回值DecodedJWT来获取claims
        return verifier.verify(jwt);
    }

    public static DecodedJWT verifyJwt(String jwt) {
        return verifyJwt(jwt, DEFAULT_SECRET);
    }
}
