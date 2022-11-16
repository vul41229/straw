package cn.tedu.straw.sys.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class UserVo implements Serializable {

    private Integer id;
    private String username;
    private String nickname;


    private int questions;

    private int collections;
}
