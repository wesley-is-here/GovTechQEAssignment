package com.pages;

import org.apache.commons.io.FileUtils;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.openqa.selenium.*;
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
    // Declare variables
    protected WebDriver driver;
    protected WebDriverWait wait;
    public JavascriptExecutor js;
    Logger log = LoggerFactory.getLogger(BasePage.class);
    private WebElement webElement;




    public BasePage(WebDriver driver, WebDriverWait wait) {
        // this keyword - used within a class to refer to its own instance variables
        // constructor initializes the state of the BasePage object by assigning the provided driver and wait objects to the corresponding instance variables
        this.driver = driver;
        this.wait = wait;
        // sets up a reference to a JavascriptExecutor instance for possible JavaScript execution
        // casting it to the JavascriptExecutor type - object casting
        this.js = (JavascriptExecutor) driver;
    }

    // Protected - used when you want the member to be accessible within the class and its subclasses
    protected void highlight(WebElement element) {
        // Execute JavaScript to highlight the element with an orange border
        js.executeScript("arguments[0].style.border='solid 2px orange'", element);
    }

    protected void highlight(List<WebElement> elements) {
        // Iterate through each WebElement in the list using a for-each loop
        for (WebElement ele : elements) {
            // Execute JavaScript to highlight the element with an orange border
            js.executeScript("arguments[0].style.border='solid 2px orange'", ele);
        }
    }


    // public - used when you want the member to be accessible from anywhere in the program
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
            // Capture a screenshot as a byte array using the TakesScreenshot interface
            byte[] img = (byte[]) ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            // Create a File object representing the file to save the screenshot
            File file = new File("Screenshots/" + filename + ".jpeg");
            // Save the screenshot file using the saveFile method
            saveFile(file, img);
        } catch (Exception e) {
            // Handle any exception that might occur during the screenshot capturing or saving process
            log.error(e.getMessage());
        }
    }

    public void saveFile(File file, byte[] img) {
        try {
            // Create an output stream to write the byte array to the specified file
            OutputStream os = new FileOutputStream(file);
            // Write the byte array to the output stream, effectively saving it to the file
            os.write(img);
            // Close the output stream to release system resources
            os.close();
        } catch (IOException e) {
            // Handle any IO exception that might occur during file saving
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
    // If a StaleElementReferenceException occurs, the method recursively calls itself to re-locate the element (Recursion)
    //  StaleElementReferenceException : WebDriver tries to interact with an element that has become invalid or no longer exists in the current state of the webpage
    public WebElement waitForVisibilityOfElement(By loc) {
        try {
            webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(loc));
            highlight(webElement);
            return webElement;
        } catch (StaleElementReferenceException e) {
            webElement = waitForVisibilityOfElement(loc);
            return webElement;
        } catch (Exception e) {
            // Re-throw the exception for further handling or logging in other methods
            throw e;
        }
    }

    // waits for an element specified by the locator to become visible within the given timeout period
    public WebElement waitForVisibilityOfElement(By loc, long timeoutInSec) {
        WebElement element;
        try {
            WebDriverWait wait = new WebDriverWait(driver, timeoutInSec);
            element = wait.until(ExpectedConditions.visibilityOfElementLocated(loc));
            highlight(element);
            return element;
        } catch (TimeoutException e) {
            element = findElement(loc);
            return element;
        } catch (Exception e) {
            throw e;
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
            throw e;
        }
    }



    // waits for an element specified by the locator to become invisible within the given timeout period
    public void waitForInVisibilityOfElement(By loc, long timeoutInSec) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, timeoutInSec);
            boolean isInvisible = wait.until(ExpectedConditions.invisibilityOf(findElement(loc)));
            Assert.assertEquals(isInvisible, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // presenceOfElementLocated checks if an element is present in the DOM, regardless of its visibility
    // use presence instead of visibility for uploadFile, also for clickUsingJS / JS Executor and check if video is playing, etc
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
            throw e;
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
            // unlike Thread.sleep(), it does not wait for the complete duration of time.
            // In case it finds the element before the duration specified, it moves on to the next line of code execution, thereby reducing the time of script execution.
            // cannot wait based on a specified condition like element selectable/clickable unlike explicit.
            // usually used when you are sure the element may be visible in a certain time
            driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
            return driver.findElement(loc);
        } catch (Exception e) {
            throw e;
        }
    }


    // method is used to locate and return a list of WebElement objects based on the provided locator (By object)
    public List<WebElement> findElements(By loc) {
        try {
            driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
            return driver.findElements(loc);
        } catch (Exception e) {
            throw e;
        }
    }


    public void waitForFileToDownload() {
        // Specify the download directory (By default, searches through root directory)
        File dir = new File(DEFAULT_DOWNLOAD_DIRECTORY);
        // Create an array to store the contents of the download directory
        File[] dirContents;
        // starts with a value of 0 to indicate that no files have been downloaded yet
        int size = 0;
        // set as false to indicate that there are no ongoing downloads
        boolean isDownloading = false;
        int cnt = 0;
        // While the file is downloading (size < 1) or continuously check [(cnt < 30) -> 30 tries)] 30 * 1 second of waiting = 30 seconds for file to finish download
        while ((size < 1) && (cnt < 30)) {
            // Wait for a short period to allow time for the file to download
            waitFor(1);
            // Get the contents of the download directory
            dirContents = dir.listFiles();
            // Check if any files with extensions indicating ongoing downloads are present
            if (dirContents.length >= 1) {
                // crdownload: This extension indicates that the file is being downloaded
                // tmp: This extension indicates a temporary file created during download
                isDownloading = Arrays.stream(dirContents).anyMatch(i -> (i.getName().contains(".crdownload") || i.getName().contains(".tmp")));  // download in progress - shows these 2

                // Log the downloading file
                log.info("File is downloading: " + dirContents[0]);
            }
            // Update the size based on whether the file is still downloading or the download is complete
            if (isDownloading) {
                size = 0;
            } else {
                size = dirContents.length; // if downloaded fully, size > 1 - to break the loop
            }
            // Increment the counter
            cnt++;
        }
        // Wait for a short period before further processing
        waitFor(1);
        // Log that the file has been downloaded successfully
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
        catch (Exception e) {
            throw e;
        }
    }

    public void clickIfDisplayed(By locator,long timeoutInSec) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, timeoutInSec);
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            highlight(element);
            element.click();
        }
        catch (ElementClickInterceptedException | StaleElementReferenceException e) {
            clickUsingJS(locator);
        }
        catch (Exception e) {
            log.info("Element not found.");
        }
    }

    // can also use 'clickAndHold' instead of 'dragAndDropBy'
    // slider.clickAndHold(Slider).moveByOffset(380,0).build().perform(); [find the pixel at halfway through the video, add in value (380px) if compared to 50% in video percentage]
    // slider.dragAndDropBy(Slider, percentage, 0).build().perform();
    // yOffset:0 (there is no vertical movement); percentage - is the xOffset
    // advantage of clickAndHold() - simulate a mouse click and hold action on a web element; Eg. Click on one file and hold and drop into another location
    // (emulating real user behavior on the web page, Precise control over timing)
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
        // Specify the download directory (By default, searches through root directory)
        File dir = new File(DEFAULT_DOWNLOAD_DIRECTORY);
        // Clear the contents of the directory
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
            log.info("waiting for page load...");
            wait.until(expectation);
        } catch (Exception e) {
            throw e;
        }
    }


    public void waitFor(double waitInSec) {
        try {
            if (waitInSec < 0 || waitInSec > 500) {
                // signal to the caller of a method that the argument provided is invalid based on certain criteria or constraints
                throw new IllegalArgumentException("Wait is specified is greater than 500 sec.");
            }
            log.info("waiting for " + (long) (waitInSec * 1000) + " sec...");
            Thread.sleep((long) (waitInSec * 1000));
        } catch (Exception e) {
            e.printStackTrace();
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
