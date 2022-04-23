package com.caruso.countries.detail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.caruso.countries.core_test.CoroutineRule
import com.caruso.countries.repository.*
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CountryDetailViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val country = Country(
        id = "ITA",
        name = "ITALY",
        flagImageUrl = "flagImageUrl",
        continent = "EUROPE",
        capital = "ROME"
    )

    private val countryRepository: CountryRepository = mockk {
        every { observeCountryDetail(any()) } returns flow { emit(NotFound.error()) }
        every { observeCountryDetail("ITA") } returns flow { emit(country.success()) }
    }

    @Test
    fun `should emit success state with country detail`() = coroutineRule.runBlockingTest {
        val stateHandle = SavedStateHandle().apply { set("id", "ITA") }
        val sut = CountryDetailViewModel(countryRepository, stateHandle)
        val expected = CountryDetailState.Success(country)
        sut.state.test {
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `should emit NotFound error if country not found`() = coroutineRule.runBlockingTest {
        val stateHandle = SavedStateHandle().apply { set("id", "AAA") }
        val sut = CountryDetailViewModel(countryRepository, stateHandle)
        val expected = CountryDetailState.Error(NotFound)
        sut.state.test {
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `should emit show loading as initial state`() = coroutineRule.runBlockingTest {
        val countryRepository: CountryRepository = mockk {
            every { observeCountryDetail(any()) } returns flow { }
        }
        val stateHandle = SavedStateHandle().apply { set("id", "ITA") }
        val sut = CountryDetailViewModel(countryRepository, stateHandle)
        val expected = CountryDetailState.Loading
        sut.state.test {
            assertEquals(expected, awaitItem())
        }
    }
}
