import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.openqa.selenium.TakesScreenshot;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class TestMain {

    static WebDriver driver;
    static ExtentTest test;
    static ExtentReports report;
    static ExtentHtmlReporter htmlReporter;
    @BeforeTest
    public static void browser(){
        System.setProperty("webdriver.chrome.driver","/Users/sukanya/IdeaProjects/FirstMac/src/main/resources/Driver/chromedriver.exe");
        driver= new ChromeDriver();
        htmlReporter = new ExtentHtmlReporter("/Users/sukanya/IdeaProjects/FirstMac/target/test-output/reportResult.html");
        report=new ExtentReports();
        report.attachReporter(htmlReporter);
    }
    @Test(priority = 1)
    public void loginPage(){
        driver.get("https://www.aa.com/homePage.do");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        WebElement element = driver.findElement(By.xpath("//span[contains(text(),'One way')]"));
        element.click();

        if(element.isDisplayed()){
            test = report.createTest("One or two way", "PASSED test case");
            test.log(Status.PASS,"One way is selected");
        }else{
            test = report.createTest("One or two way", "Failed test case");
            test.log(Status.FAIL,"One way is not selected");
        }
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(2));
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//p[contains(text(),'Explore status benefits')]"))));
    }
    @Test(priority = 2)
    public void homePageCheck(){
        driver.get("https://www.aa.com/homePage.do");
        driver.manage().window().maximize();
        String title = driver.getTitle();
        test = report.createTest("Check title", "Failed test case");
        Assert.assertEquals(title,"American  Airlines");
        test.log(Status.FAIL,"Wrong title");
    }
    public static String capture(WebDriver driver,String screenShotName) throws IOException
    {
        TakesScreenshot ts = (TakesScreenshot)driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        String dest = System.getProperty("user.dir") +"\\ErrorScreenshots\\"+screenShotName+".png";
        File destination = new File(dest);
        FileUtils.copyFile(source, destination);
        return dest;
    }

    //capture screen shots of the failure testcases
    @AfterMethod
    public void tearDown(ITestResult result)
    {
        if(ITestResult.FAILURE==result.getStatus())
        {
            try
            {
                TakesScreenshot ts=(TakesScreenshot)driver;
                File source=ts.getScreenshotAs(OutputType.FILE);
                FileHandler.copy(source, new File("/Users/sukanya/IdeaProjects/FirstMac/src/test/java/Reports/Screenshots/"+result.getName()+".png"));
                System.out.println("Screenshot taken");
//                String screenShotPath = capture(driver, "screenShotName");
//                test.log(Status.FAIL, "Snapshot below: " + test.addScreenCaptureFromPath(screenShotPath));
            }
            catch (Exception e)
            {
                System.out.println("Exception while taking screenshot "+e.getMessage());
            }
        }
    }

//send email of failure test cases
//testng xml file for parallel execution
    @AfterTest
    public static void endOfTest(){
    report.flush();
    driver.quit();
    }
}
