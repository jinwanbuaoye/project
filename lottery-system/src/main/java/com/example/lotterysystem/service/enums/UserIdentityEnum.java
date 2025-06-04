package com.example.lotterysystem.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserIdentityEnum {
    ADMIN("管理员"),
    NORMAL("普通用户");

    private final String massage;
    public static UserIdentityEnum forName(String name){
        for (UserIdentityEnum userIdentityEnum : UserIdentityEnum.values()){
            if (userIdentityEnum.name().equalsIgnoreCase(name)){
                return userIdentityEnum;
            }
        }
        return null;
    }
}
