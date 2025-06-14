import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.List;

public class FirstTest {

    WebDriver driver = null;

    void createDriver(){

        //1.打开浏览器  使用驱动
        WebDriverManager.chromedriver().setup();

        //增加浏览器配置：允许访问所有链接
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);

        //隐式等待5秒
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        //2.输入网址：https://www.sogou.com/
        driver.get("https://www.sogou.com/");
    }

    //测试浏览器搜索自动化测试
    void test1() throws InterruptedException {

        createDriver();

        Thread.sleep(3000);

        //3.找到输入框，并输入关键词，自动化测试
        driver.findElement(By.cssSelector("#query")).sendKeys("自动化测试");
        Thread.sleep(3000);

        //4.找到搜索按钮，并点击
        driver.findElement(By.cssSelector("#stb")).click();
        Thread.sleep(3000);

        //5.关闭浏览器
        driver.quit();
    }

    //元素定位
    void test2() throws InterruptedException {

        createDriver();

        //选择器-地理位置
        Thread.sleep(8000);
        driver.findElement(By.cssSelector("#cur-weather > span:nth-child(1)"));
        driver.findElement(By.xpath("//*[@id=\"cur-weather\"]/span[1]"));
        List<WebElement> ll = driver.findElements(By.cssSelector("#wrap > div.header > div.top-nav"));
        for (int i = 0; i < 1; i++) {
            System.out.println(ll.get(i).getText());
        }
                
        driver.quit();
    }

    //操作元素
    void test3(){
        createDriver();

        String bdtext = driver.findElement(By.xpath("//*[@id=\"mingyi\"]")).getText();
        WebElement ele = driver.findElement(By.xpath("//*[@id=\"mingyi\"]"));
        System.out.println("打印的内容是："+bdtext+ele.getText());

        String attribute = driver.findElement(By.xpath("//*[@id=\"stb\"]")).getAttribute("value");
        System.out.println("搜索按钮上面的文本是："+attribute);

        String title = driver.getTitle();
        String url = driver.getCurrentUrl();
        System.out.println(title+": "+url);

        driver.findElement(By.cssSelector("#query")).sendKeys("自动化测试");

        driver.findElement(By.cssSelector("#query")).clear();
        driver.findElement(By.cssSelector("#query")).sendKeys("非自动化测试");

        driver.findElement(By.cssSelector("#stb")).click();

        driver.quit();
    }

    //设置窗口大小
    void test4() throws IOException {
        createDriver();

        //窗口最小化
        driver.manage().window().minimize();

        //窗口最大化
        driver.manage().window().maximize();

        //全屏窗口
        driver.manage().window().fullscreen();

        //手动设置窗口大小
        driver.manage().window().setSize(new Dimension(1024, 768));

        test5(getClass().getName());

        driver.findElement(By.cssSelector("#stb")).click();

        test5(getClass().getName());

        driver.quit();
    }

    //屏幕截图
    void test5(String str) throws IOException {

//        //屏幕截图
//        File file = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
//        //放到指定位置
//        FileUtils.copyFile(file,new File("one.png"));

        SimpleDateFormat sim1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sim2 = new SimpleDateFormat("HHmmssSS");
        String dirTime = sim1.format(System.currentTimeMillis());
        String fileTime = sim2.format(System.currentTimeMillis());

        //生成的文件夹路径./src/test/autotest-2022-08-01/goodsbroser-20220801-214130.png

        String filename ="./src/test/autotest/"+ dirTime + "/" + str+"-"+fileTime+".png";
        File srcfile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        //把屏幕截图放到指定的路径下
        FileUtils.copyFile(srcfile,new File(filename));

    }

    //等待
    void test6() throws InterruptedException {
        createDriver();



        //找到输入框，并输入关键词，自动化测试
        driver.findElement(By.cssSelector("#query")).sendKeys("自动化测试");

        //找到搜索按钮，并点击
        driver.findElement(By.cssSelector("#stb")).click();

        driver.findElement(By.cssSelector("#upquery")).clear();

        //找到输入框，并输入关键词，自动化测试
        driver.findElement(By.cssSelector("#upquery")).sendKeys("自动化");

        //找到搜索按钮，并点击
        driver.findElement(By.cssSelector("#searchBtn")).click();

        //隐式等待1000毫秒
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(1000));


        Thread.sleep(3000);

        WebElement ele = driver.findElement(By.cssSelector("#sogou_vr_30010462_title_0 > h3 > span"));
        System.out.println("txt:"+ele.getText());

        driver.findElement(By.cssSelector("#sogou_baike"));

        //5.关闭浏览器
        driver.quit();
    }
}
