package com.caruso.countries.repository

import com.caruso.countries.repository.local.CountryDao
import com.caruso.countries.repository.local.CountryWithDetail
import com.caruso.countries.repository.remote.CountryRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
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
    }.distinctUntilChanged()

    override fun observeCountryDetail(countryId: String): Flow<ResultOf<Country>> = flow {
        val localCountryDetail = local.getCountryDetailById(countryId)?.toEntity()
        localCountryDetail?.let { emit(it.success()) }

        val country = remote.getCountryDetailById(countryId)
            .flatMap { it.firstOrNull()?.success() ?: NotFound.error() }
            .onSuccess {
                val countryWithDetail =
                    CountryWithDetail(it.toLocalEntity(), it.toDetailLocalEntity())
                local.insertCountryWithDetail(countryWithDetail)
            }.map { it.toEntity() }
        emit(country)
    }.distinctUntilChanged()
}
