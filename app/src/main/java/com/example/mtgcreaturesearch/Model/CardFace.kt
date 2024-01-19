package com.example.mtgcreaturesearch.Model

import kotlinx.serialization.Serializable


@Serializable
data class CardFace(
//    val artist: String,
//    val artist_id: String,
//    val color_indicator: List<String>,
    val colors: List<String>? = null,
//    val defense: String,
//    val flavor_name: String,
//    val flavor_text: String,
//    val illustration_id: String,
    val image_uris: ImageUrisX? = null,
//    val mana_cost: String,
//    val name: String,
//    val `object`: String,
    val oracle_text: String? = null,
    val power: String? = null,
    val toughness: String? = null,
//    val type_line: String
)