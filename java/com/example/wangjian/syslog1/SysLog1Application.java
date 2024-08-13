package com.example.wangjian.syslog1;


import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@MapperScan("com.example.wangjian.syslog1.mapper")
public class SysLog1Application {

    public static void main(String[] args) {
        Factory.setOptions(getOptions());
        System.setProperty("org.apache.tomcat.util.buf.UDecoder.ALLOW_ENCODED_SLASH", "true");
        SpringApplication.run(SysLog1Application.class, args);
    }

    private static Config getOptions() {
        Config config = new Config();
        config.protocol  ="https";
        config.gatewayHost = "openapi-sandbox.dl.alipaydev.com/gateway.do";
        config.signType = "RSA2";
        return config;
    }

    @Bean
    RestTemplate restTemplate(){
        return new RestTemplate();
    }

}
