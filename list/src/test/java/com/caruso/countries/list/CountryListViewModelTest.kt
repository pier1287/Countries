package com.caruso.countries.list

import app.cash.turbine.test
import com.caruso.countries.core_test.CoroutineRule
import com.caruso.countries.repository.Country
import com.caruso.countries.repository.CountryRepository
import com.caruso.countries.repository.success
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CountryListViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val countries = listOf(
        Country(id = "FRA", name = "FRANCE"),
        Country(id = "ITA", name = "ITALY")
    )

    private val countryRepository: CountryRepository = mockk {
        every { observeCountries() } returns flow { emit(countries.success()) }
    }

    @Test
    fun `should emit a state with two Countries`() = coroutineRule.runBlockingTest {
        val sut = CountryListViewModel(countryRepository)
        val expectedCountries = listOf(
            Country(id = "FRA", name = "FRANCE"),
            Country(id = "ITA", name = "ITALY")
        )
        val expected = CountryListState(expectedCountries)
        sut.state.test {
            assertEquals(expected, awaitItem())
        }
    }
}