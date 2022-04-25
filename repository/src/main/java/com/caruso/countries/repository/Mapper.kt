package com.caruso.countries.repository

import com.caruso.countries.repository.local.CountryEntity
import com.caruso.countries.repository.remote.CountryDto

internal fun List<CountryEntity>.toEntities(): List<Country> = map { it.toEntity() }

internal fun CountryEntity.toEntity(): Country = Country(
    id = id,
    name = name,
    flagImageUrl = flagImageUrl,
    continent = continent.orEmpty(),
    capital = capital.orEmpty()
)

internal fun Array<CountryDto>.toEntities(): List<Country> = map { it.toEntity() }
internal fun CountryDto.toEntity(): Country = Country(
    id = cca3,
    name = name.commonName,
    flagImageUrl = flags.svg,
    continent = continents.firstOrNull().orEmpty(),
    capital = capitals.firstOrNull().orEmpty()
)

internal fun Array<CountryDto>.toLocalEntities(): List<CountryEntity> = map { it.toLocalEntity() }
internal fun CountryDto.toLocalEntity(): CountryEntity = CountryEntity(
    id = cca3,
    name = name.commonName,
    flagImageUrl = flags.svg,
    continent = continents.firstOrNull().orEmpty(),
    capital = capitals.firstOrNull().orEmpty()
)
