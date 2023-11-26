package com.example.mtgcreaturesearch.Model
import kotlinx.serialization.Serializable


@Serializable
data class Prices(
    val eur: String,
    val eur_foil: String,
    val tix: String,
    val usd: String,
    val usd_etched: String,
    val usd_foil: String
)