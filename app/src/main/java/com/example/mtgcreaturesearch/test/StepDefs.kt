package com.example.mtgcreaturesearch.test

import com.example.mtgcreaturesearch.model.Creature
import com.example.mtgcreaturesearch.model.Creatures
import io.cucumber.java.PendingException
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.Assert.*
import kotlin.properties.Delegates

class StepDefs {

    private lateinit var card: Creature
    private var isFavorited by Delegates.notNull<Boolean>()
    private lateinit var favoriteAnswer: String

    // is_card_favorited
    @Given("creatureCard is favorited")
    fun creature_card_is_favorited() {
        card = Creature("Creature", false)
        card.setFavorited(true)
    }

    @Given("creatureCard isn't favorited")
    fun creature_card_isn_t_favorited() {
        card = Creature("Creature", true)
        card.setFavorited(false)
    }

    @When("I view the card")
    fun i_view_the_card() {
        isFavorited = card.getFavorited()
    }

    @Then("I should be shown the symbol {string}")
    fun i_should_be_shown(expectedAnswer: String) {

        favoriteAnswer = if(isFavorited) "<3" else "";

        assertEquals(expectedAnswer, favoriteAnswer)
    }

    @Then("I shouldn't be shown the symbol {string}")
    fun i_shouldn_t_be_shown_the_symbol(expectedAnswer: String) {
        favoriteAnswer = if(isFavorited) "<3" else "";

        assertNotEquals(expectedAnswer, favoriteAnswer)
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    private var cards = Creatures()
    private var cardsLength = 0
    private var listAnswer by Delegates.notNull<Int>()

    // is_card_added_to_list
    @Given("I add a card to the list")
    fun i_add_a_card_to_the_list() {
        card = Creature("Creature", false)

        cards.addElement(card)
    }

    @When("I check the list")
    fun i_check_the_list() {
        listAnswer = cards.element.size
    }

    @Then("I should see the list be {int} longer")
    fun i_should_see_the_list_be_longer(expectedAnswer: Int) {
        assertEquals(expectedAnswer, (listAnswer - cardsLength))
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    private lateinit var nameAnswer: String

    // can_name_be_shown
    @Given("Creature has a name")
    fun creature_has_a_name() {
        card = Creature("Creature Name", false)
    }

    @When("I want to be shown the name")
    fun i_want_to_be_shown_the_name() {
        nameAnswer = card.getName()
    }

    @Then("I should be shown the name {string}")
    fun i_should_be_shown_the_name(expectedAnswer: String) {
        assertEquals(expectedAnswer, nameAnswer)
    }

}
