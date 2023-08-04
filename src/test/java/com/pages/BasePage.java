package com.pages;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
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

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BasePage {



    // Set download directory
    private static final String DEFAULT_DOWNLOAD_DIRECTORY = "downloads";
    // Declare variables
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected JavascriptExecutor js;
    Logger log = LoggerFactory.getLogger(BasePage.class);
    private WebElement webElement;




    protected BasePage(WebDriver driver, WebDriverWait wait) {
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
    protected String getText(By element) {
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
            driver.get(url);
            waitForPageToLoad();
        } catch (TimeoutException e) {
            driver.get(url);
            waitForPageToLoad();
        }
    }

    protected void maxWindow() {
        driver.manage().window().maximize();
    }

    protected void setText(By locator, String Text) {
        clearText(locator);
        waitForVisibilityOfElement(locator).sendKeys(Text);
    }


    // Method to scroll to a specific element
    protected void scrollToElement(By locator) {
        try {
            WebElement element = findElement(locator);
            js.executeScript("arguments[0].scrollIntoView(true);", element);
        } catch (NoSuchElementException | TimeoutException e) {
            throw new NoSuchElementException("Unable to scroll to element: " + locator);
        }
    }

    // Method to load SRT File and return SRTContent
    protected String getSRTContent(String srtFile) throws FileNotFoundException {
        String srtContent = "";
        try {
        // Load the SRT file
        File file = new File(srtFile);
        BufferedReader reader = new BufferedReader(new FileReader(file));

        String line;
        // Creates a new instance of the StringBuilder class named contentBuilder.
        //StringBuilder is a mutable sequence of characters that is used to build the content of the SRT file line by line
        StringBuilder contentBuilder = new StringBuilder();
        // Enters a loop that continues until there are no more lines to read from the BufferedReader
        while ((line = reader.readLine()) != null) {
            // appends the line to the contentBuilder
            contentBuilder.append(line);
            // appends a platform-specific line separator (e.g., newline character) to the contentBuilder after each line.
            contentBuilder.append(System.lineSeparator());
        }
        // close the BufferedReader after reading the lines from the SRT file to free up system resources and prevent memory leaks
        reader.close();

        // Get the content of the SRT file
        srtContent = contentBuilder.toString();

    }
        catch (Exception e){
            e.printStackTrace();
            throw new FileNotFoundException("Error reading file.");
        }

        return srtContent;
    }




    // Method to check SRT content format using a string
    protected void checkSRTString(String srtFile) throws IOException {

        // load SRT File
        String srtContent = getSRTContent(srtFile);

        // Regular expression pattern for matching subtitle entries
        String subtitleEntryPattern = "\\n(\\d+\\n\\d{2}:\\d{2}:\\d{2},\\d{3}\\s*-->\\s*\\d{2}:\\d{2}:\\d{2},\\d{3}\\n.*)";

        // Match subtitle entries using regex
        Pattern pattern = Pattern.compile(subtitleEntryPattern, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(srtContent);

        boolean validSRTFormat = true;

        while (matcher.find()) {
            String subtitleEntry = matcher.group(1);

            // Split the subtitle entry into index, time range, and subtitle text
            String[] subtitleParts = subtitleEntry.split("\\n");
            if (subtitleParts.length >= 3) {
                String index = subtitleParts[0];
                String timeRange = subtitleParts[1];
                String subtitleText = subtitleParts[2];

                // Validate the extracted subtitle elements

                // Example validation: Check if the subtitle index is a sequential number
                int expectedIndex = Integer.parseInt(index);
                if (expectedIndex < 1) {
                    System.out.println("Invalid subtitle index: " + expectedIndex);
                    validSRTFormat = false;
                    // Perform appropriate error handling or assertion failure
                }

                // Example validation: Check if the time range is in the correct format
                String timeFormatPattern = "\\d{2}:\\d{2}:\\d{2},\\d{3}";
                if (!timeRange.matches(timeFormatPattern)) {
                    System.out.println("Invalid time range format: " + timeRange);
                    validSRTFormat = false;
                    // Perform appropriate error handling or assertion failure
                }

                // Example validation: Check if the subtitle text is not empty
                if (subtitleText.isEmpty()) {
                    System.out.println("Subtitle text is empty.");
                    validSRTFormat = false;
                    // Perform appropriate error handling or assertion failure
                }
            } else {
                System.out.println("Invalid subtitle entry: " + subtitleEntry);
                validSRTFormat = false;
                // Perform appropriate error handling or assertion failure
            }
        }

        if (validSRTFormat) {
            System.out.println("Valid SRT Format, no issues!");
        }
    }


     // Verify if the content of an SRT file contains the expected text.
    protected boolean verifySRTContent(String srtFile, String expectedText) {
        try{
            // load SRT File
            String srtContent = getSRTContent(srtFile);

            // Convert expectedText to same syntax
            String expectedTextFinal = expectedText.replace("\\n", System.lineSeparator());

            log.info(srtContent);
            log.info(expectedTextFinal);

            // Perform assertions or checks on the extracted content
            return srtContent.contains(expectedTextFinal);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // PDF Checking Content Method
    protected String PDFVerify(String filePath, int pageNoStart) {

        // PDFTextStripper from PDFBox Dependency
        //read full pdf text:
            String extractedText = "";
            try (PDDocument document = PDDocument.load(new File(filePath))) {
                PDFTextStripper stripper = new PDFTextStripper();

                stripper.setStartPage(pageNoStart);
                extractedText = stripper.getText(document);

                // Print out Meta data of Document
                log.info("------meta data of pdf-------");
                System.out.println("Version Name:" + document.getVersion());
                System.out.println("Can this PDF document be printed:" + document.getCurrentAccessPermission().canPrint());
                System.out.println("Can this PDF document be read:" + document.getCurrentAccessPermission().isReadOnly());

                System.out.println("Number of Pages:" + document.getPages().getCount());
                System.out.println("PDF Subject:" + document.getDocumentInformation().getSubject());
                System.out.println("PDF Title:" + document.getDocumentInformation().getTitle());
                System.out.println("PDF Creator:" + document.getDocumentInformation().getCreator());
                System.out.println("PDF Creation Date:" + document.getDocumentInformation().getCreationDate());

            } catch (IOException e) {
                e.printStackTrace();
            }
            return extractedText;
        }









    protected void addScreenshot(String filename) {
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

    protected void saveFile(File file, byte[] img) {
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
    // If a StaleElementReferenceException occurs, the method recursively calls itself to re-locate the element (Recursion) - the element should become valid again if it reappears in the DOM
    //  StaleElementReferenceException : WebDriver tries to interact with an element that has become invalid or no longer exists in the current state of the webpage
    // In most cases, when you make another attempt to find or interact with the element, it should succeed because the webpage has settled into a new state, and the element has become valid again. The exception was triggered during the transition period when the webpage was changing.
    protected WebElement waitForVisibilityOfElement(By loc) {
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
    protected WebElement waitForVisibilityOfElement(By loc, long timeoutInSec) {
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
    protected List<WebElement> waitForVisibilityOfElements(By loc) {
        try {
            List<WebElement> elements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(loc));
            highlight(elements);
            return elements;
        } catch (Exception e) {
            throw e;
        }
    }



    // waits for an element specified by the locator to become invisible within the given timeout period
    protected void waitForInVisibilityOfElement(By loc, long timeoutInSec) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, timeoutInSec);
            boolean isInvisible = wait.until(ExpectedConditions.invisibilityOf(findElement(loc)));
            Assert.assertEquals(isInvisible, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // presenceOfElementLocated checks if an element is present in the DOM, regardless of its visibility
    // use presence instead of visibility, anything regarding javascript, eg. clickUsingJS / JS Executor and check if video is playing, etc
    protected WebElement waitForPresenceOfElement(By loc) {
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

// element displayed within a duration
    protected boolean isElementVisible(By loc, long timeoutInSec) {
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
    protected WebElement findElement(By loc) {
        try {
            // unlike Thread.sleep(), it does not wait for the complete duration of time.
            // In case it finds the element before the duration specified, it moves on to the next line of code execution, thereby reducing the time of script execution.
            // cannot wait based on a specified condition like element selectable/clickable unlike explicit.
            // usually used when you are sure the element may be visible in a certain time (used for simple method like, findElement)
            return driver.findElement(loc);
        } catch (Exception e) {
            throw e;
        }
    }


    // method is used to locate and return a list of WebElement objects based on the provided locator (By object)
    protected List<WebElement> findElements(By loc) {
        try {
            return driver.findElements(loc);
        } catch (Exception e) {
            throw e;
        }
    }


    protected void waitForFileToDownload() {
        // Specify the download directory (By default, searches through root directory)
        File dir = new File(DEFAULT_DOWNLOAD_DIRECTORY);
        // Create an array to store the contents of the download directory
        File[] dirContents;
        // starts with a value of 0 to indicate that no files have been downloaded yet
        int size = 0;
        // set as false to indicate that there are no ongoing downloads
        boolean isDownloading = false;
        int cnt = 0;
        // While the file is downloading (size < 1) and continuously check [(cnt < 30) -> 30 tries)] 30 * 1 second of waiting = 30 seconds for file to finish download
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
                size = dirContents.length; // if downloaded fully, size > 0 [1 or more] - to break the loop
            }
            // Increment the counter
            cnt++;
        }
        // Wait for a short period before further processing
        waitFor(1);
        // Log that the file has been downloaded successfully
        log.info("File downloaded successfully.");
    }



    protected String getAttribute(By locator, String attribute) {
        WebElement element = findElement(locator);
        return element.getAttribute(attribute);
    }


    // Click for element located (If not will crash)
    protected void click(By locator) {
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

    // Used for Intermittent PopUp / Display of certain elements (Click - Within a specified time frame) - except won't crash (it will log - "Element not found.")
    protected void clickIfDisplayed(By locator,long timeoutInSec) {
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

    // Method to check if a WebElement is displayed
    protected boolean isElementDisplayed(By locator) {
        try {
            WebElement element = findElement(locator);
            return element.isDisplayed();
        } catch (NoSuchElementException | TimeoutException e) {
            return false;
        }
    }

    // can also use 'clickAndHold' instead of 'dragAndDropBy'
    // slider.clickAndHold(Slider).moveByOffset(380,0).build().perform(); [find the pixel at halfway through the video, add in value (380px) if compared to 50% in video percentage]
    // slider.dragAndDropBy(Slider, percentage, 0).build().perform();
    // yOffset:0 (there is no vertical movement); percentage - is the xOffset
    // advantage of clickAndHold() - simulate a mouse click and hold action on a web element; Eg. Click on one file and hold and drop into another location
    // (emulating real user behavior on the web page, Precise control over timing)
    protected void moveSliderToPosition(By by, int percentage) {
        WebElement slider = findElement(by);
        // Check if slider element is displayed
        if (!isElementDisplayed(by)) {
            throw new NoSuchElementException("Slider element is not displayed.");
        }
        try {
            Actions actions = new Actions(driver);
            actions.dragAndDropBy(slider, percentage, 0).build().perform();
        }
        catch (ElementNotInteractableException e) {
            throw new ElementNotInteractableException("Unable to move slider element.");
        }
    }

    // Method to perform a mouse hover action (By Type)
    protected void mouseHover(By locator) {
        try {
            WebElement element = findElement(locator);
            Actions actions = new Actions(driver);
            actions.moveToElement(element).build().perform();
        } catch (NoSuchElementException | TimeoutException e) {
            throw new NoSuchElementException("Unable to perform hover action on element: " + locator);
        }
    }


    // Method to perform a mouse hover action (WebElement Type)
    protected void mouseHover(WebElement element) {
        try {
            Actions actions = new Actions(driver);
            actions.moveToElement(element).build().perform();
        } catch (NoSuchElementException | TimeoutException e) {
            throw new NoSuchElementException("Unable to perform hover action on element: " + element);
        }
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


    protected void clickUsingJS(By loc) {
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

    // Method to navigate back to the previous page
    protected void navigateBack() {
        try {
            driver.navigate().back();
        } catch (WebDriverException e) {
            throw new WebDriverException("Unable to navigate back: " + e.getMessage());
        }
    }

    // Method to refresh the current page
    protected void refreshPage() {
        try {
            driver.navigate().refresh();
        } catch (WebDriverException e) {
            throw new WebDriverException("Unable to refresh page: " + e.getMessage());
        }
    }

    protected void waitFor(double waitInSec) {
        try {
            if (waitInSec < 0 || waitInSec > 500) {
                // signal to the caller of a method that the argument provided is invalid based on certain criteria or constraints
                throw new IllegalArgumentException("Wait is specified is greater than 500 sec.");
            }
            log.info("waiting for " + waitInSec + " sec...");
            // Thread.sleep() method expects the argument to be of type long
            // Thread.sleep(), which expects a long value, the code uses (long) to explicitly cast the result to a long data type.
            Thread.sleep((long) (waitInSec * 1000));  // Thread.sleep used milliseconds by default
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
        // Wait for a new window to open (total 2 Windows)
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


    // Method to switch to a specific tab by index
    protected void switchToTab(int tabIndex) {
        try {
            String originalTab = driver.getWindowHandle();
            // Convert set to list
            List<String> mySet = new ArrayList<>(driver.getWindowHandles());
            driver.switchTo().window(mySet.get(tabIndex));
        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException("Invalid tab index provided.");
        }
    }

    // Method to close the current tab and switch back to the original tab
    protected void closeCurrentTabAndSwitchBack() {
        String currentTab = driver.getWindowHandle();
        driver.close();
        driver.switchTo().window(currentTab);
    }


    // Method to check if an element is enabled
    protected boolean isElementEnabled(By locator) {
        try {
            WebElement element = findElement(locator);
            return element.isEnabled();
        } catch (Exception e) {
            // Handle any exception that occurs while finding the element
            return false;
        }
    }

    // Method to check if an element is clickable
    protected boolean isElementClickable(By locator) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(locator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Method to check and verify Table is sorted both Ascending and Descending order
    protected void verifyTableSorting(By table,String sortingType,String headerName){
        ArrayList<String> obtainedList = new ArrayList<>();
        ArrayList<String> headers = new ArrayList<>();
        List<WebElement> headerList=driver.findElement(table).findElements(By.xpath(".//th"));


        for(WebElement we:headerList){
            headers.add(we.getText());
        }

        int pos = headers.indexOf(headerName)+1;
        List<WebElement> elementList= findElement(table).findElements(By.xpath("./tbody/tr/td["+pos+"]"));
        for(WebElement we:elementList){
            obtainedList.add(we.getText());
        }
        ArrayList<String> sortedList = new ArrayList<>(obtainedList);
        // Sort the list in ascending order
        Collections.sort(sortedList);
        if(sortingType.equalsIgnoreCase("asc")){
            log.info("This is sorted in a ascending order.");
        }
        else if(sortingType.equalsIgnoreCase("desc")) {
            // If sortingType is "desc", reverse the list to get it in descending order
            Collections.reverse(sortedList);
            log.info("This is sorted in an desscending order.");
        }
        Assert.assertEquals(sortedList,obtainedList);
    }


    protected String getTitle(String windID) {
        driver.switchTo().window(windID);
        return driver.getTitle();
    }

    protected void switchToIframe(By locator) {
        driver.switchTo().frame(waitForPresenceOfElement(locator));
    }


}
