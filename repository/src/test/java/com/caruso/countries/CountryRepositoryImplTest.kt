package com.caruso.countries

import com.caruso.countries.repository.*
import com.caruso.countries.repository.remote.CountryDto
import com.caruso.countries.repository.remote.FlagsDto
import com.caruso.countries.repository.remote.NameDto
import com.caruso.countries.repository.remote.RemoteDataSource
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test

class CountryRepositoryImplTest {

    private val remote: RemoteDataSource = mockk {
        coEvery { getAllCountries() } returns arrayOf<CountryDto>().success()
    }
    private val sut = CountryRepositoryImpl(remote)

    private val countryDto = CountryDto(
        cca3 = "ITA",
        name = NameDto(commonName = "ITALY"),
        flags = FlagsDto("png", "svg")
    )

    @Test
    fun `should emit an empty country list`() = runBlocking {
        val actual = sut.observeCountries().first().getOrNull()
        assertTrue(actual!!.isEmpty())
    }

    @Test
    fun `should emit a list with one Country`() = runBlocking {
        coEvery { remote.getAllCountries() } returns arrayOf(countryDto).success()
        val actual = sut.observeCountries().first().getOrNull()
        val expected = listOf(Country("IT", "ITALY"))
        assertEquals(expected, actual)
    }

    @Test
    fun `should emit NetworkUnavailable on Network error`() = runBlocking {
        coEvery { remote.getAllCountries() } returns NetworkUnavailable.error()
        val actual = sut.observeCountries().first()
        val expected = NetworkUnavailable.error()
        assertEquals(expected, actual)
    }
}