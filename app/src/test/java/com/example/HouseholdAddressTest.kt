package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.AppDatabase
import com.example.data.Household
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class HouseholdAddressTest {
    private lateinit var db: AppDatabase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun tearDown() = db.close()

    @Test
    fun addressLookupDoesNotMergeSameHouseNumberFromDifferentVillages() = runBlocking {
        val firstId = db.householdDao().insert(
            Household(houseNo = "10", villageNo = "1", subdistrict = "A", district = "B", province = "C")
        )
        val secondId = db.householdDao().insert(
            Household(houseNo = "10", villageNo = "2", subdistrict = "A", district = "B", province = "C")
        )

        val first = db.householdDao().getHouseholdByAddress("10", "1", "A", "B", "C")
        val second = db.householdDao().getHouseholdByAddress("10", "2", "A", "B", "C")

        assertEquals(firstId, first?.id)
        assertEquals(secondId, second?.id)
    }
}

