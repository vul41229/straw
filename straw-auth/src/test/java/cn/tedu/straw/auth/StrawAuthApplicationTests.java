package cn.tedu.straw.auth;

import cn.tedu.straw.auth.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.Resource;

@SpringBootTest
class StrawAuthApplicationTests {

    @Resource
    UserDetailsServiceImpl userDetailsService;

    @Test
    public void testLoadUser() {
        UserDetails user=userDetailsService.loadUserByUsername("st2");
        System.out.println(user);
    }

    @Test
    void contextLoads() {
    }

}
