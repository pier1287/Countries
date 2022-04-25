package com.caruso.countries.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CountryDao {
    @Query("SELECT * FROM CountryEntity")
    suspend fun getAllCountries(): List<CountryEntity>

    @Query("SELECT * FROM CountryEntity WHERE id== :countryId")
    suspend fun getCountryById(countryId: String): CountryEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountry(vararg users: CountryEntity)
}
