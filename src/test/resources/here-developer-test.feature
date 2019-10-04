Feature: Here developer documentation test

  Scenario: Checking here developer documentation page for broken links and verifying angular is loaded in all the pages.
    Given I am on here developer documnetation page
    When I get all the links on the page
    Then I verify all links for 200 status code
    Then I verify angualr is initialized for all pages