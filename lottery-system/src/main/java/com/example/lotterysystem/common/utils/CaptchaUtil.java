package com.example.lotterysystem.common.utils;

import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import lombok.Data;

public class CaptchaUtil {
    /**
     * 生成随机验证码
     * @param length
     * @return
     */
    public static String getCaptcha(int length){
        RandomGenerator randomGenerator = new RandomGenerator("0123456789", length);
        LineCaptcha lineCaptcha = cn.hutool.captcha.CaptchaUtil.createLineCaptcha(200, 100);
        lineCaptcha.setGenerator(randomGenerator);
        // 重新生成code
        lineCaptcha.createCode();
        return lineCaptcha.getCode();
    }
}
