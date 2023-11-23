package com.example.mtgcreaturesearch.test

import com.example.mtgcreaturesearch.model.Creature
import io.cucumber.java.PendingException
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.Assert.*
import kotlin.properties.Delegates

class StepDefs {

    private lateinit var card: Creature
    private var isFavorited by Delegates.notNull<Boolean>()
    private lateinit var actualAnswer: String
    @Given("creatureCard is favorited")
    fun creature_card_is_favorited() {
        card = Creature("Creature", false)
        card.setFavorited(true)
    }

    @When("I view the card")
    fun i_view_the_card() {
        isFavorited = card.getFavorited()
    }

    @Then("I should be shown \"{String}\"")
    fun i_should_be_shown(expectedAnswer: String) {

        actualAnswer = if(isFavorited) "<3" else "";

        assertEquals(expectedAnswer, actualAnswer)
    }
}
