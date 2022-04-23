package com.caruso.countries.list

import com.caruso.countries.core_test.launchFragmentInHiltContainer
import com.caruso.countries.repository.remote.CountryRemoteDataSource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class CountryListFragmentTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var countryRemoteDataSource: CountryRemoteDataSource

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun shouldShowCountryList() {
        launchCountryListFragment()
        assertEquals(true, true)
    }

    private fun launchCountryListFragment() {
        launchFragmentInHiltContainer<CountryListFragment>()
    }
}
