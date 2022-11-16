package cn.tedu.straw.faq.vo;


import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class AnswerVO implements Serializable {

        @NotNull(message = "問題id不能為空")
        private Integer questionId;

        @NotBlank(message = "問題內容不能為空")
        private String content;

}
