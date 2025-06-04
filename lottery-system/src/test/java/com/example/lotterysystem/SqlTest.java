package com.example.lotterysystem;

import com.example.lotterysystem.dao.dataobject.Encrypt;
import com.example.lotterysystem.dao.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SqlTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    void mailcount(){
        int count = userMapper.countByMail("444");
        System.out.println("count:"+count);
    }

    @Test
    void phonecount(){
        int count = userMapper.countByPhoneNumber(new Encrypt("555"));
        System.out.println("count:"+count);
    }
}
