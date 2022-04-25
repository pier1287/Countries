package com.caruso.countries.repository.local

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "country")
data class CountryEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "flag_image_url") val flagImageUrl: String
)

@Entity(tableName = "country_detail")
data class CountryDetailEntity(
    @PrimaryKey @ColumnInfo(name = "country_id") val countryId: String,
    @ColumnInfo(name = "continent") val continent: String,
    @ColumnInfo(name = "capital") val capital: String
)

data class CountryWithDetail(
    @Embedded val country: CountryEntity,
    @Relation(parentColumn = "id", entityColumn = "country_id")
    val detail: CountryDetailEntity?
)
