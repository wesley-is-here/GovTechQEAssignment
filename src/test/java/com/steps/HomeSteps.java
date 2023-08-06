package com.steps;

import com.pages.HomePage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;


public class HomeSteps {

//  step definition class that contains the implementation of test steps related to the home page
//  interact with the elements on the home page, perform actions, and make assertions to validate the expected behavior.
//  To perform these actions and assertions, it needs access to the WebDriver instance (to control the browser) and the WebDriverWait instance (to wait for specific conditions).

    // Retrieve the WebDriver instance from the _Hooks class
    WebDriver driver = _Hooks.driver;
    // Retrieve the WebDriverWait instance from the _Hooks class
    WebDriverWait wait = _Hooks.wait;
    // Create a new instance of the HomePage class, passing the WebDriver and WebDriverWait instances
    HomePage homepage = new HomePage(driver, wait);



    @Given("user launches the url {string}")
    public void userLaunchTheUrl(String url) {
        homepage.launchURL(url);
    }

    @Given("I click on Personal Microsoft Account")
    public void userClickPMA() {
        homepage.clickPMA();
    }

    @And("I enter my email {string} to Microsoft Outlook Login Page")
    public void enterEmail(String email) {
        homepage.enterMsoftEmail(email);
    }

    @Then("I click on next button on Microsoft Outlook Login Page")
    public void nextButton() {
        homepage.nextMsoftButton();
    }

    @And("I enter my password {string} to Microsoft Outlook Login Page")
    public void enterPassword(String password) {
        homepage.enterMsoftPassword(password);
    }

    @Then("I click on Sign In button on Microsoft Outlook Login Page")
    public void signInButton() {
        homepage.signInMsoftButton();
    }

    @And("I click on Yes to confirm Sign In")
    public void yesButton() {
        homepage.yesButtonConfirm();
    }

    @Then("I check video-indexer.ai media library page is loaded")
    public void pageLoaded() {
        homepage.waitForPageToLoad();
    }

    @And("I click on Upload button")
    public void uploadButton() {
        homepage.uploadButtonClick();
    }

    @And("I upload the video file {string}")
    public void uploadVideoButton(String vid) {
        homepage.uploadFile(vid);
    }

    @And("I click on the video file {string}")
    public void clickOnTheVideoFile(String video) throws IOException {
        homepage.videoClick(video);
    }

    @And("I select Privacy drop down option to {string}")
    public void selectVideoDropDownPrivacyDetails(String value) {
        homepage.selectVideoDropDownPrivacy(value);
    }

    @And("I select Video source language drop down option to {string}")
    public void selectVideoDropDownVSLDetails(String value) {
        homepage.selectVideoDropDownVSL(value);
    }

    @Then("I check the checkbox to accept terms and conditions")
    public void clickCheckBox() {
        homepage.selectTermsAndConditions();
    }

    @And("I click on Upload + Index button")
    public void clickOnUploadIndexBut() {
        homepage.clickOnUploadIndexButton();
    }

    @Then("I wait for file to upload")
    public void waitFileUpload() {
        homepage.waitForFileToUpload();
    }

    @And("I click on close button")
    public void clickOnCloseButton() {
        homepage.closeButton();

    }

    @And("I check indexing has completed")
    public void iCheckIndexingHasCompleted() {
        homepage.waitForIndexComplete();
    }

    @And("I check video {string} is visible")
    public void videoIsVisible(String video) {
        String videoName = homepage.videoVisible(video);
        Assert.assertEquals(video, videoName.trim());
    }


    @And("I check if video player is displayed")
    public void checkIfVideoPlayerIsDisplayed() {
        homepage.videoPlayerVisible();
    }

    @Then("I check if video is still playing if not click on play once")
    public void checkIfVideoStillPlaying() {
        homepage.checkVideoPlaying();
    }


    @And("I click on the download insight button")
    public void clickOnTheDownloadButtonToDownload() {
        homepage.clickDownloadInsights();
    }

    @Then("I click to download {string} completed insights")
    public void clickDownloadCompletedInsight(String insight) {
        homepage.clickDownloadCompletedInsights(insight);
    }

    @And("I switch to Tab with title {string}")
    public void switchToTabWithTitle(String text) {
        homepage.switchesTab(text);
    }

    @And("I should see the segment {string}")
    public void shouldSeeTheSegment(String text) {
        homepage.seeSegment(text);
    }

    @Then("I click on {string} Timeline Bar percent {int}")
    public void clickOnAudioEffectTimelineBar(String type, int num) {
        homepage.selectVideoTimelineSlider(type, num);
    }

    @And("I click on the Timeline tab")
    public void clickTimelineTab() {
        homepage.clickTimeline();
    }

    @And("I click on download button in Pop-Up")
    public void clickOnDownloadButtonInPopUp() {
        homepage.clickDownloadBtn();
    }

    @Then("I click on cancel button in Pop-Up")
    public void clickOnCancelButtonInPopUp() {
        homepage.clickCancelBtn();
    }

    @Then("I can see the full transcribed text from the video")
    public void fullTranscribedTextFromTheVideo() {
        homepage.transcribedText();
    }

    @And("I enter {string} text into Search Box")
    public void enterIntoSearchBox(String text) {
        homepage.enterTextSearchBox(text);
    }

    @And("I click on Search Icon button")
    public void clickOnSearchIconButton() {
        homepage.clickSearch();
    }

    @And("I click on Filter button")
    public void clickOnFilterButton() {
        homepage.clickFilter();
    }

    @And("I select Search In drop down option to {string}")
    public void selectSearchInDropDownOptionTo(String text) {
        homepage.searchInDropdown(text);
    }

    @And("I select Insight Type drop down option to {string}")
    public void selectInsightTypeDropDownOptionTo(String text) {
        homepage.insightTypeDropdown(text);
    }

    @And("I check for availability of Indexed Videos")
    public void checkForAvailabilityOfIndexes() {
        homepage.checkIndexes();
    }

    @And("I hover over each video tile to reveal video information")
    public void hoverOverEachVideoTileToRevealVideoInformation() {
        homepage.hoverVideoTiles();
    }

    @And("I click on view drop down to select view")
    public void clickOnViewDropDownToSelectView() {
        homepage.viewDropDown();
    }

    @Then("I select List View")
    public void selectListView() {
        homepage.selectLstView();
    }

    @Then("I select Tiled View")
    public void selectTiledView() {
        homepage.selectTledView();
    }


    @And("click or wait some time for Pop-Up to diminish")
    public void clickOrWaitTimeForPopUpToDiminish() {
        homepage.clickOrWaitForPopupGone();
    }

    @And("I verify error on page {string}")
    public void pageIsNotLoaded(String error) {
        String wholeError = homepage.verifyErrorMessage();
        Assert.assertTrue(wholeError.contains(error));
    }


    @And("I verify SRT content with {string}")
    public void verifySRTContent(String text) throws IOException {
        homepage.verifyContent(text);
    }


    @And("I read PDF file {string} and check its content contains {string} from page Number {int}")
    public void readPDFFile(String PDF, String text, int pageNoStart) {
        homepage.readPDFContent(PDF, text, pageNoStart);
    }


    @Then("I check the {string} element button is enabled and clickable")
    public void checkElementButtonIsEnabled(String element) {
        homepage.checkElementEnabledAndClickable(element);
    }

    @Given("user clicks on {string} sorting")
    public void userClicksOnSortButton(String tableHeader) {
        homepage.clickonColHeader(tableHeader);
    }

    @And("verify the {string} order for the header {string}")
    public void verifyTheSortingOrderForTheHeader(String sortingType,String tableHeader) {
        homepage.verifySortingOrder(sortingType,tableHeader);
    }


    @And("I check current page title is {string}")
    public void currentPageTitleIs(String pageTitle) {
        homepage.verifyPageTitle(pageTitle);
    }
}

