package com.caruso.countries

import com.caruso.countries.domain.Country
import com.caruso.countries.domain.NetworkUnavailable
import com.caruso.countries.domain.NotFound
import com.caruso.countries.domain.error
import com.caruso.countries.domain.getOrNull
import com.caruso.countries.domain.success
import com.caruso.countries.remote.CountryDto
import com.caruso.countries.remote.CountryRemoteDataSource
import com.caruso.countries.remote.FlagsDto
import com.caruso.countries.remote.NameDto
import com.caruso.countries.repository.CountryRepositoryImpl
import com.caruso.countries.repository.local.CountryDao
import com.caruso.countries.repository.local.CountryDetailEntity
import com.caruso.countries.repository.local.CountryEntity
import com.caruso.countries.repository.local.CountryWithDetail
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test

class CountryRepositoryImplTest {

    private val itaCountryDto = CountryDto(
        cca3 = "ITA",
        name = NameDto(commonName = "ITALY"),
        flags = FlagsDto("italy.png", "italy.svg")
    )

    private val fraCountryDto = CountryDto(
        cca3 = "FRA",
        name = NameDto(commonName = "FRANCE"),
        flags = FlagsDto("france.png", "france.svg")
    )

    private val itaEntity = CountryEntity(
        id = "ITA",
        name = "ITALY",
        flagImageUrl = "italy.svg"
    )
    private val itaEntityDetail = CountryDetailEntity(
        countryId = "ITA",
        continent = "EUROPE",
        capital = "ROME"
    )

    private val remote: CountryRemoteDataSource = mockk {
        coEvery { getAllCountries() } returns arrayOf(fraCountryDto, itaCountryDto).success()
        coEvery { getCountryDetailById(any()) } returns NotFound.error()
        coEvery { getCountryDetailById("ITA") } returns
            arrayOf(itaCountryDto, itaCountryDto).success()
    }

    private val local: CountryDao = mockk(relaxed = true) {
        coEvery { getAllCountries() } returns listOf(itaEntity)
        coEvery { getCountryDetailById(any()) } returns null
        coEvery { getCountryDetailById("ITA") } returns
            CountryWithDetail(itaEntity, itaEntityDetail)
    }

    private val sut = CountryRepositoryImpl(remote, local)

    @Test
    fun `should emit an empty country list`() = runBlocking {
        coEvery { remote.getAllCountries() } returns arrayOf<CountryDto>().success()
        coEvery { local.getAllCountries() } returns emptyList()
        val actual = sut.observeCountries().first().getOrNull()
        assertTrue(actual!!.isEmpty())
    }

    @Test
    fun `should emit two list of Countries sorted by name`() = runBlocking {
        val countriesFlow = sut.observeCountries()
        val first = countriesFlow.first().getOrNull()
        val firstExpected = listOf(Country("ITA", "ITALY", "italy.svg"))
        assertEquals(firstExpected, first)

        val second = countriesFlow.drop(1).first().getOrNull()
        val secondExpected = listOf(Country("FRA", "FRANCE", "france.svg")) + firstExpected
        assertEquals(secondExpected, second)
    }

    @Test
    fun `should emit two country detail`() = runBlocking {
        val itaCountryDetailDto = itaCountryDto.copy(
            continents = listOf("EUROPE"),
            capitals = listOf("ROME"),
        )
        coEvery { local.getCountryDetailById("ITA") } returns CountryWithDetail(itaEntity, null)
        coEvery { remote.getCountryDetailById("ITA") } returns
            arrayOf(itaCountryDetailDto, itaCountryDetailDto).success()

        val countryDetailFlow = sut.observeCountryDetail("ITA")

        val first = countryDetailFlow.first().getOrNull()
        val firstExpected = Country(
            id = "ITA",
            name = "ITALY",
            flagImageUrl = "italy.svg"
        )
        assertEquals(firstExpected, first)

        val second = countryDetailFlow.drop(1).first().getOrNull()
        val secondExpected = Country(
            id = "ITA",
            name = "ITALY",
            flagImageUrl = "italy.svg",
            continent = "EUROPE",
            capital = "ROME",
        )
        assertEquals(secondExpected, second)
    }

    @Test
    fun `should emit country detail only one time when unchanged`() = runBlocking {
        val itaCountryDetailDto = itaCountryDto.copy(
            continents = listOf("EUROPE"),
            capitals = listOf("ROME"),
        )
        coEvery { local.getCountryDetailById("ITA") } returns
            CountryWithDetail(itaEntity, itaEntityDetail)

        coEvery { remote.getCountryDetailById("ITA") } returns
            arrayOf(itaCountryDetailDto, itaCountryDetailDto).success()

        val countryDetailFlow = sut.observeCountryDetail("ITA")

        val first = countryDetailFlow.first().getOrNull()
        val firstExpected = Country(
            id = "ITA",
            name = "ITALY",
            flagImageUrl = "italy.svg",
            continent = "EUROPE",
            capital = "ROME",
        )
        assertEquals(firstExpected, first)
        assertEquals(1, countryDetailFlow.count())
    }

    @Test
    fun `should emit NetworkUnavailable on Network error`() = runBlocking {
        coEvery { local.getAllCountries() } returns emptyList()
        coEvery { remote.getAllCountries() } returns NetworkUnavailable.error()
        val actual = sut.observeCountries().first()
        val expected = NetworkUnavailable.error()
        assertEquals(expected, actual)
    }

    @Test
    fun `should emit NotFound error if country is not found`() = runBlocking {
        coEvery { local.getCountryDetailById(any()) } returns null
        coEvery { remote.getCountryDetailById(any()) } returns NotFound.error()

        val actual = sut.observeCountryDetail("ITA").first()
        val expected = NotFound.error()
        assertEquals(expected, actual)
    }
}
