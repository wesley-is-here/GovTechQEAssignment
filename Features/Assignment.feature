Feature: QE Tech Challenge

  Background:
    Given user launches the url "https://www.videoindexer.ai"
    And I click on Personal Microsoft Account
    And I enter my email "testing8694@outlook.com" to Microsoft Outlook Login Page
    Then I click on next button on Microsoft Outlook Login Page


  @UserStory1
  Scenario Outline: User Story 1 – Add A Video
    Given I enter my password "password888" to Microsoft Outlook Login Page
    When I click on Sign In button on Microsoft Outlook Login Page
    And I click on Yes to confirm Sign In
    Then I check video-indexer.ai media library page is loaded
    And click or wait some time for Pop-Up to diminish
    Then I check the "Upload" element button is enabled and clickable
    When I click on Upload button
    And I upload the video file "<Video>"
    And I select Privacy drop down option to "<PrivacyType>"
    And I select Video source language drop down option to "<Language>"
    Then I check the checkbox to accept terms and conditions
    When I click on Upload + Index button
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
    Given I enter my password "password888" to Microsoft Outlook Login Page
    When I click on Sign In button on Microsoft Outlook Login Page
    And I click on Yes to confirm Sign In
    Then I check video-indexer.ai media library page is loaded
    And click or wait some time for Pop-Up to diminish
    When I click on the video file "Video_3"
    And I check if video player is displayed
    Then I check if video is still playing if not click on play once
    And I should see the segment "labels"
#    And I should see the segment "audio effect (preview)"
#    And I should see the segment "keywords"
#    And I should see the segment "named entity"
    When I click on "Labels" Timeline Bar percent 50
#    When I click on "Audio" Timeline Bar percent 50
#    When I click on "Keywords" Timeline Bar percent 50
#    When I click on "Entities" Timeline Bar percent 50
    And I click on the Timeline tab
    Then I can see the full transcribed text from the video
    When I click on the download insight button
    Then I click to download "Insights (JSON)" completed insights
    And I switch to Tab with title "Azure AI Video Indexer Portal - Cognitive Video Indexing"
    When I click on the download insight button
    Then I click to download "Artifacts (ZIP)" completed insights
    When I click on the download insight button
    Then I click to download "Closed captions" completed insights
    And I click on download button in Pop-Up
    And I verify SRT content with "1\n00:00:00,060 --> 00:00:01,800\nDer bor.\n\n2\n00:00:02,130 --> 00:00:04,580\nThree, two way."
    Then I click on cancel button in Pop-Up


  @UserStory3
  Scenario: User Story 3 – Search Videos with Insights
    Given I enter my password "password888" to Microsoft Outlook Login Page
    When I click on Sign In button on Microsoft Outlook Login Page
    And I click on Yes to confirm Sign In
    Then I check video-indexer.ai media library page is loaded
    And click or wait some time for Pop-Up to diminish
    When I click on Filter button
    And I select Search In drop down option to "Entire video"
    And I select Insight Type drop down option to "People"
    And I enter "Video_3" text into Search Box
    When I click on Search Icon button
    Then I check for availability of Indexed Videos
    And I hover over each video tile to reveal video information
    When I click on view drop down to select view
    Then I select List View
    When I click on view drop down to select view
    Then I select Tiled View


  @VerifyPDFDocument
  Scenario: Verify PDF Document
    Given I read PDF file "resources/PDFs/sample.pdf" and check its content contains "In pulvinar mattis enim a convallis. Cras aliquet urna faucibus ipsum venenatis venenatis." from page Number 12



  @NegativeTestCase
  Scenario: Login Fail
    Given I enter my password "1" to Microsoft Outlook Login Page
    When I click on Sign In button on Microsoft Outlook Login Page
    Then I verify error on page "Your account or password is incorrect."





    # Given: The Given step sets up the initial state or preconditions for the scenario.
    # And: The And step is used to continue the scenario by adding additional steps that have the same semantic meaning as the previous step.
    # When: The When step describes the actions or events that are performed as part of the scenario.
    # Then: Then: The Then step specifies the expected outcomes or assertions that should be true after the actions in the When step have been performed.















