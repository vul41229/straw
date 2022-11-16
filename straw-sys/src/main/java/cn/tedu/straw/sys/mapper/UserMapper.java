package cn.tedu.straw.sys.mapper;

import cn.tedu.straw.commons.model.Permission;
import cn.tedu.straw.commons.model.Role;
import cn.tedu.straw.commons.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* <p>
    *  Mapper 接口
    * </p>
*
* @author tedu.cn
* @since 2022-09-22
*/
    @Repository
    public interface UserMapper extends BaseMapper<User> {

    @Select("select id,username,nickname,password," +
            "sex,birthday,phone,classroom_id,createtime," +
            "enabled,locked,type,self_introduction " +
            "from user where username=#{username}")
    User findUserByUserName(String username);

    @Select("select p.id,p.name " +
            "from user u " +
            "left join user_role ur on u.id=ur.user_id " +
            "left join role r ON ur.role_id=r.id " +
            "left join role_permission rp on r.id=rp.role_id " +
            "left join permission p on rp.permission_id=p.id " +
            "where u.id=#{id}")
    List<Permission> findUserPermissionById(Integer id);


    @Select("select * from user where type=1")
    List<User> findTeachers();


    @Select("SELECT r.id , r.name\n" +
            "FROM user u\n" +
            "LEFT JOIN user_role ur ON u.id=ur.user_id\n" +
            "LEFT JOIN role r       ON r.id=ur.role_id\n" +
            "WHERE u.id=#{id}")
    List<Role> findUserRolesById(Integer userId);
    }
