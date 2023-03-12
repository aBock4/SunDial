package com.sundial.v1001

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sundial.v1001.dto.Twilight
import com.sundial.v1001.service.TwilightService
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class TwilightTests {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private lateinit var twilightService : TwilightService
    private var allTwilights: List<Twilight>? = ArrayList<Twilight>()

    @Test
    fun `Given service connects to Twilight JSON stream when data is read and parsed then twilight collection should be greater than zero`() = runTest {
        givenTwilightServiceIsInitialized()
        whenTwilightDataIsReadAndParsed()
        thenTheTwilightDataShouldNotBeNull()
    }
    private fun givenTwilightServiceIsInitialized() {
        twilightService = TwilightService()
    }
    private suspend fun whenTwilightDataIsReadAndParsed() {
        allTwilights = twilightService.fetchTwilight()
    }
    private fun thenTheTwilightDataShouldNotBeNull() {
        Assert.assertNotNull(allTwilights)
        Assert.assertTrue(allTwilights!!.isNotEmpty())
    }
}