package cn.tedu.straw.search.security;


import cn.tedu.straw.search.interceptor.AuthInterceptor;
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
    private AuthInterceptor authInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns(
                        "/v3/questions"    //搜索问题
                );
    }
}
