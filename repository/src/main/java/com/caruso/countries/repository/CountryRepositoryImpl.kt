package com.caruso.countries.repository

import com.caruso.countries.repository.local.CountryDao
import com.caruso.countries.repository.remote.CountryRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CountryRepositoryImpl @Inject constructor(
    private val remote: CountryRemoteDataSource,
    private val local: CountryDao
) : CountryRepository {

    override fun observeCountries(): Flow<ResultOf<List<Country>>> = flow {
        val localCountries = local.getAllCountries().toEntities().sortedBy(Country::name)
        if (localCountries.isNotEmpty()) emit(localCountries.success())

        val remoteCountries = remote.getAllCountries()
            .onSuccess {
                val locals = it.toLocalEntities()
                local.insertCountry(*locals.toTypedArray())
            }.map { it.toEntities().sortedBy(Country::name) }

        emit(remoteCountries)
    }

    override fun observeCountryDetail(countryId: String): Flow<ResultOf<Country>> = flow {
        val country = remote.getCountryDetailById(countryId)
            .flatMap { it.firstOrNull()?.success() ?: NotFound.error() }
            .map { it.toEntity() }
        emit(country)
    }
}
