package cn.tedu.straw.faq.security;

import cn.tedu.straw.faq.interceptor.AuthInterceptor;
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
                        "/v2/questions",   // 学生发布问题
                        "/v2/questions/my",       //学生首页问题列表
                        "/v2/questions/teacher",  //讲师首页任务列表
                        "/v2/answers",             // 讲师回复
                        "/v2/comments",            // 添加评论
                        "/v2/comments/*/delete",   // 删除评论
                        "/v2/comments/*/update",   // 修改评论
                        "/v2/answers/*/solved"     // 采纳回答
                );
    }
}
