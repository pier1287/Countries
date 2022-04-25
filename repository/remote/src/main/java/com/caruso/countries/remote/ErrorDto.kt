package com.caruso.countries.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorDto(
    @SerialName("status") val status: Int?,
    @SerialName("message") val message: String?
)
