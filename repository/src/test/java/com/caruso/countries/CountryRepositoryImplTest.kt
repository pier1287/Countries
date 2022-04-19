package com.caruso.countries

import com.caruso.countries.repository.CountryRepositoryImpl
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test

class CountryRepositoryImplTest {

    private val sut = CountryRepositoryImpl()

    @Test
    fun `should emit an empty country list`() = runBlocking {
        val firstItem = sut.observeCountries().first()
        assertTrue(firstItem.isEmpty())
    }
}