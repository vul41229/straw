package cn.tedu.straw.faq.controller;



import cn.tedu.straw.commons.exception.ServiceException;
import cn.tedu.straw.commons.model.Comment;
import cn.tedu.straw.faq.service.ICommentService;
import cn.tedu.straw.faq.vo.CommentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tedu.cn
 * @since 2022-09-22
 */
@RestController
@RequestMapping("/v2/comments")
@Slf4j
public class CommentController {
    @Autowired
    private ICommentService commentService;

    @PostMapping("")
    public Comment postComment(
            @Validated CommentVO commentVO,
            BindingResult result,
            @AuthenticationPrincipal UserDetails user){
        log.debug("評論對象{}",commentVO);
        if(result.hasErrors()){
            String message=result.getFieldError().getDefaultMessage();
            throw new ServiceException(message);
        }

        Comment comment = commentService.saveComment(commentVO,user.getUsername());
        return comment;
    }

    @GetMapping("/{id}/delete")
    public String removeComment(@PathVariable Integer id, @AuthenticationPrincipal UserDetails user){
        boolean isDelete=commentService.removeComment(id,user.getUsername());
        if(isDelete){
            return "ok";
        }else{
            return "fail";
        }
    }
    @PostMapping("/{commentId}/update")
    public Comment updateComment(@PathVariable Integer commentId,
                                 @Validated CommentVO commentVO,BindingResult result,
                                 @AuthenticationPrincipal UserDetails user){
        log.debug("接收到表單信息:{}",commentVO);
        log.debug("要修改的評論id:{}",commentId);
        if(result.hasErrors()){
            String message=result.getFieldError().getDefaultMessage();
            throw new ServiceException(message);
        }
        // 这里调用业务逻辑层
        Comment comment=commentService.updateComment(commentId,commentVO,user.getUsername());
        return comment;
    }

}
