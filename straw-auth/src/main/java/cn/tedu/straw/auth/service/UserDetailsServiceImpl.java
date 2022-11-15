package cn.tedu.straw.auth.service;

import cn.tedu.straw.commons.model.Permission;
import cn.tedu.straw.commons.model.Role;
import cn.tedu.straw.commons.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;


@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private RestTemplate restTemplate;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名获得用户对象
        String url="http://sys-service/v1/auth/user?username={1}";
        User user=restTemplate.getForObject(url  ,  User.class  ,  username);
        // 判断用户对象是否为空
        if(user == null){
            throw new UsernameNotFoundException("用戶名不存在!");
        }
        // 获得当前用户所有权限
        url="http://sys-service/v1/auth/permissions?id={1}";
        // 当Ribbon请求的目标方法返回值是List类型时
        // 要使用该List泛型类型的数组来接收这个返回值
        Permission[] permissions=restTemplate.getForObject(url, Permission[].class ,user.getId());
        // 获得当前用户所有角色
        url="http://sys-service/v1/auth/role?id={1}";
        Role[] roles=restTemplate.getForObject(url, Role[].class, user.getId());
        // 将权限和角色保存在数组中
        String[] auth=new String[permissions.length+roles.length];
        int i=0;
        for(Permission p: permissions){
            auth[i++]=p.getName();
        }
        for(Role r:roles){
            auth[i++]=r.getName();
        }
        // 获得UserDetails对象并返回
        UserDetails details= org.springframework.security.core
                .userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(auth)
                .accountLocked(user.getLocked()==1)
                .disabled(user.getEnabled()==0)
                .build();
        // 千万别忘了返回details
        return details;
    }
}
