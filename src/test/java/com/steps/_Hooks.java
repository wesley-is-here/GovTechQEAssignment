package com.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class _Hooks {

    public static WebDriver driver;
    public static WebDriverWait wait;
    Logger logger = LoggerFactory.getLogger(_Hooks.class);


    @Before()
    public void openBrowser() {
        logger.info("Driver Setting Up.");
        WebDriverManager.chromedriver().setup();

//      this is the code to configure download file path in chrome
        // This line sets the download file path to a directory named "downloads" located in the current working directory of the project.
        // The System.getProperty("user.dir") retrieves the current working directory, and File.separator provides the platform-specific file separator (e.g., "/" on Unix-like systems and "" on Windows).
        String downloadFilepath = System.getProperty("user.dir")+File.separator+"downloads";
        // HashMap named chromePrefs is created to store the Chrome browser preferences. The key-value pairs in the map define the specific preferences.
        //"profile.default_content_settings.popups", 0 sets the default behavior for pop-up windows to be blocked.
        //"download.default_directory", downloadFilepath specifies the default directory where downloaded files will be saved. It uses the downloadFilepath variable that we defined.
        HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("download.default_directory", downloadFilepath);
        // ChromeOptions object named options1 is created to set additional browser options.
        ChromeOptions options1 = new ChromeOptions();
        // setExperimentalOption() method is used to set the "prefs" experimental option with the chromePrefs map.
        options1.setExperimentalOption("prefs", chromePrefs);

        // initialize driver
        driver = new ChromeDriver(options1);
//        explicit wait (wait till condition meet , max 30 secs)
        wait = new WebDriverWait(driver,30);

//        implicit wait (fix time wait of 2 seconds)
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
    }


    @After()
    public void closeDriver() {
        logger.info("Driver closing.");
        driver.quit();
    }


}