package com.caruso.countries.repository

import com.caruso.countries.repository.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class CountryRepositoryImpl @Inject constructor(
    private val remote: RemoteDataSource
) : CountryRepository {
    override fun observeCountries(): Flow<ResultOf<List<Country>>> = flow {
        val countryList = remote.getAllCountries()
            .map { it.toEntities() }
            .map { it.sortedBy(Country::name) }
        emit(countryList)
    }

    override fun observeCountryDetail(countryName: String): Flow<ResultOf<Country>> = flow {
        val country = remote.getCountryDetailByName(countryName).map { it.toEntity() }
        emit(country)
    }
}
