package cn.tedu.straw.faq.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class CommentVO implements Serializable {

    @NotNull(message = "回答id不能為空")
    private Integer answerId;

    @NotBlank(message = "評論内容不能為空")
    private String content;
}
