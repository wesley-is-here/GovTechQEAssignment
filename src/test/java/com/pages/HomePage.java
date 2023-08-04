package com.pages;

import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.File;
import java.io.IOException;
import java.util.List;


public class HomePage extends BasePage {

    Logger logger = LoggerFactory.getLogger(HomePage.class);


    public HomePage(WebDriver driver, WebDriverWait wait) {
        // super: It is a keyword used to access or invoke members (methods or variables) of the parent class
        // code assigns the driver and wait parameters to the respective member variables in the HomePage class (this.driver and this.wait)
        super(driver, wait);
    }

    // This is for this assignment (private - limit access to this class only)
    private By PMA = By.cssSelector("#viLoginOutlook > span");
    private By mSoftEmail = By.xpath("//input[@type='email']");
    private By mSoftPassword = By.xpath("//input[@type='password']");
    private By mSoftNextButton = By.xpath("//input[@type='submit']");
    private By mSoftSignInButton = By.xpath("//input[@type='submit']");
    private By yesButton = By.xpath("//input[@type='submit']");
    private By uploadButton = By.cssSelector("#galleryUploadtabs");
    private By fileUpload = By.xpath("//input[@type='file']");
    private By checkBox = By.cssSelector("span.checkbox-text");
    private By btnUploadIndex = By.xpath("//span[text()='Upload + index']");
    private By uploadText = By.xpath("//span[text()='1 file uploaded']");
    private By closeButton = By.cssSelector("#close > span");
    private By txtIndex = By.xpath("//span[normalize-space()='Indexing...']");
    private By btnDownloadInsights = By.cssSelector("#download-insights");

    private By btnDownloadPopUp = By.cssSelector("#download > span");
    private By btnCancelPopUp = By.cssSelector("#cancel > span");

    private By tabTimeline = By.xpath("//button[@title='Timeline']");

    private By fullTranscribedText = By.id("appViInsightsTimeline");
    private By dropDownPrivacy = By.xpath("(//*[@id='indexingPrivacy']/p-dropdown//span/i)[1]");
    private By dropDownIndexLanguage = By.xpath("(//*[@id='indexingLanguage']/p-dropdown//span/i)[1]");
    private By searchBox = By.id("viFilterSearchInput");

    private By searchIcon = By.xpath("//i[@title='Search']");

    private By searchFilter = By.xpath("//button[@title='Filters']");

    private By searchInDropDownArrow = By.xpath("//*[@id='scope-filter-id']//i[@role='presentation']");

    private By insightTypeDropDownArrow = By.xpath("//*[@title='Insight type']//i[@role='presentation']");

    private By txtPresenceIndex = By.xpath("//*[contains(text(),'Indexed')]");

    private By viewDropDown = By.xpath("//*[@id='gallery-view-menu']/p-dropdown//i[@role='presentation']");

    private By listView = By.id("list-view");

    private By tiledView = By.id("grid-view");

    private By errorMsg = By.id("passwordError");

    private By btnPopUpClose = By.cssSelector("div > div > span > div.notification-title > i.vi-delete-parent.i-vi-close-big");

    private By tblCustomer = By.xpath("//*[@id='table1']");


    public void launchURL(String url) {
        getURL(url);
    }

    public void clickPMA() {
        click(PMA);
    }

    public void enterMsoftEmail(String email) {
        setText(mSoftEmail, email);
    }

    public void enterMsoftPassword(String password) {
        setText(mSoftPassword, password);
    }

    public void nextMsoftButton() {
        click(mSoftNextButton);
    }

    public void signInMsoftButton() {
        click(mSoftSignInButton);
    }

    public void yesButtonConfirm() {
        waitFor(1);
        click(yesButton);
    }

    public void uploadButtonClick() {
        waitForPageToLoad();
        waitFor(5);
        click(uploadButton);
    }


    public void uploadFile(String filename) {
    // Path from Content Root: resources/VideoUpload/Video_2.mp4
    // System.getProperty("user.dir") - used to retrieve the current working directory of the user.
    // working directory refers to the directory in the file system from which the Java application was launched or is currently executing. It represents the starting point for resolving relative file paths
    // File.separator constant is used to represent the platform-specific file separator character.
    // File.separator is used instead of hardcoding a specific character like / or \ is to ensure platform independence and portability of code. Different operating systems use different characters as file separators.
    // Windows uses backslash \ (e.g., C:\folder\file.txt), while Unix-like systems (including Linux and macOS) use forward slash /
        String file = System.getProperty("user.dir") + File.separator + "resources" + File.separator + "VideoUpload" + File.separator + filename;
        waitForPresenceOfElement(fileUpload).sendKeys(file);
        waitFor(1);
    }

    public void selectTermsAndConditions() {
        waitForVisibilityOfElement(checkBox);
        click(checkBox);
        waitFor(1);
    }

    public void clickOnUploadIndexButton() {
        click(btnUploadIndex);
        waitFor(2);
    }

    public void selectVideoDropDownPrivacy(String value) {
        click(dropDownPrivacy);
        By val = By.xpath("//span[normalize-space()='" + value + "']");
        click(val);
        waitFor(1);
    }

    public void selectVideoDropDownVSL(String value) {
        click(dropDownIndexLanguage);
        By val = By.xpath("//span[normalize-space()='" + value + "']");
        click(val);
        waitFor(1);
    }

    public void waitForFileToUpload() {
        // wait for text with 150 seconds time limit
        waitForVisibilityOfElement(uploadText, 150);
    }

    public void closeButton() {
        click(closeButton);
    }

    public void waitForIndexComplete() {
        waitFor(2);
        waitForInVisibilityOfElement(txtIndex, 500);
    }

    public String videoVisible(String text) {
        By videoName = By.xpath("//span[normalize-space()='" + text + "' and @title='" + text + "']");
        String name = getText(videoName);
        logger.info(name);
        return name;
    }

    public void videoClick(String text) throws IOException {
        By videoName = By.xpath("//span[normalize-space()='" + text + "' and @title='" + text + "']");
        click(videoName);
        waitForPageToLoad();
        clearDownloadDirectory();
        waitFor(10);
    }

    public void videoPlayerVisible() {
        By videoPlayer = By.xpath("//video");
        waitForVisibilityOfElement(videoPlayer, 15);
        logger.info("Video Player is visible.");
    }



    // playback will occur once if the video is playing initially
    public void checkVideoPlaying() {
        By videoElement = By.xpath("//video");
        // Check if the video is currently playing
        Boolean isPlaying = isVideoPlaying(videoElement);

        if (isPlaying) {
            // Video is still playing
            logger.info("The video is playing.");
        } else {
            // Video is paused or not playing
            logger.info("The video is paused or not playing.");

            // Perform playback action (e.g., click the play button)
            // (Click once to repeat - allowing playback of the current selected video)
            By playButton = By.xpath("//*[@title='Play']");
            click(playButton);
            // Recheck if the video is playing after initiating playback
            isPlaying = isVideoPlaying(videoElement);


            // Continuously check if the video starts playing [!isPlaying = Not Playing]
            while (!isPlaying) {
                waitFor(1); // Wait for 1 second
                // Verify that the video is now playing
                isPlaying = isVideoPlaying(videoElement);
                waitFor(1);
            }

            logger.info("The video playback has started.");
        }
    }

    public void clickDownloadInsights() {
        click(btnDownloadInsights);
        waitFor(1);
    }

    public void clickDownloadCompletedInsights(String insight) {
        waitFor(1);
        By insightDownload = By.xpath("//*[@id='submenu-download-insights']/li[@title='" + insight + "']");
        click(insightDownload);
        waitForFileToDownload();
    }


    public void seeSegment(String text) {
        By segment = By.xpath("//*[contains(@title,'" + text + "')]");
        waitForVisibilityOfElement(segment);
    }

    public void selectVideoTimelineSlider(String type, int num) {
        switch (type) {
            case "Audio":
                By insightsBar = By.cssSelector("#acousticEventsComponent > div.row.timeline.ng-star-inserted > div > app-vi-insights-horizontal-timeline > div.horizontal-timeline > svg > rect.transition.bar");
                moveSliderToPosition(insightsBar, num);
                break;
            case "Keywords":
                insightsBar = By.cssSelector("#keywordsComponent > div.row.timeline.ng-star-inserted > div > app-vi-insights-horizontal-timeline > div.horizontal-timeline > svg > rect.transition.bar");
                moveSliderToPosition(insightsBar, num);
                break;
            case "Labels":
                insightsBar = By.cssSelector("#labelsComponent > div.row.timeline.ng-star-inserted > div > app-vi-insights-horizontal-timeline > div.horizontal-timeline > svg > rect.transition.bar");
                moveSliderToPosition(insightsBar, num);
                break;
            case "Entities":
                insightsBar = By.cssSelector("#mentionedEntitiesComponent > div.row.timeline.ng-star-inserted > div > app-vi-insights-horizontal-timeline > div.horizontal-timeline > svg > rect.transition.bar");
                moveSliderToPosition(insightsBar, num);
                break;
            default:
                throw new RuntimeException("insight " + type + " not found.");
        }
        waitFor(2);
    }

    public void clickDownloadBtn() {
        click(btnDownloadPopUp);
        waitForFileToDownload();
    }

    public void clickCancelBtn() {
        click(btnCancelPopUp);
        waitFor(1);
    }


    public void clickTimeline() {
        click(tabTimeline);
        waitFor(1);
    }

    public void transcribedText() {
        String transcribedText = getText(fullTranscribedText);
        waitFor(1);
        logger.info(transcribedText);
        waitFor(1);
    }

    public void enterTextSearchBox(String text) {
        setText(searchBox, text);
        waitFor(1);
    }

    public void clickSearch() {
        click(searchIcon);
        waitFor(5);
    }

    public void clickFilter() {
        click(searchFilter);
    }

    public void searchInDropdown(String text) {
        click(searchInDropDownArrow);
        waitFor(1);
        By val = By.xpath("//span[normalize-space()='" + text + "']");
        click(val);
        waitFor(1);
    }

    public void insightTypeDropdown(String text) {
        click(insightTypeDropDownArrow);
        waitFor(1);
        By val = By.xpath("//span[normalize-space()='" + text + "']");
        click(val);
        waitFor(1);
    }

    public void checkIndexes() {
        // Wait for the visibility of elements identified by txtPresenceIndex locator
        List<WebElement> elements = waitForVisibilityOfElements(txtPresenceIndex);
        // Log the number of indexed videos
        logger.info("There are " + elements.size() + " indexed videos.");
    }


    public void hoverVideoTiles() {
        // Wait for the visibility of elements identified by txtPresenceIndex locator
        List<WebElement> elements = waitForVisibilityOfElements(txtPresenceIndex);
        // Iterate through each WebElement in the list using a for-each loop
        for (WebElement ele : elements) {
            // Perform a mouse hover action on each element
            mouseHover(ele);
            waitFor(1.2);
        }
    }


    public void viewDropDown() {
        click(viewDropDown);
        waitFor(1);
    }

    public void selectLstView() {
        click(listView);
        waitFor(2);
    }

    public void selectTledView() {
        click(tiledView);
        waitFor(2);
    }


    public void switchesTab(String Tab) {
        logger.info("Switch Tab");
        switchToNewWindow(Tab);
    }


    public String verifyErrorMessage() {
        waitFor(2);
        String wholeError = getText(errorMsg);
        logger.info(wholeError);
        return wholeError;
    }

    public void clickOrWaitForPopupGone() {
        clickIfDisplayed(btnPopUpClose,  7);
    }

    public void verifyContent(String expectedText) throws IOException {
        String SRTFilePath = "downloads/Video_3.srt";
        checkSRTString(SRTFilePath);
        boolean isContentValid = verifySRTContent(SRTFilePath, expectedText);
        if (isContentValid) {
            logger.info("SRT content contains the expected text.");
        } else {
            logger.info("SRT content does not contain the expected text.");
        }
    }


    public void readPDFContent(String pdf, String text, int pageNoStart) {
        String PDFtext = PDFVerify(pdf, pageNoStart);
        logger.info(PDFtext);
        Assert.assertTrue(PDFtext.contains(text));
    }


    public void checkElementEnabledAndClickable(String element) {
        By locator;

        switch (element) {
            case "Upload":
                locator = uploadButton;
                break;
            default:
                throw new RuntimeException("Element " + element + " not found.");
        }

        boolean isEnabled = isElementEnabled(locator);
        boolean isClickable = isElementClickable(locator);

        // Check if the element is enabled
        if (isEnabled && isClickable) {
            logger.info("Yes! Element is Enabled & Clickable.");
        } else {
            logger.info("No! Element is not Enabled & Clickable.");
        }
    }

    public void clickonColHeader(String tableHeader) {
            By loc= By.xpath("//*[@id='table1']//th[normalize-space()='"+tableHeader+"']");
            click(loc);
            waitFor(1);
        }


    public void verifySortingOrder(String sortingType,String header) {
        verifyTableSorting(tblCustomer,sortingType,header);
    }

}
