package common;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;

public class Utils {
    public static WebDriver driver;

    public static WebDriver createDriver(){
        if (driver == null){
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            //允许访问所有链接
            options.addArguments("--remote-allow-origins=*");

            driver = new ChromeDriver(options);

            //隐式等待
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        }
        return driver;
    }

    public Utils(String url){
        //调用driver对象
        driver = createDriver();

        //访问url
        driver.get(url);
    }

    public void getScreenShot(String str) throws IOException{
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
}
