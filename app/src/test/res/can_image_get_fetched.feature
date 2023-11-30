Feature: Can we fetch the image of cards?
  We want to see the card images

  Scenario: Image is shown
    Given Cards are visible
    When I want to see the image
    Then I should be given the image url "https://cards.scryfall.io/png/front/0/e/0ed089db-54b9-40d7-9a91-19f0bab44303.png?1690005365"