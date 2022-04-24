package com.caruso.countries.repository

import com.caruso.countries.repository.remote.CountryRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CountryRepositoryImpl @Inject constructor(
    private val remote: CountryRemoteDataSource
) : CountryRepository {
    override fun observeCountries(): Flow<ResultOf<List<Country>>> = flow {
        val countryList = remote.getAllCountries()
            .map { it.toEntities() }
            .map { it.sortedBy(Country::name) }
        emit(countryList)
    }

    override fun observeCountryDetail(countryId: String): Flow<ResultOf<Country>> = flow {
        val country = remote.getCountryDetailById(countryId)
            .flatMap { it.firstOrNull()?.success() ?: NotFound.error() }
            .map { it.toEntity() }
        emit(country)
    }
}
