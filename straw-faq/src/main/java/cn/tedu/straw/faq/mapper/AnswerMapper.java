package cn.tedu.straw.faq.mapper;


import cn.tedu.straw.commons.model.Answer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* <p>
    *  Mapper 接口
    * </p>
*
* @author tedu.cn
* @since 2022-09-22
*/
    @Repository
    public interface AnswerMapper extends BaseMapper<Answer> {

        List<Answer> findAnswersByQuestionId(Integer questionId);

    @Update("update answer set accept_status=#{acceptStatus} " + " where id=#{answerId}")
    int updateAcceptStatus(@Param("acceptStatus") Integer acceptStatus, @Param("answerId") Integer answerId);
    }
