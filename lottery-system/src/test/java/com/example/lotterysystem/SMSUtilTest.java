package com.example.lotterysystem;

import com.example.lotterysystem.common.utils.SMSUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SMSUtilTest {

    @Autowired
    private SMSUtil smsUtil;

    @Test
    void smsTest() {
        smsUtil.sendMessage(

                "SMS_154950909",
                "19182243902",
                "{\"code\":\"1234\"}");
        // {"code":"1234"}
    }

}