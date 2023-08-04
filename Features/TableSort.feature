Feature: Table Sort Feature

  Background:
    Given user launches the url "https://the-internet.herokuapp.com/tables"


  @UserStory1
  Scenario: Table Sorting
  Given user clicks on "Last Name" sorting
  And verify the "asc" order for the header "Last Name"
  Then user clicks on "Last Name" sorting
  And verify the "desc" order for the header "Last Name"
  When user clicks on "First Name" sorting
  And verify the "asc" order for the header "First Name"
  Then user clicks on "First Name" sorting
  And verify the "desc" order for the header "First Name"