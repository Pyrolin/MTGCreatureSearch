Feature: Can the creature name be shown?
  We want to see the name if the creatures

  Scenario: Creature name is shown
    Given Creature has a name
    When I want to be shown the name
    Then I should be shown the name "Creature Name"