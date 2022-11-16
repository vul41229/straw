package cn.tedu.straw.search.controller;

import cn.tedu.straw.search.service.IQuestionService;
import cn.tedu.straw.search.vo.QuestionVo;
import com.github.pagehelper.PageInfo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/v3/questions")
public class QuestionController {
    @Resource
    private IQuestionService questionService;
    @PostMapping
    public PageInfo<QuestionVo> search(
            String key,
            Integer pageNum,
            @AuthenticationPrincipal UserDetails user){
        if (pageNum==null)
            pageNum=1;
        Integer pageSize=8;
        PageInfo<QuestionVo> pageInfo=questionService
                .search(key,user.getUsername(),pageNum,pageSize);

        return pageInfo;
    }
}
