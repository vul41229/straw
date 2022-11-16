package cn.tedu.straw.sys;

import cn.tedu.straw.commons.model.User;
import cn.tedu.straw.sys.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@SpringBootTest
class StrawSysApplicationTests {

    @Resource
    UserMapper userMapper;


    @Resource
    RestTemplate restTemplate;

    @Test
    public void getUser(){

        // url路径中?之后的内容就是Ribbon请求的参数设置
        // 参数名称必须和控制器方法声明的参数名称一致
        // 参数值不用直接赋值,使用{1}来占位,需要更多参数时使用{2},{3}....
        String url="http://sys-service/v1/auth/user?username={1}";
        // 调用时,前两个参数意义不变,从第三个参数开始向{1}中传值
        User user=restTemplate.getForObject(url,User.class,"st2");
        System.out.println(user);

    }

    @Test
    public void ribbon(){
        // 发送ribbon请求需要先定义url
        // sys-service是服务的生产者注册到Nacos的名称
        // /v1/auth/demo就是要调用的控制方法的路径,既要调用的Rest接口
        String url="http://sys-service/v1/auth/demo";
        // restTemplate.getForObject就是在发起Ribbon请求
        // 参数url就是上面定义好的字符串
        // String.class就是这个Rest接口返回值类型的反射
        String object=restTemplate.getForObject(url,String.class);
        System.out.println(object);
    }

    @Test
    void contextLoads() {
    }


    @Test
    public void testMapper() {
        User user=userMapper.findUserByUserName("st2");
        System.out.println("st2"+user);
    }

}
