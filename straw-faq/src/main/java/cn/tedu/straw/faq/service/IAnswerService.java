package cn.tedu.straw.faq.service;


import cn.tedu.straw.commons.model.Answer;
import cn.tedu.straw.faq.vo.AnswerVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tedu.cn
 * @since 2022-09-22
 */
public interface IAnswerService extends IService<Answer> {

    Answer saveAnswer(AnswerVO answerVO, String username);

    List<Answer> getAnswersByQuestionId(Integer id);

    boolean accept(Integer answerId,String username);
}
