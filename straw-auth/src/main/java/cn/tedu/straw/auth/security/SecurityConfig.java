package cn.tedu.straw.auth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable() // 防跨域攻击
                .authorizeRequests()   // 访问权限设置
                .anyRequest().permitAll()  // 全部放行
                .and().formLogin();        //支持表单登录
    }
    // 我们的令牌和一些信息需要进行加密操作
    // 向Spring容器中保存一个加密对象供框架使用
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    // Spring Cloud Security 需要授权管理器对象
    // 当前类的父类中有这个类型对象
    // 我们要将它保存到Spring容器中,Oauth2后面的配置需要使用到它
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
