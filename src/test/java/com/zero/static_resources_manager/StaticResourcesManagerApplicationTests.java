package com.zero.static_resources_manager;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class StaticResourcesManagerApplicationTests {
@Autowired
private RedisTemplate redisTemplate;
    @Test
    void contextLoads() {
        Map<String,Object> map = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 39);
        String token = JWT.create()
                .withClaim("username", "zero")
                .withClaim("userid", 1)
                .withExpiresAt(calendar.getTime()) //国企时间
                .sign(Algorithm.HMAC256("abcd"));//签名
        System.out.println(token);
    }
    @Test
    void test(){
        //创建验证对象
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("abcd")).build();
        DecodedJWT verify = jwtVerifier.verify("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MjA3MTU4MTAsInVzZXJpZCI6MSwidXNlcm5hbWUiOiJ6ZXJvIn0.-Z2mFSJ70hKLxcDHv5NXQZGeMRsRPc79c68nnWCG-dw");

        System.out.println(verify.getPayload());
        System.out.println(verify.getClaim("username").asString());
        System.out.println(verify.getClaims().get("userid").asInt());
        System.out.println(verify.getExpiresAt());
        System.out.println(verify.getSignature());
    }
    @Test
    public void te() {
        System.out.println(new Date().getTime());
    }
}
