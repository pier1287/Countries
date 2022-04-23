package com.caruso.countries.repository

import com.caruso.countries.repository.remote.CountryDto

internal fun Array<CountryDto>.toEntities(): List<Country> = map { it.toEntity() }

internal fun CountryDto.toEntity(): Country = Country(
    id = cca3,
    name = name.commonName,
    flagImageUrl = flags.svg,
    continent = continents.firstOrNull().orEmpty(),
    capital = capitals.firstOrNull().orEmpty()
)