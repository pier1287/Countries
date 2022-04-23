package com.caruso.countries.list

import app.cash.turbine.test
import com.caruso.countries.core_test.CoroutineRule
import com.caruso.countries.repository.*
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CountryListViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val countries = listOf(
        Country(id = "FRA", name = "FRANCE", flagImageUrl = "flagImageUrl"),
        Country(id = "ITA", name = "ITALY", flagImageUrl = "flagImageUrl")
    )

    private val expectedCountries = listOf(
        Country(id = "FRA", name = "FRANCE", flagImageUrl = "flagImageUrl"),
        Country(id = "ITA", name = "ITALY", flagImageUrl = "flagImageUrl")
    )

    private val countryRepository: CountryRepository = mockk {
        every { observeCountries() } returns flow {
            emit(countries.success())
            delay(1)
            emit((countries + Country("ESP", "SPAIN", "flagImageUrl")).success())
        }
    }

    @Test
    fun `should emit a state with Countries`() = coroutineRule.runBlockingTest {
        val sut = CountryListViewModel(countryRepository)
        val expected = CountryListState.Success(expectedCountries)
        sut.state.test {
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `should update the state on next emitted value`() = coroutineRule.runBlockingTest {
        val sut = CountryListViewModel(countryRepository)
        val expected = CountryListState.Success(
            expectedCountries + Country(id = "ESP", name = "SPAIN", "flagImageUrl")
        )
        sut.state.test {
            awaitItem()
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `should emit show loading as initial state`() = coroutineRule.runBlockingTest {
        val countryRepository: CountryRepository = mockk {
            every { observeCountries() } returns flow { }
        }
        val sut = CountryListViewModel(countryRepository)
        val expected = CountryListState.Loading
        sut.state.test {
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `should emit error state if an error occurs`() = coroutineRule.runBlockingTest {
        val countryRepository: CountryRepository = mockk {
            every { observeCountries() } returns flow {
                emit(NetworkUnavailable.error())
            }
        }
        val sut = CountryListViewModel(countryRepository)

        val expected = CountryListState.Error(NetworkUnavailable)
        sut.state.test {
            assertEquals(expected, awaitItem())
        }
    }
}