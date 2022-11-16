package cn.tedu.straw.faq;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("cn.tedu.straw.faq.mapper")
@EnableKafka
public class StrawFaqApplication {

    public static void main(String[] args) {
        SpringApplication.run(StrawFaqApplication.class, args);
    }

    @Bean

    @LoadBalanced

    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
