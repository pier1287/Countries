package com.caruso.countries.detail

import androidx.core.os.bundleOf
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.caruso.countries.core_test.launchFragmentInHiltContainer
import com.caruso.countries.core_test.matcher.matchToolbarTitle
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
class CountryDetailFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var countryRemoteDataSource: CountryRemoteDataSource

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun shouldShowCountryDetail() {
        launchCountryDetailFragment("ITA")
        matchToolbarTitle("ITALY").check(matches(isDisplayed()))
        onView(withId(R.id.continentTextView)).check(matches(withText("EUROPE")))
        onView(withId(R.id.capitalTextView)).check(matches(withText("ROME")))
    }

    @Test
    fun shouldShowNotFoundError() {
        launchCountryDetailFragment("AAA")

        val snackBarTextId = com.google.android.material.R.id.snackbar_text
        val genericErrorStringRes = com.caruso.countries.core.R.string.not_found_error

        val snackBarMatcher = withId(snackBarTextId)
        onView(snackBarMatcher).check(matches(withText(genericErrorStringRes)))
    }

    @Test
    fun shouldShowGenericErrorOnNetworkUnavailable() {
        coEvery { countryRemoteDataSource.getCountryDetailById(any()) } returns
            NetworkUnavailable.error()

        launchCountryDetailFragment("AAA")

        val snackBarTextId = com.google.android.material.R.id.snackbar_text
        val genericErrorStringRes = com.caruso.countries.core.R.string.generic_error

        val snackBarMatcher = withId(snackBarTextId)
        onView(snackBarMatcher).check(matches(withText(genericErrorStringRes)))
    }

    private fun launchCountryDetailFragment(countryId: String) {
        val bundle = bundleOf("id" to countryId)
        launchFragmentInHiltContainer<CountryDetailFragment>(fragmentArgs = bundle)
    }
}
