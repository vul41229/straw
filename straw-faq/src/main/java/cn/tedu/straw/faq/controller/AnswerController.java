package cn.tedu.straw.faq.controller;


import cn.tedu.straw.commons.exception.ServiceException;
import cn.tedu.straw.commons.model.Answer;
import cn.tedu.straw.faq.service.IAnswerService;
import cn.tedu.straw.faq.vo.AnswerVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@RestController
@RequestMapping("/v2/answers")
@Slf4j
public class AnswerController {
    @Autowired
    private IAnswerService answerService;


    @PostMapping("")
    public Answer postAnswer(@Validated AnswerVO answerVO, BindingResult result,
                             @AuthenticationPrincipal UserDetails user){
        log.debug("表單信息:{}",answerVO);
        if(result.hasErrors()){
            String message=result.getFieldError().getDefaultMessage();
            throw new ServiceException(message);
        }
        Answer answer = answerService.saveAnswer(answerVO,user.getUsername());
        log.debug("返回的答案{}",answer);
        return  answer;
    }
    @GetMapping("/question/{id}")
    public List<Answer> questionAnswers(@PathVariable Integer id){
        List<Answer> answers=answerService.getAnswersByQuestionId(id);
        return answers;
    }

    @GetMapping("/{answerId}/solved")
    public String solved(
            @PathVariable Integer answerId,
            @AuthenticationPrincipal UserDetails user){
        // 调用业务逻辑层方法
        boolean accepted=answerService.accept(
                answerId,  user.getUsername());
        if(accepted){
            return "採納完成";
        }else{
            return "您不能採納別人的問題!";
        }
    }
}
