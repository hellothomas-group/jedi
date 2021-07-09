package xyz.hellothomas.jedi.admin.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import xyz.hellothomas.jedi.admin.domain.User;
import xyz.hellothomas.jedi.core.exception.BusinessException;

import java.util.Calendar;
import java.util.Date;

import static xyz.hellothomas.jedi.core.enums.ErrorCodeEnum.TOKEN_UNAVAILABLE;

/**
 * @author 80234613 唐圆
 * @date 2021/7/8 9:48
 * @descripton
 * @version 1.0
 */
public class JwtUtil {
    public static final String CLAIM_USER_NAME = "userName";

    /**
     签发对象：这个用户的id
     签发时间：现在
     有效时间：30分钟
     载荷内容：暂时设计为：这个人的名字
     加密密钥：这个人的id加上一串字符串
     */
    public static String createToken(User user) {

        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.MINUTE, 30);
        Date expiresDate = nowTime.getTime();

        return JWT.create()
                //签发对象
                .withAudience(String.valueOf(user.getId()))
                //发行时间
                .withIssuedAt(new Date())
                //有效时间
                .withExpiresAt(expiresDate)
                //载荷，随便写几个都可以
                .withClaim(CLAIM_USER_NAME, user.getUserName())
                //加密
                .sign(Algorithm.HMAC256(user.getPassword()));
    }

    /**
     * 检验合法性，其中secret参数就应该传入的是用户的id
     * @param token
     * @throws
     */
    public static void verifyToken(String token, String secret) {
        DecodedJWT jwt = null;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
            jwt = verifier.verify(token);
        } catch (Exception e) {
            //效验失败
            //这里抛出的异常是我自定义的一个异常，你也可以写成别的
            throw new BusinessException(TOKEN_UNAVAILABLE);
        }
    }

    /**
     * 获取签发对象
     */
    public static String getAudience(String token) {
        String audience;
        try {
            audience = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            //这里是token解析失败
            throw new BusinessException(TOKEN_UNAVAILABLE);
        }
        return audience;
    }

    /**
     * 通过载荷名字获取载荷的值
     */
    public static Claim getClaimByName(String token, String name) {
        return JWT.decode(token).getClaim(name);
    }
}
