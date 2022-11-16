package cn.tedu.straw.sys;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("cn.tedu.straw.sys.mapper")
public class StrawSysApplication {

    public static void main(String[] args) {
        SpringApplication.run(StrawSysApplication.class, args);
    }

    @Bean
    // LoadBalanced是负载均衡的意思
    // 微服务间调用不经过网关,所以网关中设置的负载均衡无效
    // 所以要设置Ribbon的负载均衡
    @LoadBalanced
    // RestTemplate就是能够实现微服务间调用的对象
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
