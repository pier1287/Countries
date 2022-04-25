package com.caruso.countries.list

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.caruso.countries.core_test.launchFragmentInHiltContainer
import com.caruso.countries.core_test.matcher.hasItemAtPosition
import com.caruso.countries.domain.NetworkUnavailable
import com.caruso.countries.domain.error
import com.caruso.countries.remote.CountryRemoteDataSource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
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
        val firstItemMatcher = hasItemAtPosition(0, hasDescendant(withText("FRANCE")))
        onView(firstItemMatcher).check(matches(isDisplayed()))

        val secondItemMatcher = hasItemAtPosition(1, hasDescendant(withText("ITALY")))
        onView(secondItemMatcher).check(matches(isDisplayed()))
    }

    @Test
    fun shouldShowSnackBarErrorMessageOnNetworkError() {
        coEvery { countryRemoteDataSource.getAllCountries() } returns NetworkUnavailable.error()
        launchCountryListFragment()

        val snackBarTextId = com.google.android.material.R.id.snackbar_text
        val genericErrorStringRes = com.caruso.countries.core.R.string.generic_error

        val snackBarMatcher = withId(snackBarTextId)
        onView(snackBarMatcher).check(matches(withText(genericErrorStringRes)))
    }

    private fun launchCountryListFragment() {
        launchFragmentInHiltContainer<CountryListFragment>()
    }
}
