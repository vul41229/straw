package cn.tedu.straw.sys.controller;

import cn.tedu.straw.commons.model.Permission;
import cn.tedu.straw.commons.model.Role;
import cn.tedu.straw.commons.model.User;
import cn.tedu.straw.sys.service.IUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {
    @Resource
    private IUserService userService;

    @GetMapping("/demo")
    public String demo(){
        System.out.println("demo方法運行");
        return "controller demo";
    }

    @GetMapping("/user")
    public User getUser(String username) {
        return userService.getUserByUsername(username);
    }

    @GetMapping("/permissions")
    public List<Permission> permissions(Integer id){
        return userService.getPermissionsById(id);
    }

    @GetMapping("/role")
    public List<Role> roles(Integer id){
        return userService.getRolesById(id);
    }

}
