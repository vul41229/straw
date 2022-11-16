package cn.tedu.straw.faq.service;


import cn.tedu.straw.commons.model.Comment;
import cn.tedu.straw.faq.vo.CommentVO;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tedu.cn
 * @since 2022-09-22
 */
public interface ICommentService extends IService<Comment> {

    Comment saveComment(CommentVO commentVO, String username);

    boolean removeComment(Integer commentId,String username);

    Comment updateComment(Integer commentId, CommentVO commentVO,String username);
}
