package com.example.mtgcreaturesearch.Model

class Query constructor(order: String, q: String, page: Int = 1) {
    val order: String = order
    val q: String = q
    val page: Int = page
}