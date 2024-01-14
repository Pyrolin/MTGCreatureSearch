package com.example.mtgcreaturesearch.Model

class ShownCards  constructor(url: String, id: String, toughness: String?, power: String?, cmc: Double?, layout: String?, colors: List<String>?, oracle_text: String?){

    var url: String = url
    var id: String = id
    var toughness: String? = toughness
    var power: String? = power
    var cmc: Double? = cmc
    var layout: String? = layout
    var colors: List<String>? = colors
    var oracle_text: String? = oracle_text
}