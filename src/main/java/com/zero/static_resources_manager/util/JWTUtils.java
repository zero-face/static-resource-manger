package com.zero.static_resources_manager.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Zero
 * @Date 2021/4/20 23:28
 * @Since 1.8
 **/
public class JWTUtils {
    private static final String SIGN = "@FJ@#DDJFA1233451_fsggHSDGR!@$GFD%^YNH&^J&M";
    //生成token header payload sign
    public static String getToken(Map<String,String> map) {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 7);
        JWTCreator.Builder builder = JWT.create();
        map.forEach((k,v) -> {
            builder.withClaim(k,v);
        });
        final String token = builder.withExpiresAt(calendar.getTime())
                .sign(Algorithm.HMAC256(SIGN));
        return token;
    }
    //验证token
    public static DecodedJWT verifyToken(String token) {
        return JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);
    }

}
