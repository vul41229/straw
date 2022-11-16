package cn.tedu.straw.faq.controller;

import cn.tedu.straw.commons.model.Question;
import cn.tedu.straw.faq.service.IQuestionService;
import cn.tedu.straw.faq.vo.JsonResult;
import cn.tedu.straw.faq.vo.QuestionVO;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tedu.cn
 * @since 2022-09-22
 */
@Slf4j
@RestController
@RequestMapping("/v2/questions")
public class QuestionController {

    @Autowired
    private IQuestionService questionService;
    @GetMapping("/my")
    public PageInfo<Question> my(
            @AuthenticationPrincipal UserDetails user,Integer pageNum){
        Integer pageSize=3;
        if (pageNum==null){
            pageNum=1;
        }
        PageInfo<Question> pageInfo=questionService
                .getMyQuestions(user.getUsername(),pageNum,pageSize);
        return pageInfo;
    }

    @PostMapping("")
    public JsonResult createQuestion(@AuthenticationPrincipal User user,
                                     @Validated QuestionVO questionVO,
                                     BindingResult result) {
        log.debug("接收表单信息为:{}", questionVO);
        if (result.hasErrors()) {
            String message = result.getFieldError().getDefaultMessage();
            return JsonResult.unProcessableEntity(message);
        }
            questionService.saveQuestion(questionVO, user.getUsername());
            return JsonResult.created("創建成功");
    }

    @GetMapping("/teacher")
// 当前登录用户必须是讲师身份才能查询讲师任务列表
// 使用Spring-Security提供的权限\角色验证的功能来实现限制
// @PreAuthorize注解设置要求当前登录用户持有ROLE_TEACHER角色才能访问
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    public PageInfo<Question> teacher(
            @AuthenticationPrincipal UserDetails user,
            Integer pageNum){
        Integer pageSize=8;
        if(pageNum==null)
            pageNum=1;
        // 调用业务逻辑层方法
        PageInfo<Question> pageInfo=questionService
                .getTeacherQuestions(user.getUsername(),
                        pageNum,pageSize);
        return pageInfo;

    }

    @GetMapping("/{id}")
    public Question question(@PathVariable Integer id){
        Question question=questionService.getQuestionById(id);
        return  question;
    }

    @GetMapping("/count")
    public Integer count(Integer userId){
        return questionService.countQuestionsByUserId(userId);
    }

    @GetMapping("/page")
    public List<Question> questions(Integer pageNum, Integer pageSize){

        PageInfo<Question> pageInfo=questionService.getQuestions(pageNum,pageSize);

        return pageInfo.getList();
    }

    @GetMapping("/page/count")
    public int pageCount(Integer pageSize) {

        int count = questionService.count();

        return count%pageSize==0 ? count/pageSize
                : count/pageSize+1;
//        return (count + pageSize - 1) / pageSize;
    }
}
