package com.caruso.countries

import com.caruso.countries.repository.Country
import com.caruso.countries.repository.CountryRepositoryImpl
import com.caruso.countries.repository.NetworkUnavailable
import com.caruso.countries.repository.NotFound
import com.caruso.countries.repository.error
import com.caruso.countries.repository.getOrNull
import com.caruso.countries.repository.remote.CountryDto
import com.caruso.countries.repository.remote.FlagsDto
import com.caruso.countries.repository.remote.NameDto
import com.caruso.countries.repository.remote.CountryRemoteDataSource
import com.caruso.countries.repository.success
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test

class CountryRepositoryImplTest {

    private val remote: CountryRemoteDataSource = mockk {
        coEvery { getAllCountries() } returns arrayOf<CountryDto>().success()
    }
    private val sut = CountryRepositoryImpl(remote)

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

    @Test
    fun `should emit an empty country list`() = runBlocking {
        val actual = sut.observeCountries().first().getOrNull()
        assertTrue(actual!!.isEmpty())
    }

    @Test
    fun `should emit a list of Countries sorted by name`() = runBlocking {
        coEvery { remote.getAllCountries() } returns arrayOf(itaCountryDto, fraCountryDto).success()
        val actual = sut.observeCountries().first().getOrNull()
        val expected = listOf(
            Country("FRA", "FRANCE", "france.svg"),
            Country("ITA", "ITALY", "italy.svg")
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `should emit country detail`() = runBlocking {
        val itaCountryDetailDto = itaCountryDto.copy(
            continents = listOf("EUROPE"),
            capitals = listOf("ROME"),
        )
        coEvery { remote.getCountryDetailById("ITA") } returns
            arrayOf(itaCountryDetailDto, itaCountryDetailDto).success()

        val actual = sut.observeCountryDetail("ITA").first().getOrNull()
        val expected = Country(
            id = "ITA",
            name = "ITALY",
            flagImageUrl = "italy.svg",
            continent = "EUROPE",
            capital = "ROME",
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `should emit NetworkUnavailable on Network error`() = runBlocking {
        coEvery { remote.getAllCountries() } returns NetworkUnavailable.error()
        val actual = sut.observeCountries().first()
        val expected = NetworkUnavailable.error()
        assertEquals(expected, actual)
    }

    @Test
    fun `should emit NotFound error if country is not found`() = runBlocking {
        coEvery { remote.getCountryDetailById(any()) } returns
            arrayOf<CountryDto>().success()

        val actual = sut.observeCountryDetail("ITA").first()
        val expected = NotFound.error()
        assertEquals(expected, actual)
    }
}
