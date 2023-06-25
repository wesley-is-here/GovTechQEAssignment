Feature: QE Tech Challenge

  Background:
    Given user launches the url "https://www.videoindexer.ai"
    And I click on Personal Microsoft Account
    And I enter my email "testing8692@outlook.com" to Microsoft Outlook Login Page
    Then I click on next button on Microsoft Outlook Login Page
    And I enter my password "password12345" to Microsoft Outlook Login Page
    Then I click on Sign In button on Microsoft Outlook Login Page
    And I click on Yes to confirm Sign In
    Then I check video-indexer.ai media library page is loaded

  @UserStory1
  Scenario Outline: User Story 1 – Add A Video
    Given I click on Upload button
    And I upload the video file "<Video>"
    And I select Privacy drop down option to "<PrivacyType>"
    And I select Video source language drop down option to "<Language>"
    Then I check the checkbox to accept terms and conditions
    And I click on Upload + Index button
    Then I wait for file to upload
    And I click on close button
    And I check indexing has completed
    Then I check video "<VideoName>" is visible

    Examples:
      | Video       | PrivacyType | Language | VideoName |
      | Video_2.mp4 | Private     | Dutch    | Video_2   |
      | Video_3.mp4 | Public      | Danish   | Video_3   |



  @UserStory2
  Scenario: User Story 2 – View Video Insights
    Given I click on the video file "Video_2"
    And I check if video player is displayed
    Then I check if video is still playing if not click on play once
    And I should see the segment "audio effect (preview)"
    And I should see the segment "keywords"
    And I should see the segment "labels"
    And I should see the segment "named entity"
    Then I click on "Audio" Timeline Bar percent 50
    Then I click on "Keywords" Timeline Bar percent 50
    Then I click on "Labels" Timeline Bar percent 50
    Then I click on "Entities" Timeline Bar percent 50
    And I click on the Timeline tab
    Then I can see the full transcribed text from the video
    And I click on the download insight button
    Then I click to download "Insights (JSON)" completed insights
    And I switch to Tab with title "Azure Video Indexer Portal - Cognitive Video Indexing"
    And I click on the download insight button
    Then I click to download "Artifacts (ZIP)" completed insights
    And I click on the download insight button
    Then I click to download "Closed captions" completed insights
    And I click on download button in Pop-Up
    Then I click on cancel button in Pop-Up


  @UserStory3
  Scenario: User Story 3 – Search Videos with Insights
    Given I click on Filter button
    And I select Search In drop down option to "Entire video"
    And I select Insight Type drop down option to "People"
    And I enter "Video_3" text into Search Box
    And I click on Search Icon button
    Then I check for availability of Indexed Videos
    And I hover over each video tile to reveal video information
    And I click on view drop down to select view
    Then I select List View
    And I click on view drop down to select view
    Then I select Tiled View























