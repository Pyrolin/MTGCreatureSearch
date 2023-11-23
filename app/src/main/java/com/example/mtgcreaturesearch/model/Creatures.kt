package com.example.mtgcreaturesearch.model

import androidx.compose.runtime.mutableStateListOf


class Creatures {
    private val _elements = mutableStateListOf<Creature>();
    val element: List<Creature> = _elements;

    fun addElement(creature: Creature) {
        _elements.add(creature);
    }
}