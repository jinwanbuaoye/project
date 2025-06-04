package com.example.lotterysystem;

import com.example.lotterysystem.service.UserService;
import com.example.lotterysystem.service.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UserInfoTest{
    @Autowired
    private UserService userService;

    @Test
    void findBaseUserInfo(){
        List<UserDTO> userDTOList = userService.findUserInfo(null);
        for (UserDTO userDTO : userDTOList){
            System.out.println(userDTO.getUserId());
            System.out.println(userDTO.getUserName());
            System.out.println(userDTO.getIdentity());
        }
    }
}
