package cn.tedu.straw.sys.controller;


import cn.tedu.straw.commons.exception.ServiceException;
import cn.tedu.straw.commons.model.User;
import cn.tedu.straw.sys.service.IUserService;
import cn.tedu.straw.sys.vo.RegisterVo;
import cn.tedu.straw.sys.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tedu.cn
 * @since 2022-09-22
 */
@RestController
@RequestMapping("/v1/users")
@Slf4j
public class UserController {
    @Autowired
    private IUserService userService;

    @PostMapping("/register")
    public String register(@Validated RegisterVo registerVo, BindingResult result){
        log.debug("接收到用戶註冊訊息{}",registerVo);
        if (result.hasErrors()){
            String message = result.getFieldError().getDefaultMessage();
            return message;
        }
        try {
            userService.registerStudent(registerVo);
            return "ok";
        }catch (ServiceException e){
            log.error("註冊失敗",e);
            return e.getMessage();
        }
    }


    @GetMapping("/master")
    public List<User> master(){
        List<User> users=userService.getTeachers();
        return users;
    }

    @GetMapping("/me")
    public UserVo me(@AuthenticationPrincipal UserDetails user){
        UserVo userVO=userService.getUserVO(user.getUsername());
        return userVO;
    }


}
