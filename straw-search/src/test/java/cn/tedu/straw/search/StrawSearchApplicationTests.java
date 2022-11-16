package cn.tedu.straw.search;

import cn.tedu.straw.search.repository.QuestionRepository;
import cn.tedu.straw.search.service.IQuestionService;
import cn.tedu.straw.search.vo.QuestionVo;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

@SpringBootTest
class StrawSearchApplicationTests {

	@Resource
	IQuestionService questionService;
	@Resource
	QuestionRepository questionRepository;
	@Test
	public void testAddEs(){
		questionService.syncData();
	}
	@Test
	void testQuery(){
		Page<QuestionVo> page=questionRepository
				.queryAllByParams("java","java",
						11, PageRequest.of(0,8));
		for(QuestionVo vo: page){
			System.out.println(vo);
		}
	}
	@Test
	void testService(){
		PageInfo<QuestionVo> pageInfo=
				questionService.search("java","st2",
						1,8);
		for (QuestionVo vo:pageInfo.getList()){
			System.out.println(vo);
		}
	}

	@Test
	void contextLoads() {
	}

}
