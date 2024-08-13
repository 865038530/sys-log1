package com.example.wangjian.syslog1.config;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.wangjian.syslog1.entity.CommonId;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component("Token")
@Slf4j
public class TokenConfig {

    /**
     * toekn 密钥
     */
    private static final String JWT_SECRET = "qiesiyv";//自己随便起个字符串
    /**
     * token 过期时间
     */
    private static final int calendarField = Calendar.DATE;
    private static final int calendarInterval = 30;

    private static final Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);

    // header Map
    private static final Map<String, Object> map = new HashMap<>();

    @Autowired
    RestTemplate restTemplate;

    private static JWTCreator.Builder createTokenBuilder() {
        /** 加密方式 **/
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        // 签发时间
        Date iaDate = new Date();
        // 设置过期时间
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(calendarField, calendarInterval);//前者为时间单位，后者时间数量
        Date expiresDate = nowTime.getTime();
        JWTCreator.Builder builder = JWT.create().withHeader(map)
                .withIssuedAt(iaDate)
                .withExpiresAt(expiresDate)
                .withClaim("iss", "qiesiyv")  //payload iss jwt的签发者 看着起名
                .withClaim("aud", "app");   //aud  接收的乙方	 看着起名
        return builder;
    }

    /**
     * 生成token
     *
     * @param
     * @return
     */
    public static String createToken(String uid,String userimg,String username,String userbimg) {//token保存的数据
        JWTCreator.Builder builder = createTokenBuilder();
        String token = builder.withClaim("uid", uid)
                .withClaim("userimg", userimg)
                .withClaim("username", username)
                .withClaim("userbimg", userbimg)
                .sign(algorithm);
        return token;
    }


    // 解析
    static DecodedJWT verifyToken(String token) {
        DecodedJWT jwt = null;
        try {
            //解析方式和密钥
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = JWT.decode(token);
            jwt = verifier.verify(decodedJWT);
        } catch (Exception e) {
            e.printStackTrace();
            // token 校检失败
        }
        return jwt;
    }


    /**
     * 解析token获取内容
     * @param token
     * @return
     */
    public static void getTokenRes(String token) {
        DecodedJWT jwt = verifyToken(token);
        Map<String, Claim> claimMap = jwt.getClaims();
        String uid = claimMap.get("uid").asString();//赋值返回值
        String userimg = claimMap.get("userimg").asString();
        String username = claimMap.get("username").asString();
        String userbimg = claimMap.get("userbimg").asString();
        log.error("数值="+uid + "--"+ userimg + "-- "+ username + "--" + userbimg);
    }

    /**
     *
     * @param userGameId 账号id
     * @param amount 充值金额
     * @param serviceRange  所在大区
     * @return
     */
    public String createToken(String userGameId,String amount,String serviceRange) {//token保存的数据
        JWTCreator.Builder builder = createTokenBuilder();
        String token = builder.withClaim("userGameId", userGameId)
                .withClaim("amount", amount)
                .withClaim("serviceRange",serviceRange)
                .sign(algorithm);
        return token;
    }

    public String sendPOst(CommonId commonId) {
        //String uri = "http://127.0.0.1:8332/mo-yu/test/pushMoYuPersonData";
        //String uri = "http://121.43.122.3:8332/mo-yu/test/pushMoYuPersonData";
        String uri = "http://www.yuhangzhifu.cn:8332/mo-yu/test/pushMoYuPersonData";
        ObjectMapper mapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();
        // 以表单的方式提交
        headers.setContentType(MediaType.APPLICATION_JSON);
        String processString = null;
        try {
            processString = mapper.writeValueAsString(commonId);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //将请求头部和参数合成一个请求
        HttpEntity<String> entity = new HttpEntity<>(processString, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(uri, entity, String.class);
        System.out.printf("---="+response.getBody());
        return response.getBody();
    }

    public static void main(String[] ss) {
        String str = "6.0".split("\\.")[0];
        System.out.println(str);
    }

}


