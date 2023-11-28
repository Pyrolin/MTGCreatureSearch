Feature: Can we fetch the image of cards?
  We want to see the card images

  Scenario: Image is shown
    Given I want to browse creature
    When I want to see the image
    Then I should be given the image url