package com.example.soul

import android.location.Location
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class PreviewScreenViewModelTest {

    private lateinit var dispatcher: TestDispatcher
    private lateinit var fakeLocation:Location
    private lateinit var repository: FirstAppModuleRepository
    private lateinit var roomRepository:ReportsRepoRoom
    private lateinit var fetchRepository:LocationFetcher
    private lateinit var locationValidator: LocationValidator


    @Before
    fun setMockkDependencies(){
        repository= mockk(name="send repository")
        roomRepository= mockk(name = "id fetch ")
        fetchRepository= mockk(name = "fetch location")
        locationValidator= mockk(name="location validator",relaxed = true)
        fakeLocation = mockk<Location>()
    }

    private fun setFakeLocation(
        lat: Double,
        lng: Double,
        accuracy: Float
    ) {
        every { fakeLocation.latitude } returns lat
        every { fakeLocation.longitude } returns lng
        every { fakeLocation.accuracy } returns accuracy
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun sendComplaint_WhenAllDataValid_ShouldSuccessScreen()= runTest {
        // attach dispatcher with the runTest scheduler because we going to ctrl this scheduler by runCurrent() etc
        dispatcher= StandardTestDispatcher(testScheduler)

        val view=PreviewScreenViewModelClass(repository,roomRepository,fetchRepository,locationValidator,dispatcher,dispatcher)
// set before run function which return result according the parameters
       view.updateComplain("water_leakage")


        setFakeLocation(lat = 23.45795, lng = 88.03848, accuracy = 10f)


val complaintSlot= slot< FirstAppFireStoreDataClass>()
        coEvery { // any() simply accept it // match{} use for check
            repository.sendComplain(capture(complaintSlot))
        }returns Result.success("id6969")

         coEvery { fetchRepository.fetch(any(),any()) } answers {
             val block=arg<(Location?) -> Unit>(0)
             block(fakeLocation)
         }

        coEvery { locationValidator.validate(any(),any(),any(),any(),false) } answers {
            val block=arg<(Location?,Boolean) -> Unit>(1)

            block(fakeLocation,false)
        }

        coEvery {
            locationValidator.validate(any(), any(), any(), any(), true)
        } answers {
            error("validate() should NOT be called with inside=true in this test")
        }


coEvery { roomRepository.fetchNewComplaint(any()) } just runs

        coEvery { roomRepository.fetchTileKeys(any(), any()) } returns emptyList()

       every { locationValidator.reset() } just runs
        view.fetchLocation(inside = false)


       view.sendComplain()

runCurrent()
//advanceUntilIdle()

        val state=view.uiState.value

assertEquals(ComplaintUiState.Success("id6969"),state)

        val data=complaintSlot.captured
        println(data.accuracy)
        assertEquals(10f,data.accuracy)


    }



}