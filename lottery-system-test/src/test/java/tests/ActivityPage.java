package tests;

import common.Utils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;

public class ActivityPage extends Utils {

    public static String url = "http://152.136.139.196:8080/admin.html";
    public ActivityPage() {
        super(url);
    }

    //成功登陆
    public void ActivitLogin() throws InterruptedException {
        Thread.sleep(1000);
        driver.findElement(By.cssSelector("body > div.cont-box > div.sidebar > ul > li:nth-child(1) > div > span"));
    }

    //未登录直接访问
    public void ActivityNoLogin() throws IOException {

        //保证未登录
        driver.findElement(By.cssSelector("body > div.header-box > div.user-box > div")).click();
        driver.get(url);

        //处理警告弹窗
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        //Alert alert = driver.switchTo().alert();
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());

        //确认
        alert.accept();


        //跳转登陆界面--检查是否成功
        String expect = driver.getTitle();
        assert expect.equals("管理员登录页面");

        getScreenShot(Thread.currentThread().getStackTrace()[1].getMethodName());
        driver.quit();
    }
}
