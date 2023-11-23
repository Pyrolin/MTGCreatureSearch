package com.example.mtgcreaturesearch.model

import androidx.compose.runtime.mutableStateOf

class Creature constructor(creatureName: String, favorite: Boolean){

    private val name: String = creatureName
    private var _favorited = mutableStateOf(favorite)
    private var favorited: Boolean = _favorited.value

    fun setFavorited(favorite: Boolean) {
        favorited = favorite
    }

    fun getName(): String {
        return name
    }

    fun getFavorited(): Boolean {
        return favorited
    }

}