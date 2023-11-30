package com.example.mtgcreaturesearch

import CardApi
import com.example.mtgcreaturesearch.Model.Data
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import kotlinx.coroutines.*
import kotlin.system.*

class StepDefs {
    lateinit var listResult: List<Data>
    lateinit var imageUrl: String

    lateinit var card: Data

    var favorites: ArrayList<String> = ArrayList()

    @Given("Cards are visible")
    fun api_has_a_creature() = runBlocking {
        listResult = getCratures()
        card = listResult[0]
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    @When("I want to see the image")
    fun i_want_to_get_the_image() {
        imageUrl = card.image_uris.png
    }

    @When("I want to favorite the card")
    fun i_want_to_favorite_the_card() {
        favorites.add(card.id)
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    @Then("I should be given the image url {string}")
    fun i_should_be_given_the_image_url(url: String) {
        assert(card.image_uris.png == url)
    }

    @Then("the card should be favorited")
    fun the_card_should_be_favorited() {
        assert(favorites.contains(card.id))
    }
}

suspend fun getCratures(): List<Data> {
    return CardApi.retrofitService.getPhotos().data
}