Feature: Is card favorited?
  We want to know if a card is favorited

  Scenario: creatureCard is favorited
    Given creatureCard is favorited
    When I view the card
    Then I should be shown the symbol "<3"

  Scenario: creatureCard isn't favorited
    Given creatureCard isn't favorited
    When I view the card
    Then I shouldn't be shown the symbol "<3"