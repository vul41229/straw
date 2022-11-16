package cn.tedu.straw.sys.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
public class RegisterVo implements Serializable {

    @NotBlank(message = "邀請碼不能為空")
    private String inviteCode;
    @NotBlank(message = "手機號不能為空")
    @Pattern(regexp = "^1\\d{10}$",message = "請輸入正確的手機號")
    private String phone;
    @NotBlank(message = "暱稱不能為空")
    @Pattern(regexp = "^.{2,20}$",message = "暱稱是2~20個字符")
    private String nickname;
    @NotBlank(message = "密碼不能為空")
    @Pattern(regexp = "^\\w{6,20}$",message = "密碼必須是6~20個字符")
    private String password;
    @NotBlank(message = "確認密碼不能為空")
    private String confirm;
}
