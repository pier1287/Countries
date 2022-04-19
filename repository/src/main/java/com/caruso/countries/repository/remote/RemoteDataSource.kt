package com.caruso.countries.repository.remote

import com.caruso.countries.repository.ResultOf
import retrofit2.http.GET

internal interface RemoteDataSource {

    @GET("v3.1/all?fields=name,cca3,flags")
    suspend fun getAllCountries(): ResultOf<List<CountryDto>>
}