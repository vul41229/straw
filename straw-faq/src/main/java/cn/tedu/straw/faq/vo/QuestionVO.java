package cn.tedu.straw.faq.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
public class QuestionVO implements Serializable {
    @NotBlank(message = "標題不能為空")
    @Pattern(regexp = "^.{3,50}$",message = "標題需要3~50個字符")
    private String title;
    @NotEmpty(message = "至少選擇一個標籤")
    private String[] tagNames={};
    @NotEmpty(message = "至少選擇一個講師")
    private String[] teacherNicknames={};
    @NotBlank(message = "問題內容不能為空")
    private String content;
}
