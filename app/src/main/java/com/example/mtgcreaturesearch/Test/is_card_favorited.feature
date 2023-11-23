Feature: Is card favorited?
  We want to know when if a card is favorited

  Scenario: creatureCard isn't favorited
    Given creatureCard isn't favorited
    When I view the card
    Then I should be shown no <3