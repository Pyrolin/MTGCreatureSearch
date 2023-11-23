Feature: Is card favorited?
  We want to know if a card is favorited

  Scenario: creatureCard is favorited
    Given creatureCard is favorited
    When I view the card
    Then I should be shown "<3"