package cn.tedu.straw.search.service;

import cn.tedu.straw.search.vo.QuestionVo;
import com.github.pagehelper.PageInfo;

public interface IQuestionService {
    void syncData();
    PageInfo<QuestionVo> search(String key, String username, Integer pageNum, Integer pageSize);

    void saveQuestion(QuestionVo question);
}
