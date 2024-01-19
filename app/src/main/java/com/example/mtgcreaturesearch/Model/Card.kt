package com.example.mtgcreaturesearch.Model

import kotlinx.serialization.Serializable


@Serializable
data class Card(
    val `data`: List<Data>,
//    val has_more: Boolean,
//    val next_page: String,
//    val `object`: String,
//    val total_cards: Int
)