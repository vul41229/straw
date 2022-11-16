package cn.tedu.straw.sys.service;

import cn.tedu.straw.commons.model.Permission;
import cn.tedu.straw.commons.model.Role;
import cn.tedu.straw.commons.model.User;
import cn.tedu.straw.sys.vo.RegisterVo;
import cn.tedu.straw.sys.vo.UserVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tedu.cn
 * @since 2022-09-22
 */
public interface IUserService extends IService<User> {

    void registerStudent(RegisterVo registerVO);
    List<User> getTeachers();

    Map<String,User> getTeacherMap();

    UserVo getUserVO(String username);

    User getUserByUsername(String username);

    List<Permission> getPermissionsById(Integer id);

    List<Role> getRolesById(Integer id);

}
