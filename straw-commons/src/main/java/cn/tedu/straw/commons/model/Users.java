package cn.tedu.straw.commons.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName(value = "user")
public class Users implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    //@TableField(value = "表中字段的名稱")
    private String name;
    private Integer age;
    private String sex;
}
