package cn.tedu.straw.faq.service.impl;

import cn.tedu.straw.commons.exception.ServiceException;
import cn.tedu.straw.commons.model.*;
import cn.tedu.straw.faq.kafka.QuestionProducer;
import cn.tedu.straw.faq.mapper.QuestionMapper;
import cn.tedu.straw.faq.mapper.QuestionTagMapper;
import cn.tedu.straw.faq.mapper.UserQuestionMapper;
import cn.tedu.straw.faq.service.IQuestionService;

import cn.tedu.straw.faq.service.ITagService;
import cn.tedu.straw.faq.vo.QuestionVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tedu.cn
 * @since 2022-09-22
 */
@Slf4j
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements IQuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionTagMapper questionTagMapper;

    @Autowired
    private UserQuestionMapper userQuestionMapper;

    @Resource
    private RestTemplate restTemplate;
    // 利用Ribbon根据用户名获得用户对象的方法

    @Resource
    private QuestionProducer questionProducer;

    private User getUser(String username){
        String url="http://sys-service/v1/auth/user?username={1}";
        User user=restTemplate.getForObject(url,User.class,username);
        return user;
    }

    @Override
    public PageInfo<Question> getMyQuestions(String username,Integer pageNum,Integer pageSize) {

        User user = getUser(username);

        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        queryWrapper.eq("delete_status", 0);
        queryWrapper.orderByDesc("createtime");
        PageHelper.startPage(pageNum,pageSize);
        List<Question> list = questionMapper.selectList(queryWrapper);
        for(Question question:list) {
            List<Tag> tags = tagNamesToTags(question.getTagNames());
            question.setTags(tags);
        }
        return new PageInfo<>(list);
    }
    @Transactional
    @Override
    public void saveQuestion(QuestionVO questionVO, String username) {
        User user = getUser(username);
        System.out.println("標籤初始狀態"+ Arrays.toString(questionVO.getTagNames()));

        StringBuilder builder=new StringBuilder();
        for(String tagName : questionVO.getTagNames()){
            builder.append(tagName).append(",");
        }
        System.out.println("中間轉換狀態"+builder);

        String tagNames=builder.deleteCharAt(builder.length()-1).toString();
        System.out.println("最終的tagNames"+tagNames);

        Question question=new Question()
                .setTitle(questionVO.getTitle())
                .setContent(questionVO.getContent())
                .setUserNickName(user.getNickname())
                .setUserId(user.getId())
                .setCreatetime(LocalDateTime.now())
                .setStatus(0)
                .setPageViews(0)
                .setPublicStatus(0)
                .setDeleteStatus(0)
                .setTagNames(tagNames);
        int num=questionMapper.insert(question);
        if (num!=1){
            throw new ServiceException("數據庫異常!");
        }

        Map<String,Tag> tagMap=tagService.getTagMap();

        for(String tagName : questionVO.getTagNames()){
            Tag t=tagMap.get(tagName);
            QuestionTag questionTag=new QuestionTag()
                    .setQuestionId(question.getId())
                    .setTagId(t.getId());
            num=questionTagMapper.insert(questionTag);
            if(num!=1){
                throw new ServiceException("數據庫異常");
            }
            log.debug("新增了問題和標籤的關係:{}",questionTag);
        }

        String url="http://sys-service/v1/users/master";
        User[] teachers=restTemplate.getForObject(url,User[].class);
        Map<String,User> teacherMap=new HashMap<>();
        for(User u:teachers){
            teacherMap.put(u.getNickname(),u);
        }
        for(String nickname : questionVO.getTeacherNicknames()){
            User teacher=teacherMap.get(nickname);
            UserQuestion userQuestion=new UserQuestion()
                    .setQuestionId(question.getId())
                    .setUserId(teacher.getId())
                    .setCreatetime(LocalDateTime.now());
            num=userQuestionMapper.insert(userQuestion);
            if(num!=1){
                throw new ServiceException("數據庫忙");
            }
            log.debug("新增了問題和講師的關係:{}",userQuestion);
        }
        questionProducer.sendQuestion(question);
    }

    @Autowired
    private ITagService tagService;

    //将tagNames属性转换为List<Tag>的方法
    private List<Tag> tagNamesToTags(String tagNames) {

        String[] names = tagNames.split(",");


        Map<String, Tag> tagMap = tagService.getTagMap();

        List<Tag> tags = new ArrayList<>();
        for (String name : names) {
            Tag t = tagMap.get(name);
            tags.add(t);
        }
        return tags;
    }

    @Override
    public PageInfo<Question> getTeacherQuestions(String username, Integer pageNum, Integer pageSize) {
        User user=getUser(username);
        // 设置分页条件
        PageHelper.startPage(pageNum,pageSize);
        // 执行查询
        List<Question> list=questionMapper
                .findTeacherQuestions(user.getId());
        //list就是查询讲师任务列表的分页结果
        for(Question q:list){
            List<Tag> tags=tagNamesToTags(q.getTagNames());
            q.setTags(tags);
        }
        // 千万别忘了返回任务列表
        return new PageInfo<>(list);
    }

    @Override
    public Question getQuestionById(Integer id) {
        Question question=questionMapper.selectById(id);
        // 获得当前Question对象的所有tags集合
        List<Tag> tags=tagNamesToTags(question.getTagNames());
        question.setTags(tags);
        // 千万别忘了返回question
        return question;
    }

    @Override
    public Integer countQuestionsByUserId(Integer userId) {
        return questionMapper.countQuestionsByUserId(userId);
    }

    @Override
    public PageInfo<Question> getQuestions(Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum,pageSize);
        List<Question> list=questionMapper.selectList(null);

        return new PageInfo<>(list);
    }


}
