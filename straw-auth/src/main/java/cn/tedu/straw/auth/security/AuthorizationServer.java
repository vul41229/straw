package cn.tedu.straw.auth.security;

import cn.tedu.straw.auth.service.UserDetailsServiceImpl;
import com.netflix.client.ssl.AbstractSslContextFactory;
import com.netflix.ribbon.proxy.annotation.Http;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.annotation.Resource;
import java.util.Arrays;

@Configuration
// 这个注解表示当前类是Oauth2标准下
// 实现授权服务器的配置类,启动授权服务器相关功能
@EnableAuthorizationServer
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    @Resource
    private AuthenticationManager authenticationManager;
    // 登录用用户详情类
    @Resource
    private UserDetailsServiceImpl userDetailsService;

    // 配置授权服务器的各种参数
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // Oauth2标准提供了很多控制器方法用于授权或验证
        // 这个方法参数endpoints翻译为"端点",这里指的就是Oauth2提供的控制器方法
        // 这个方法要做的就是配置端点,以便授权和验证等操作顺利执行
        endpoints
                // 配置授权管理器对象
                .authenticationManager(authenticationManager)
                // 配置登录时调用的获得用户详情的对象(我们自己编写的并以及测试通过)
                .userDetailsService(userDetailsService)
                // 配置登录信息提交的方法只能是Post
                .allowedTokenEndpointRequestMethods(HttpMethod.POST)
                // 配置生产令牌的对象
                .tokenServices(tokenService());
    }

    // 添加保存令牌配置的对象
    @Resource
    private TokenStore tokenStore;
    // 添加客户端详情对象(系统自动提供的)
    @Resource
    private ClientDetailsService clientDetailsService;
    // 配置生成令牌和保存令牌的方法
    @Resource
    private JwtAccessTokenConverter accessTokenConverter;
    // 配置生成令牌和保存令牌的方法
    @Bean
    public AuthorizationServerTokenServices tokenService() {
        // 创建生产令牌的对象
        DefaultTokenServices services=new DefaultTokenServices();
        // 设置令牌如何保存
        services.setTokenStore(tokenStore);
        // 实例化令牌增强对象
        TokenEnhancerChain chain=new TokenEnhancerChain();
        // 设置JWT转换对象到令牌增强对象中
        chain.setTokenEnhancers(Arrays.asList(accessTokenConverter));
        // 将令牌增强对象,加载到生成令牌的对象中
        services.setTokenEnhancer(chain);
        // 设置令牌有效期(单位是秒  3600既1小时)
        services.setAccessTokenValiditySeconds(3600);
        // 配置这个令牌为哪个客户端生成
        services.setClientDetailsService(clientDetailsService);
        // 别忘了返回services对象
        return services;
    }

    // 获得加密对象
    @Resource
    private PasswordEncoder passwordEncoder;

    // 重写方法2配置客户端对应的各种权限
    // 这里的客户端指所有依赖当前授权服务器进行授权和登录的项目
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 当前项目只支持达内知道进行授权
        // "所有客户端"只包含达内知道项目
        clients.inMemory()   // 将客户端信息保存在内存中
                .withClient("knows") //开始配置达内知道(knows)项目的权限
                // 配置达内知道客户端登录时的密码(加密的)
                .secret(passwordEncoder.encode("123456"))
                .scopes("main") //配置当前客户端的权限 main只是个名字
                // 设置客户端登录的方式
                // password表示用户名密码登录的方式
                // 这个字符串是框架识别的关键字,不能写错
                .authorizedGrantTypes("password");
    }

    // 重写方法3配置客户端允许使用的功能
    // 这个配置设置资源服务器的安全
    // 规定哪些资源可以被客户端访问
    // 我们的项目所有资源都对所有客户端开放,所以配置比较简单
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                // 允许哪些客户端访问生产令牌的端点(permitAll():全部允许)
                .tokenKeyAccess("permitAll()")
                // 允许哪些客户端访问验证令牌的端点
                .checkTokenAccess("permitAll()")
                // 允许通过验证的客户端获得令牌
                .allowFormAuthenticationForClients();
    }
}
