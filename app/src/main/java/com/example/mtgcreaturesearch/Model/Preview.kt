package com.example.mtgcreaturesearch.Model
import kotlinx.serialization.Serializable


@Serializable
data class Preview(
    val previewed_at: String,
    val source: String,
    val source_uri: String
)