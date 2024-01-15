package com.example.mtgcreaturesearch.Model

class ShownCards  constructor(url: String, id: String, toughness: String?, power: String?, cmc: Double?, layout: String?, colors: List<String>?, oracle_text: String?, card_faces: List<CardFace>?) {

    var url: String = url
    var id: String = id
    var toughness: String? = toughness
    var power: String? = power
    var cmc: Double? = cmc
    var layout: String? = layout
    var colors: List<String>? = colors
    var oracle_text: String? = oracle_text
    var card_faces: List<CardFace>? = card_faces
}