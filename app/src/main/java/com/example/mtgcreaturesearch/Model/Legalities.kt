package com.example.mtgcreaturesearch.Model
import kotlinx.serialization.Serializable


@Serializable
data class Legalities(
    val alchemy: String,
    val brawl: String,
    val commander: String,
    val duel: String,
    val explorer: String,
    val future: String,
    val gladiator: String,
    val historic: String,
    val historicbrawl: String,
    val legacy: String,
    val modern: String,
    val oathbreaker: String,
    val oldschool: String,
    val pauper: String,
    val paupercommander: String,
    val penny: String,
    val pioneer: String,
    val predh: String,
    val premodern: String,
    val standard: String,
    val vintage: String
)