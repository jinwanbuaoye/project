package tests;

import common.Utils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class NewprizePage extends Utils {
    public static String url = "http://152.136.139.196:8080/admin.html";
    public NewprizePage() {
        super(url);
    }

    //新建奖品失败
    public void prizeFail(){
        //点击新建奖品
        driver.findElement(By.cssSelector("body > div.cont-box > div.sidebar > ul > li:nth-child(2) > ul > li:nth-child(2) > a")).click();

        //等待iframe加载并切换
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("contentFrame"));

        //查看是否跳转成功
        driver.findElement(By.cssSelector("body > div > h2"));

        //全都不填写，直接创建
        driver.findElement(By.cssSelector("body > div > button")).click();

        //弹窗
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        alert.accept(); // 处理弹窗

        driver.switchTo().defaultContent();
    }
}
