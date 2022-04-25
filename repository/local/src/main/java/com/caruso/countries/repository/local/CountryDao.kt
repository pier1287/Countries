package com.caruso.countries.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface CountryDao {
    @Query("SELECT * FROM country")
    suspend fun getAllCountries(): List<CountryEntity>

    @Transaction
    @Query("SELECT * FROM country WHERE id== :countryId")
    suspend fun getCountryById(countryId: String): CountryWithDetail?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountry(vararg countries: CountryEntity)

    @Transaction
    suspend fun insertCountryWithDetail(countryWithDetail: CountryWithDetail) {
        insertCountry(countryWithDetail.country)
        countryWithDetail.detail?.let { insertCountryDetail(it) }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountryDetail(vararg countriesDetail: CountryDetailEntity)
}
