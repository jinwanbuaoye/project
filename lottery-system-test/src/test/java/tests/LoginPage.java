package tests;

import common.Utils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;

public class LoginPage extends Utils {

    public static String url = "http://152.136.139.196:8080/blogin.html";
    public LoginPage() {
        super(url);
    }

    //查看页面成功正常加载
    public void loginPageRight(){
        //查看页面正常加载密码登录和验证码登录
        driver.findElement(By.cssSelector("body > div > div.login-container.col-sm-6.col-md-6.col-lg-5.col-xl-5 > div.tab-box > span.tab-span.active"));
        driver.findElement(By.cssSelector("body > div > div.login-container.col-sm-6.col-md-6.col-lg-5.col-xl-5 > div.tab-box > span:nth-child(2)"));

    }

    //登录成功
    public void loginSuc() throws IOException {

        //通过clear保证输入框没有文本信息
        driver.findElement(By.cssSelector("#phoneNumber")).clear();
        driver.findElement(By.cssSelector("#password")).clear();

        driver.findElement(By.cssSelector("#phoneNumber")).sendKeys("13122223333");
        driver.findElement(By.cssSelector("#password")).sendKeys("111111");
        driver.findElement(By.cssSelector("#loginForm > button")).click();

        //退出按钮--检查是否成功
        driver.findElement(By.cssSelector("body > div.header-box > div.user-box > div > span"));

        //检查页面标题
        String expect = driver.getTitle();
        assert expect.equals("后台管理");

        getScreenShot(Thread.currentThread().getStackTrace()[1].getMethodName());

        //回退
        driver.navigate().back();
    }

    //登录失败
    public void loginFail() throws IOException, InterruptedException {

        //通过clear保证输入框没有文本信息
        driver.findElement(By.cssSelector("#phoneNumber")).clear();
        driver.findElement(By.cssSelector("#password")).clear();

        //刷新保证没有文本
        driver.navigate().refresh();

        driver.findElement(By.cssSelector("#phoneNumber")).sendKeys("13122233333");
        driver.findElement(By.cssSelector("#password")).sendKeys("111111");
        driver.findElement(By.cssSelector("#loginForm > button")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        //Alert alert = driver.switchTo().alert();
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());

        //确认
        alert.accept();

        getScreenShot(Thread.currentThread().getStackTrace()[1].getMethodName());
    }
}
