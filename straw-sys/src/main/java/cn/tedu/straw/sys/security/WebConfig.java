package cn.tedu.straw.sys.security;

import cn.tedu.straw.sys.interceptor.AuthInterceptor;
import cn.tedu.straw.sys.interceptor.DemoInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 配置当前项目的所有路径跨域请求
        registry.addMapping("/**") // 表示所有路径
                .allowedOrigins("*")         // 允许任何访问源
                .allowedMethods("*")         // 允许所有请求方法(get\post)
                .allowedHeaders("*");        // 允许任何请求头信息
    }

    @Resource
    private DemoInterceptor demoInterceptor;
    // 配置拦截器的方法
    @Resource
    private AuthInterceptor authInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 配置拦截器对象
        registry.addInterceptor(demoInterceptor)
                //设置生效路径
                .addPathPatterns("/v1/auth/demo");
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/v1/home","/v1/users/me");


    }
}
