package tests;

import common.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class PrizePage extends Utils {
    public static String url = "http://152.136.139.196:8080/admin.html";
    public PrizePage() {
        super(url);
    }

    //查看奖品列表成功
    public void prizeSuc(){
        //点击奖品列表
        driver.findElement(By.cssSelector("body > div.cont-box > div.sidebar > ul > li:nth-child(2) > ul > li:nth-child(1) > a")).click();

        //等待iframe加载并切换
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("contentFrame"));

        //是否跳转成功
        driver.findElement(By.cssSelector("body > div > h2"));

        driver.switchTo().defaultContent();
    }
}
