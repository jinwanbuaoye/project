package com.example.lotterysystem;

import com.example.lotterysystem.service.VerificationCodeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class VerificationCodeServiceTest {
    @Autowired
    private VerificationCodeService verificationCodeService;

    @Test
    void testSend(){
        verificationCodeService.sendVerificationCode("19182243902");
    }

    @Test
    void testGet(){
        verificationCodeService.getVerificationCode("19182243902");
        System.out.println(verificationCodeService.getVerificationCode("19182243902"));
    }

}
