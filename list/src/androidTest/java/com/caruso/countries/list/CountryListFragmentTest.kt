package com.caruso.countries.list

import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.caruso.countries.core_test.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule

@HiltAndroidTest
class CountryListFragmentTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltRule.inject()
    }

    private fun launchCountryListFragment(): TestNavHostController {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        navController.setGraph(R.navigation.list_nav_graph)
        launchFragmentInHiltContainer<CountryListFragment>(navHostController = navController)
        return navController
    }
}