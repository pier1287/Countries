package com.caruso.countries.repository.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CountryEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "flag_image_url") val flagImageUrl: String,
    @ColumnInfo(name = "continent") val continent: String?,
    @ColumnInfo(name = "capital") val capital: String?
)
