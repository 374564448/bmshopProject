package com.banmingi.bmshop.user.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * @auther 半命i 2019/11/18
 * @description
 */
@Data
@Table(name = "tb_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(min = 4,max = 30,message = "用户名必须在4~30位之间")
    private String username; //用户名

    @Length(min = 4,max = 30,message = "密码必须在4~30位之间")
    @JsonIgnore //对象序列化为json时,忽略该属性(不包含该属性)
    private String password; //密码

    @Pattern(regexp = "^1[3456789]\\d{9}$",message = "请输入正确格式的手机号码") //正则表达式校验手机号码
    private String phone;  //电话


    private Date created;  //创建时间


    @JsonIgnore
    private String salt; //密码加密的salt值

}
