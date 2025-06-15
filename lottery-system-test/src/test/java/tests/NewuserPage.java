package tests;

import common.Utils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class NewuserPage extends Utils {
    public static String url = "http://152.136.139.196:8080/admin.html";
    public NewuserPage() {
        super(url);
    }

    //新建奖品失败
    public void newUserFail(){
        //点击注册用户
        driver.findElement(By.cssSelector("#register")).click();

        //等待iframe加载并切换
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("contentFrame"));

        //查看是否跳转成功
        driver.findElement(By.cssSelector("body > div > h2"));

        //全都不填写，直接创建
        driver.findElement(By.cssSelector("#registerForm > button")).click();

        //错误提示
        driver.findElement(By.cssSelector("#name-error"));

        //填写名字
        driver.findElement(By.cssSelector("#name")).sendKeys("诺基亚");
        driver.findElement(By.cssSelector("#registerForm > button")).click();

        //错误提示
        driver.findElement(By.cssSelector("#mail-error"));

        //已有邮箱
        driver.findElement(By.cssSelector("#mail")).sendKeys("000@qq.com");
        driver.findElement(By.cssSelector("#phoneNumber")).sendKeys("19111111111");
        driver.findElement(By.cssSelector("#registerForm > button")).click();

        //弹窗
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        alert.accept(); // 处理弹窗

    }

    //新建奖品成功
    public void newUserSuc(){

        //填写
        driver.findElement(By.cssSelector("#mail")).clear();
        driver.findElement(By.cssSelector("#mail")).sendKeys("999@qq.com");

        driver.findElement(By.cssSelector("#registerForm > button")).click();


        driver.switchTo().defaultContent();
    }
}
