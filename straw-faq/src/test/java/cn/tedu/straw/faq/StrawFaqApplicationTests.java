package cn.tedu.straw.faq;

import cn.tedu.straw.commons.model.Tag;
import cn.tedu.straw.faq.mapper.TagMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class StrawFaqApplicationTests {
    @Resource
    TagMapper tagMapper;

    @Resource
    RedisTemplate<String,String> redisTemplate;


    @Test
    void contextLoads() {
    }

    @Test
    public void testMapper(){
        List<Tag> tags=tagMapper.selectList(null);
        tags.forEach(tag -> System.out.println(tag));
    }

    @Test
    public void redis(){
        // 向redis中新增一个数据
        redisTemplate.opsForValue().set("mystr","小媛");
        System.out.println("數據以添加");
    }

    @Test
    public void getValue(){
        // 从Redis中获得指定key的value
        String mystr=redisTemplate.opsForValue().get("mystr");
        System.out.println(mystr);
    }
}
