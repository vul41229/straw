package cn.tedu.straw.faq.kafka;

import cn.tedu.straw.commons.model.Question;
import cn.tedu.straw.commons.vo.Topic;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class QuestionProducer {

    @Resource
    private KafkaTemplate<String,String> kafkaTemplate;

    public void sendQuestion(Question question){

        Gson gson=new Gson();
        String json=gson.toJson(question);
        log.debug("發送問題內容:{}",json);
        kafkaTemplate.send(Topic.QUESTION,json);
    }
}
