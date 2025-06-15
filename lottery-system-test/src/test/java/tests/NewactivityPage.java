package tests;

import common.Utils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class NewactivityPage extends Utils {
    public static String url = "http://152.136.139.196:8080/admin.html";
    public NewactivityPage() {
        super(url);
    }

    //新建失败
    public void newActivityFail(){
        //1.直接点击新建活动
        driver.findElement(By.cssSelector("#createActivity")).click();

        //等待iframe加载并切换
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("contentFrame"));
        //直接点击创建

        driver.findElement(By.cssSelector("#createActivity")).click();
        //查找错误提示
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("activityName-error")));

        //2.不圈选奖品人员
        driver.findElement(By.cssSelector("#activityName")).sendKeys("111");
        driver.findElement(By.cssSelector("#description")).sendKeys("测试");
        driver.findElement(By.cssSelector("#createActivity")).click();

        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        alert.accept(); // 处理弹窗

        driver.switchTo().defaultContent();
    }
}
