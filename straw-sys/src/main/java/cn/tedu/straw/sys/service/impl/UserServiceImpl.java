package cn.tedu.straw.sys.service.impl;


import cn.tedu.straw.commons.exception.ServiceException;
import cn.tedu.straw.commons.model.*;
import cn.tedu.straw.sys.mapper.ClassroomMapper;
import cn.tedu.straw.sys.mapper.UserMapper;
import cn.tedu.straw.sys.mapper.UserRoleMapper;
import cn.tedu.straw.sys.service.IUserService;
import cn.tedu.straw.sys.vo.RegisterVo;
import cn.tedu.straw.sys.vo.UserVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tedu.cn
 * @since 2022-09-22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private ClassroomMapper classroomMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    private List<User> teachers = new CopyOnWriteArrayList<>();

    private Map<String,User> teacherMap = new ConcurrentHashMap<>();
    @Override
    public void registerStudent(RegisterVo registerVO) {

        QueryWrapper<Classroom> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("invite_code",registerVO.getInviteCode());
        Classroom classroom = classroomMapper.selectOne(queryWrapper);

        if(classroom==null){
            throw new ServiceException("邀请码错误!");
        }

        User user=userMapper.findUserByUserName(registerVO.getPhone());

        if(user!=null){
            throw new ServiceException("手機號已註冊過!");
        }

        PasswordEncoder encoder=new BCryptPasswordEncoder();

        String pwd="{bcrypt}"+encoder.encode(registerVO.getPassword());

        User stu=new User()
                .setUsername(registerVO.getPhone())
                .setNickname(registerVO.getNickname())
                .setPassword(pwd)
                .setClassroomId(classroom.getId())
                .setCreatetime(LocalDateTime.now())
                .setEnabled(1)
                .setLocked(0)
                .setType(0);
        // 7.将User对象新增到数据库
        int num=userMapper.insert(stu);
        if(num!=1){
            throw new ServiceException("数据库异常");
        }

        UserRole userRole=new UserRole()
                .setRoleId(2)
                .setUserId(stu.getId());
        num=userRoleMapper.insert(userRole);
        if(num!=1){
            throw new ServiceException("数据库异常");
        }


    }

    @Override
    public List<User> getTeachers() {
        if(teachers.isEmpty()){
            synchronized (teachers){
                if(teachers.isEmpty()){
                    List<User> users=userMapper.findTeachers();
                    teachers.addAll(users);
                    for(User u:users){
                        teacherMap.put(u.getNickname(),u);
                    }
                }
            }
        }
        return teachers;
    }

    @Override
    public Map<String, User> getTeacherMap() {
        if(teacherMap.isEmpty()){
            getTeachers();
        }
        return teacherMap;
    }

    @Resource
    private RestTemplate restTemplate;

    @Override
    public UserVo getUserVO(String username) {
        User user=userMapper.findUserByUserName(username);

        String url= "http://faq-service/v2/questions/count?userId={1}";

        Integer count=restTemplate.getForObject(url,Integer.class,user.getId());

        UserVo userVO=new UserVo()
                .setId(user.getId())
                .setUsername(user.getUsername())
                .setNickname(user.getNickname())
                .setQuestions(count);

        return userVO;
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.findUserByUserName(username);
    }

    @Override
    public List<Permission> getPermissionsById(Integer id) {
        return userMapper.findUserPermissionById(id);
    }

    @Override
    public List<Role> getRolesById(Integer id) {
        return userMapper.findUserRolesById(id);
    }
}
