package com.caruso.countries.repository.remote

import com.caruso.countries.repository.ResultOf
import retrofit2.http.GET
import retrofit2.http.Path

internal interface RemoteDataSource {

    @GET("v3.1/all?fields=name,cca3,flags")
    suspend fun getAllCountries(): ResultOf<Array<CountryDto>>

    @GET("v3.1/alpha/{cca3}")
    suspend fun getCountryDetailById(@Path("cca3") id: String): ResultOf<Array<CountryDto>>
}