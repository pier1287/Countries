package com.caruso.countries.list

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.caruso.countries.core_test.launchFragmentInHiltContainer
import com.caruso.countries.core_test.matcher.hasItemAtPosition
import com.caruso.countries.repository.remote.CountryRemoteDataSource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
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

    private fun launchCountryListFragment() {
        launchFragmentInHiltContainer<CountryListFragment>()
    }
}
