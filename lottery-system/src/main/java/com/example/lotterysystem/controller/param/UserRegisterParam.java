package com.example.lotterysystem.controller.param;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserRegisterParam implements Serializable {
    @NotBlank(message = "姓名不能为空")
    private String name;//姓名

    @NotBlank(message = "邮箱不能为空")
    private String mail;//邮箱

    @NotBlank(message = "电话不能为空")
    private String phoneNumber;//电话

    private String password;//密码

    @NotBlank(message = "身份信息不能为空")
    private String identity;//身份信息
}
