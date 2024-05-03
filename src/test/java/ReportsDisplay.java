
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class ReportsDisplay {

    ExtentHtmlReporter htmlReporter;
    ExtentReports extent;
    ExtentTest test;
    WebDriver driver;
    @BeforeTest
    public void startReport() {
        System.setProperty("webdriver.chrome.driver","/Users/sukanya/IdeaProjects/FirstMac/src/main/resources/Driver/chromedriver");
        driver= new ChromeDriver();
        htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") +"/test-output/testReportAA.html");
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        //configuration items to change the look and feel
        htmlReporter.config().setChartVisibilityOnOpen(true);
        htmlReporter.config().setDocumentTitle("Simple Automation Report");
        htmlReporter.config().setReportName("Test Report on AA");
        htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
        htmlReporter.config().setTheme(Theme.STANDARD);
        htmlReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
    }

    @Test
    public void test_1() {
        driver.get("https://www.aa.com/homePage.do");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        WebElement element = driver.findElement(By.xpath("//span[contains(text(),'One way')]"));
        element.click();

        if(element.isDisplayed()){
            test = extent.createTest("One or two way", "PASSED test case");
            test.log(Status.PASS,"One way is selected");
            Assert.assertTrue(true);
        }
    }
    @Test
    public void test_2() {
        driver.get("https://www.aa.com/homePage.do");
        driver.manage().window().maximize();
        String title = driver.getTitle();
        test = extent.createTest("Check title", "Failed test case");
        test.log(Status.FAIL,"Wrong title");
        Assert.assertEquals(title,"American  Airlines");
    }
    @Test
    public void test_3() {
        test = extent.createTest("Test Case 3", "The test case 3 has been skipped");
        throw new SkipException("The test has been skipped");
    }
    @AfterMethod
    public void getResult(ITestResult result) {
        // take screenshot when it fails
        if(result.getStatus() == ITestResult.FAILURE) {
            test.log(Status.FAIL,result.getThrowable());
            {
                try
                {
                    TakesScreenshot ts=(TakesScreenshot)driver;
                    File source=ts.getScreenshotAs(OutputType.FILE);
                    String dest = "/Users/sukanya/IdeaProjects/FirstMac/src/test/java/Reports/Screenshots/"+result.getName()+".png";
                    File destination = new File(dest);
                    FileUtils.copyFile(source, destination);
                    System.out.println("Screenshot taken");
                test.log(Status.FAIL, "Snapshot below: " + test.addScreenCaptureFromPath(dest));
                }
                catch (Exception e)
                {
                    System.out.println("Exception while taking screenshot "+e.getMessage());
                }
            }
        }
        else if(result.getStatus() == ITestResult.SUCCESS) {
            test.log(Status.PASS, result.getTestName());
        }
        else {
            test.log(Status.SKIP, result.getTestName());
        }
    }
    @AfterTest
    public void tearDown() {
        extent.flush();
    }
}
//commit change to git
//add screenshot to the report
//send email of failure test cases
//testng xml file for parallel execution
