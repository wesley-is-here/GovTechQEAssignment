package com.pages;

import org.apache.commons.io.FileUtils;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class BasePage {



    // Set download directory
    private static final String DEFAULT_DOWNLOAD_DIRECTORY = "downloads";
    // Initialize driver
    protected WebDriver driver;
    protected WebDriverWait wait;
    Logger log = LoggerFactory.getLogger(BasePage.class);
    private WebElement webElement;

    public JavascriptExecutor js;



    public BasePage(WebDriver driver, WebDriverWait wait) {
        // this keyword - used within a class to refer to its own instance variables
        // constructor initializes the state of the BasePage object by assigning the provided driver and wait objects to the corresponding instance variables
        this.driver = driver;
        this.wait = wait;
        // sets up a JavascriptExecutor instance for possible JavaScript execution
        this.js = (JavascriptExecutor) driver;
    }


    protected void highlight(WebElement element) {
        js.executeScript("arguments[0].style.border='solid 2px orange'", element);
    }

    protected void highlight(List<WebElement> elements) {
        for (WebElement ele : elements) {
            js.executeScript("arguments[0].style.border='solid 2px orange'", ele);
        }
    }

    public String highlightAndGetText(WebElement element) {
        js.executeScript("arguments[0].style.border='solid 2px orange'", element);
        return element.getText().trim();
    }


    // method waits for the web element specified by the locator to become visible,
    // retrieves its visible text content, and returns it as a String.
    // If a StaleElementReferenceException occurs, the method re-locates the element and retrieves the updated text content
    public String getText(By element) {
        try {
            WebElement webElement = waitForVisibilityOfElement(element);
            return webElement.getText().trim();
        } catch (StaleElementReferenceException e) {
            webElement = waitForVisibilityOfElement(element);
            return webElement.getText().trim();
        }
    }


    protected void getURL(String url) {
        try {
            driver.manage().window().maximize();
            driver.get(url);
            this.waitForPageToLoad();
        } catch (TimeoutException e) {
            driver.get(url);
            waitForPageToLoad();
        }
    }

    public void maxWindow() {
        driver.manage().window().maximize();
    }

    public void setText(By locator, String Text) {
        waitForVisibilityOfElement(locator).sendKeys(Text);
    }


    public void addScreenshot(String filename) {
        try {
            byte[] img = (byte[]) ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            File file = new File("Screenshots/" + filename + ".jpeg");
            saveFile(file, img);
        } catch (Exception e) {
//            to print specific error
            log.error(e.getMessage());
        }
    }

    public void saveFile(File file, byte[] img) {
        try {
            OutputStream os = new FileOutputStream(file);
            os.write(img);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    protected void clearText(By locator) {
        WebElement element = waitForVisibilityOfElement(locator);
        element.clear();
        element.sendKeys(Keys.CONTROL + "a" + Keys.BACK_SPACE);
    }

    protected void selectDropDown(By locator, String visibleText) {
        WebElement element = waitForVisibilityOfElement(locator);
        Select select = new Select(element);
        select.selectByVisibleText(visibleText);
    }


    // method waits for an element specified by the locator to become visible.
    // Once the element becomes visible, the reference to the located element is returned.
    // If a StaleElementReferenceException occurs, the method recursively calls itself to re-locate the element
    public WebElement waitForVisibilityOfElement(By loc) {
        try {
            webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(loc));
            highlight(webElement);
            return webElement;
        } catch (StaleElementReferenceException e) {
            // Retry finding the element in case of stale reference exception
            return waitForVisibilityOfElement(loc);
        } catch (Exception e) {
            // Handle the exception, e.g., logging the error
            System.err.println("Exception occurred while waiting for element visibility: " + e.getMessage());
            // Perform any other necessary error handling
            // Return null or a default value indicating failure, depending on your requirements
            return null;
        }
    }


    // method waits for multiple elements specified by the locator to become visible.
  // Once all the elements become visible, the references to the located elements are returned as a list
    public List<WebElement> waitForVisibilityOfElements(By loc) {
        try {
            List<WebElement> elements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(loc));
            highlight(elements);
            return elements;
        } catch (Exception e) {
            // Handle the exception, e.g., logging the error
            System.err.println("Exception occurred while waiting for elements visibility: " + e.getMessage());
            // Perform any other necessary error handling
            // Return an empty list or null, depending on your requirements
            return Collections.emptyList();
        }
    }



    // waits for an element specified by the locator to become visible within the given timeout period
    public WebElement waitForVisibilityOfElement(By loc, long timeoutInSec) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, timeoutInSec);
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(loc));
            highlight(element);
            return element;
        } catch (TimeoutException e) {
            // Handle the timeout exception, e.g., logging the error
            System.err.println("Timeout occurred while waiting for element visibility: " + e.getMessage());
            // Perform any other necessary error handling
            // Return null or a default value indicating failure, depending on your requirements
            return null;
        } catch (Exception e) {
            // Handle any other exception, e.g., logging the error
            System.err.println("Exception occurred while waiting for element visibility: " + e.getMessage());
            // Perform any other necessary error handling
            // Return null or a default value indicating failure, depending on your requirements
            return null;
        }
    }


    // waits for an element specified by the locator to become invisible within the given timeout period
    public void waitForInVisibilityOfElement(By loc, long timeoutInSec) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, timeoutInSec);
            boolean isInvisible = wait.until(ExpectedConditions.invisibilityOf(findElement(loc)));
            Assert.assertEquals(isInvisible, true);
        } catch (TimeoutException e) {
            // Handle any other exception, e.g., logging the error
            System.err.println("Timeout occurred while waiting for element visibility: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Exception occurred while waiting for element visibility: " + e.getMessage());
        }
    }


    public WebElement waitForPresenceOfElement(By loc) {
        WebElement element;
        try {
            element = wait.until(ExpectedConditions.presenceOfElementLocated(loc));
            highlight(element);
            return element;
        } catch (TimeoutException e) {
            element = findElement(loc);
            return element;
        } catch (Exception e) {
            // Handle any other exception, e.g., logging the error
            System.err.println("Exception occurred while waiting for element visibility: " + e.getMessage());
            return null;
        }
    }


    public boolean isElementVisible(By loc, long timeoutInSec) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, timeoutInSec);
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(loc));
            highlight(element);
            // Return true to indicate that the element is visible
            return true;
        } catch (TimeoutException e) {
            // Catch a TimeoutException if the element does not become visible within the specified timeout
            // Return false to indicate that the element is not visible
            return false;
        }
    }


    // method attempts to find and return a WebElement based on the provided locator
    public WebElement findElement(By loc) {
        try {
            driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);
            return driver.findElement(loc);
        } catch (NoSuchElementException e) {
            System.err.println("Element not found: " + loc);
            return null;
        }
    }


    // method is used to locate and return a list of WebElement objects based on the provided locator (By object)
    public List<WebElement> findElements(By loc) {
        try {
            driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);
            return driver.findElements(loc);
        } catch (NoSuchElementException e) {
            System.err.println("No elements found: " + loc);
            return Collections.emptyList();
        }
    }



    protected void waitForFileToDownload() {
        File dir = new File(DEFAULT_DOWNLOAD_DIRECTORY);
        File[] dirContents;
        int size = 0;
        boolean isDownloading = false;
        int cnt = 0;
        while ((size < 1) && (cnt < 15)) {
            waitFor(1);
            dirContents = dir.listFiles();
            if (dirContents.length >= 1) {
                // crdownload : when file is downloading we have this extension
                // tmp : temporary file extension (when file is downloading)
                isDownloading = Arrays.stream(dirContents).anyMatch(i -> (i.getName().contains(".crdownload") || i.getName().contains(".tmp")));
                log.info("file is downloading .." + dirContents[0]);
            }
            if (isDownloading) {
                size = 0;
            } else {
                size = dirContents.length;
            }
            cnt++;
        }
        waitFor(2);
        log.info("File downloaded successfully.");
    }


    public String getAttribute(By locator, String attribute) {
        WebElement element = findElement(locator);
        return element.getAttribute(attribute);
    }

    public void click(By locator) {
        try {
            waitForVisibilityOfElement(locator).click();
        }
//        ElementClickInterceptedException : WebDriver is unable to perform the click if the element is blocked (some other element is intercepting it) and we try to click we get this exception
//        StaleElementReferenceException : WebDriver tries to interact with an element that has become invalid or no longer exists in the current state of the webpage
        catch (ElementClickInterceptedException | StaleElementReferenceException e) {
            clickUsingJS(locator);
        }
    }

    protected void moveSliderToPosition(By by, int percentage) {
        WebElement Slider = findElement(by);
        Actions slider = new Actions(driver);
        slider.dragAndDropBy(Slider, percentage, 0).build().perform();
    }

    public WebElement mouseHover(By by) {
        WebElement element = findElement(by);
        Actions action = new Actions(driver);
        action.moveToElement(element).build().perform();
        return element;
    }

    public WebElement mouseHover(WebElement element) {
        Actions action = new Actions(driver);
        action.moveToElement(element).build().perform();
        return element;
    }

    protected void clearDownloadDirectory() throws IOException {
        File dir = new File(DEFAULT_DOWNLOAD_DIRECTORY.toString());
        FileUtils.cleanDirectory(dir);
    }

    protected void retryClick(By loc) {
        waitForVisibilityOfElement(loc).click();
        waitFor(1);
        if (isElementVisible(loc, 6)) {
            click(loc);
        }
    }


    public void clickUsingJS(By loc) {
        js.executeScript("arguments[0].click();", waitForPresenceOfElement(loc));
    }

    protected void executeScript(String script) {
        js.executeScript(script);
    }

    protected boolean isVideoPlaying(By loc) {
        return (Boolean) js.executeScript("return arguments[0].paused;", waitForPresenceOfElement(loc));
    }


    public void waitForPageToLoad() {
        //  expectation is that the readyState of the document should be "complete" for the page to be considered fully loaded.
        //  This condition is commonly used to ensure that the page has finished loading before performing further interactions or assertions in Selenium tests
        // ExpectedCondition is a class that provides various conditions to wait for in order to synchronize tests with the application under test
        ExpectedCondition<Boolean> expectation = driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");
        try {
            log.info("Waiting for page load...");
            WebDriverWait wait = new WebDriverWait(driver, 10); // Add a timeout of 10 seconds
            wait.until(expectation);
            log.info("Page load completed.");
        } catch (TimeoutException e) {
            // Handle TimeoutException error
            log.error("Timeout occurred while waiting for page to load: " + e.getMessage());
        } catch (Exception e) {
            // Handle any other exception, e.g., logging the error
            log.error("An error occurred while waiting for page to load: " + e.getMessage());
        }
    }


    public void waitFor(double waitInSec) {
        try {
            if (waitInSec < 0 || waitInSec > 500) {
                throw new IllegalArgumentException("Wait time specified is invalid. Wait time should be between 0 and 500 seconds.");
            }
            log.info("Waiting for " + (long) (waitInSec * 1000) + " milliseconds...");
            Thread.sleep((long) (waitInSec * 1000));
        } catch (IllegalArgumentException e) {
            log.error("Invalid argument: " + e.getMessage());
            // Handle the exception, e.g., provide a default wait time or take appropriate action
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Thread sleep was interrupted: " + e.getMessage());
            // Handle the interrupted exception, e.g., re-interrupt the thread or take appropriate action
        } catch (Exception e) {
            log.error("An error occurred during the wait: " + e.getMessage());
            // Handle other exceptions, e.g., log the error or take appropriate action
        }
    }



    protected void switchToNewWindow(String title) {
        waitForPageToLoad();
        // If the current window has the desired title, return early without switching
        String parentWin = driver.getWindowHandle();
        if (driver.getTitle().equals(title)) {
            driver.switchTo().window(parentWin);
            return;
        }
        // Wait for a new window to open
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        // Retrieve all window handles
        Set<String> winids = driver.getWindowHandles();
        // Iterate through the window handles and check if any has the desired title
        for (String winid : winids) {
            // If the window has the desired title, return to switch to it
            if (getTitle(winid).equals(title)) {
                return;
            }
        }
    }


    public String getTitle(String windID) {
        driver.switchTo().window(windID);
        return driver.getTitle();
    }


}
