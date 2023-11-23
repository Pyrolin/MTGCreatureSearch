Feature: Is card added to the list?
  We want to see if a card gets added to the list of cards

  Scenario: Card is added to the list
    Given I add a card to the list
    When I check the list
    Then I should see the list be 1 longer

