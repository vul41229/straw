package cn.tedu.straw.faq.interceptor;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    //解析Jwt需要Ribbon
    @Resource
    private RestTemplate restTemplate;
    // 在运行控制器方法之前运行的拦截器方法:
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获得前端请求中包含的jwt信息
        String token=request.getParameter("accessToken");
        // 向auth模块发送ribbon请求解析jwt
        String url="http://auth-service/oauth/check_token?token={1}";
        // 我们可以使用Map类型类接收Ribbon请求的结果
        // Ribbon调用时会自动将json对象解析为键值对的格式赋值给Map对象
        Map<String,Object> map=restTemplate.getForObject(url,Map.class,token);
        // 根据auth模块的解析令牌测试的结果可知
        // 我们需要的用户名key为user_name,用户的权限信息为authorities
        String username=map.get("user_name").toString();
        List<String> list=(List<String>)map.get("authorities");
        // 下面我们需要将用户信息保存到Spring-Security
        // 向Spring-Security中保存用户信息的代码是固定的,而且非常严谨,
        // 必须按照框架规定的方式进行编写
        // 首先需要将我们包含所有权限的List<String> 转换为String[]
        String[] auth=list.toArray(new String[0]);
        UserDetails details= User.builder()
                .username(username)
                .password("")
                .authorities(auth)
                .build();
        // 获得了用户详情,下面要按照Spring-Security给定的方式
        // 将用户详情对象保存到Spring-Security环境中,以便控制器中获得
        PreAuthenticatedAuthenticationToken authenticationToken=
                new PreAuthenticatedAuthenticationToken(
                        details,
                        details.getPassword(),
                        AuthorityUtils.createAuthorityList(auth));
        // 和当前请求进行关联后才能在控制层中获取
        authenticationToken.setDetails(
                new WebAuthenticationDetails(request));
        // 将用户详情保存到Spring-Security容器
        SecurityContextHolder.getContext()
                .setAuthentication(authenticationToken);
        // 千万别忘了返回true,否则无法运行控制器方法
        return true;
    }
}
