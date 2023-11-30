Feature: Can we favorite a card?
  We want to be able to favorite cards

  Scenario: Card is favorited
    Given Cards are visible
    When I want to favorite the card
    Then the card should be favorited