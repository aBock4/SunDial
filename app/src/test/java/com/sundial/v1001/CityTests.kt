package com.sundial.v1001

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sundial.v1001.dto.City
import com.sundial.v1001.service.CityService
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class CityTests {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var cityService : CityService
    private var allCities: List<City>? = ArrayList<City>()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Given service connects to City JSON stream when data is read and parsed then City collection should be greater than zero`() = runTest {
        givenCityServiceIsInitialized()
        whenCityDataIsReadAndParsed()
        thenCityShouldContainCincinnati()
    }

    private fun givenCityServiceIsInitialized() {
        cityService = CityService(androidApplication())
    }

    private suspend fun whenCityDataIsReadAndParsed() {
        allCities = cityService.fetchCities()
    }

    private fun thenCityShouldContainCincinnati()
    {
        assertNotNull(allCities)
        assertTrue(allCities!!.isNotEmpty())
        var containsCincinnati = false
        allCities!!.forEach {
            if (it.cityName == "Cincinnati") {
                containsCincinnati = true
            }
        }
        assertTrue(containsCincinnati)
    }
}