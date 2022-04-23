package com.caruso.countries.repository

data class Country(
    val id: String,
    val name: String,
    val flagImageUrl: String,
    val continent: String = "",
    val capital: String = ""
)
