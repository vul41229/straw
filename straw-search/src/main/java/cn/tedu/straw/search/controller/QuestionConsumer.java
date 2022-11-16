package cn.tedu.straw.search.controller;

import cn.tedu.straw.commons.vo.Topic;
import cn.tedu.straw.search.service.IQuestionService;
import cn.tedu.straw.search.vo.QuestionVo;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class QuestionConsumer {
    @Resource
    private IQuestionService questionService;

    @KafkaListener(topics = Topic.QUESTION)
    public void questionReceive(ConsumerRecord<String,String> record){

        String json=record.value();
        Gson gson=new Gson();
        QuestionVo question=gson.fromJson(json,QuestionVo.class);

        questionService.saveQuestion(question);
        log.debug("成功的新增問題到ES:{}",question);
    }
}
