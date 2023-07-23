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
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
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


    @Before
    public void openBrowser() {
        // Set the desired browser (can be "chrome" or "firefox")
        String browser = "firefox";

        switch (browser.toLowerCase()) {
            case "chrome":
                // Setup ChromeDriver using WebDriverManager
                WebDriverManager.chromedriver().setup();

                // Set the download file path for Chrome
                String downloadFilepath = System.getProperty("user.dir") + File.separator + "downloads";
                HashMap<String, Object> chromePrefs = new HashMap<>();
                chromePrefs.put("profile.default_content_settings.popups", 0);
                chromePrefs.put("download.default_directory", downloadFilepath);

                // Create ChromeOptions and configure preferences
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setExperimentalOption("prefs", chromePrefs);

                // Initialize the ChromeDriver with the configured options
                driver = new ChromeDriver(chromeOptions);
                break;
            case "firefox":
                // Setup FirefoxDriver using WebDriverManager
                WebDriverManager.firefoxdriver().setup();

                // Set the download file path for Firefox
                String downloadFilePath = System.getProperty("user.dir") + File.separator + "downloads";

                // Create FirefoxOptions and configure preferences
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addPreference("browser.download.folderList", 2);
                firefoxOptions.addPreference("browser.download.dir", downloadFilePath);
                firefoxOptions.addPreference("browser.helperApps.neverAsk.saveToDisk", "application/octet-stream");

                // Initialize the FirefoxDriver with the configured options
                driver = new FirefoxDriver(firefoxOptions);
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        // Maximize the browser window
        driver.manage().window().maximize();

        // Set the implicit wait timeout (4 seconds) - applies to all subsequent findElement or findElements operations
        driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);

        // Create WebDriverWait with explicit wait timeout (30 seconds)
        wait = new WebDriverWait(driver, 30);
    }


    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            logger.info("Driver closing.");
        }
    }


}