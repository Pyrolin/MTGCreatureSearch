package com.example.mtgcreaturesearch.Model
import kotlinx.serialization.Serializable


@Serializable
data class AllPart(
    val component: String,
    val id: String,
    val name: String,
    val `object`: String,
    val type_line: String,
    val uri: String
)