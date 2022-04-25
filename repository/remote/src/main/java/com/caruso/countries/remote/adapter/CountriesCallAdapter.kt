package com.caruso.countries.remote.adapter

import com.caruso.countries.domain.ResultOf
import retrofit2.Call
import retrofit2.CallAdapter

class CountriesCallAdapter<T>(
    private val clazz: Class<T>,
) : CallAdapter<T, Any> {

    override fun responseType() = clazz

    override fun adapt(call: Call<T>): Call<ResultOf<T>> = CountriesCall(call)
}
