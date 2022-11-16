package cn.tedu.straw.faq.service;


import cn.tedu.straw.commons.model.Question;
import cn.tedu.straw.faq.vo.QuestionVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tedu.cn
 * @since 2022-09-22
 */
public interface IQuestionService extends IService<Question> {

    PageInfo<Question> getMyQuestions(String username, Integer pageNum, Integer pageSize);

    void saveQuestion(QuestionVO questionVO, String username);

    PageInfo<Question> getTeacherQuestions(String username, Integer pageNum,Integer pageSize);

    Question getQuestionById(Integer id);

    Integer countQuestionsByUserId(Integer userId);

    PageInfo<Question> getQuestions(Integer pageNum,Integer pageSize);
}
