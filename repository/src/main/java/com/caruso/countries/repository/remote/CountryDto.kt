package com.caruso.countries.repository.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CountryDto(
    @SerialName("cca3") val cca3: String,
    @SerialName("name") val name: NameDto,
    @SerialName("flags") val flags: FlagsDto,
    @SerialName("capital") val capitals: List<String> = emptyList(),
    @SerialName("continents") val continents: List<String> = emptyList(),
)

@Serializable
internal data class FlagsDto(
    @SerialName("png") val png: String,
    @SerialName("svg") val svg: String,
)

@Serializable
internal data class NameDto(
    @SerialName("common") val commonName: String
)