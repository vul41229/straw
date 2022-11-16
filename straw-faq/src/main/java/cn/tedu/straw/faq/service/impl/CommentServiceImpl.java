package cn.tedu.straw.faq.service.impl;


import cn.tedu.straw.commons.exception.ServiceException;
import cn.tedu.straw.commons.model.Comment;
import cn.tedu.straw.commons.model.User;
import cn.tedu.straw.faq.mapper.CommentMapper;
import cn.tedu.straw.faq.service.ICommentService;
import cn.tedu.straw.faq.vo.CommentVO;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tedu.cn
 * @since 2022-09-22
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Resource
    private RibbonClient ribbonClient;

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public Comment saveComment(CommentVO commentVO, String username) {
        User user =ribbonClient.getUser (username);
        Comment comment=new Comment()
                .setUserId(user.getId())
                .setUserNickName(user.getNickname())
                .setAnswerId(commentVO.getAnswerId())
                .setContent(commentVO.getContent())
                .setCreatetime(LocalDateTime.now());
        int num=commentMapper.insert(comment);
        if(num!=1){
            throw new ServiceException("數據庫繁忙");
        }

        return comment;
    }

    @Override
    public boolean removeComment(Integer commentId, String username) {
        User user=ribbonClient.getUser(username);
        //判断是不是讲师
        if(user.getType().equals(1)){
            // 如果是讲师,允许删除任何评论
            int num=commentMapper.deleteById(commentId);
            return num==1;
        }
        // 不是讲师删除评论要判断当前登录的用户是不是评论的发布者
        // 而评论的发布者id在comment对象中,我们需要先通过id查询到它
        Comment comment=commentMapper.selectById(commentId);
        // 判断评论的发布者是不是登录用户
        if(comment.getUserId().equals(user.getId())){
            // 如果是评论的发布者在删除评论,直接删除
            int num=commentMapper.deleteById(commentId);
            return num==1;
        }
        throw new ServiceException("您不能刪除別人的評論!");
    }

    @Override
    @Transactional
    public Comment updateComment(Integer commentId, CommentVO commentVO, String username) {
        User user=ribbonClient.getUser(username);
        // 按id查询当前要修改的评论对象
        Comment comment=commentMapper.selectById(commentId);
        // 如果是讲师或者是评论的发布者,可以修改评论
        if(user.getType().equals(1) || comment.getUserId().equals(user.getId())){
            // 将要修改的属性直接赋值
            comment.setContent(commentVO.getContent());
            // 执行修改,将修改后的对象提交到数据库
            int num=commentMapper.updateById(comment);
            if(num!=1){
                throw new ServiceException("數據庫忙!");
            }
            // 如果num是1就是修改成功了!返回修改成功的对象
            return comment;
        }
        throw new ServiceException("您不能修改別人的評論");
    }
}
