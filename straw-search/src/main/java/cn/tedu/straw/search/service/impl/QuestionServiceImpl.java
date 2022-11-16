package cn.tedu.straw.search.service.impl;

import cn.tedu.straw.commons.model.Tag;
import cn.tedu.straw.commons.model.User;
import cn.tedu.straw.search.repository.QuestionRepository;
import cn.tedu.straw.search.service.IQuestionService;
import cn.tedu.straw.search.utils.Pages;
import cn.tedu.straw.search.vo.QuestionVo;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;

@Service
@Slf4j
public class QuestionServiceImpl implements IQuestionService {

    @Resource
    private RestTemplate restTemplate;
    @Resource
    private QuestionRepository questionRepository;
    @Override
    public void syncData() {

        String url= "http://faq-service/v2/questions/page/count?pageSize={1}";
        int pageSize=8;
        Integer total=restTemplate.getForObject(url,Integer.class,pageSize);

        for(int i=1;i<=total;i++){
            url= "http://faq-service/v2/questions/page?pageNum={1}&pageSize={2}";

            QuestionVo[] questions=restTemplate.getForObject(url,QuestionVo[].class,i,pageSize);

            questionRepository.saveAll(Arrays.asList(questions));

            log.debug("完成了第{}頁的新增",i);
        }

    }

    @Override
    public PageInfo<QuestionVo> search(String key, String username, Integer pageNum, Integer pageSize) {
        String url="http://sys-service/v1/auth/user?username={1}";
        User user=restTemplate.getForObject(url,User.class,username);
        // 调用数据访问层方法执行搜索
        Pageable pageable= PageRequest.of(pageNum-1,pageSize,
                Sort.Direction.DESC,"createtime");
        Page<QuestionVo> page=questionRepository.queryAllByParams(key,key,user.getId(),pageable);

        for(QuestionVo vo:page){
            vo.setTags(tagNamesToTags(vo.getTagNames()));
        }

        // 将查询结果转换为PageInfo类型返回
        return Pages.pageInfo(page);
    }

    @Override
    public void saveQuestion(QuestionVo question) {
        questionRepository.save(question);
    }


    private List<Tag> tagNamesToTags(String tagNames) {

        String[] names = tagNames.split(",");

        String url = "http://faq-service/v2/tags";
        Tag[] tagArr = restTemplate.getForObject(url, Tag[].class);

        Map<String, Tag> tagMap = new HashMap<>();
        for (Tag t : tagArr) {
            tagMap.put(t.getName(), t);
        }

        List<Tag> tags = new ArrayList<>();

        for (String name : names) {
            tags.add(tagMap.get(name));
        }
        return tags;
    }


}
