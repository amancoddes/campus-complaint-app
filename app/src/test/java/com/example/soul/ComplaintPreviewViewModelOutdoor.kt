package com.example.soul

import android.location.Location
import com.example.demo.complaintApp.ComplaintPreviewScreenViewModel
import com.example.demo.complaintApp.ComplaintSubmissionRepository
import com.example.demo.complaintApp.ComplaintUiState
import com.example.demo.complaintApp.FirstAppFireStoreDataClass
import com.example.demo.complaintApp.LocationFetcher
import com.example.demo.complaintApp.LocationValidator
import com.example.demo.complaintApp.UserComplaintsReadRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
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
    private lateinit var repository: ComplaintSubmissionRepository
    private lateinit var roomRepository: UserComplaintsReadRepository
    private lateinit var fetchRepository: LocationFetcher
    private lateinit var locationValidator: LocationValidator


    @Before
    fun setMockkDependencies(){
        repository= mockk(name="send repository")
        roomRepository= mockk(name = "id fetch ",relaxed = true)
        fetchRepository= mockk(name = "fetch location")
        locationValidator= mockk(name="location validator",relaxed = true)
        fakeLocation = mockk<Location>()// <- mockk Location class
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
//overview
// fetch -> validate -> viewmodel -> sendComplain() if success -> show Success Screen else show Idle Screen with error message snackBar
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun fetchLocation_WhenLocationIsNull_ShouldShowIdleScreenWithErrorMessage() = runTest {
        dispatcher= StandardTestDispatcher(testScheduler)

        val viewModelObject= ComplaintPreviewScreenViewModel(repository,
            roomRepository,fetchRepository,locationValidator,dispatcher,dispatcher)

        val nullLocation=null
        val isInside=false

        coEvery { fetchRepository.fetch(any(),any()) } answers {
            val block = arg<(Location?) -> Unit>(0)
            block(nullLocation)
        }


        val snackBarMsg= this.async{
            viewModelObject.snackbarEvent.first()
        }

        viewModelObject.fetchLocation(isInside)

        advanceUntilIdle()

    val snackBarMessage=snackBarMsg.await()
    val screenState=viewModelObject.uiState.value
    assertEquals(ComplaintUiState.Idle,screenState)
        assertEquals("some thing wrong location not precise",snackBarMessage)
        coVerify { fetchRepository.fetch(any(),any()) }

    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun fetchLocation_WhenLocationFetcherFails_ShouldShowIdleScreenWithErrorMessage() = runTest {

        dispatcher= StandardTestDispatcher(testScheduler)
        val viewModelObject=ComplaintPreviewScreenViewModel(repository,roomRepository,fetchRepository,locationValidator,dispatcher,dispatcher)


        coEvery { fetchRepository.fetch(any(),any()) } answers {
            val block=arg<(Throwable)-> Unit>(1)

            block(Exception("accuracy is low"))
        }

        val message= this.async {
            viewModelObject.snackbarEvent.first()
        }

        val isInside=false
        viewModelObject.fetchLocation(isInside)

        runCurrent()

        val errorMessage=message.await()
        val screenState=viewModelObject.uiState.value
        assertEquals(ComplaintUiState.Idle,screenState)
        assertEquals("accuracy is low",errorMessage)
        coVerify { fetchRepository.fetch(any(),any()) }

    }



    @OptIn(ExperimentalCoroutinesApi::class)
    @Test// test when accuracy low greater than 18f for outdoor complaints
    fun fetchLocation_WhenLocationValidationFailsDueToAccuracyLow_ShouldShowIdleScreenWithErrorMessage()= runTest{
        dispatcher= StandardTestDispatcher(testScheduler)
        val viewModelObject=ComplaintPreviewScreenViewModel(repository,roomRepository,fetchRepository,locationValidator,dispatcher,dispatcher)
        viewModelObject.updateComplain("water leakage")
        // accuracy greater than 18f
        setFakeLocation(lat = 23.45795, lng = 88.03848, accuracy = 20f)
        coEvery { fetchRepository.fetch(any(),any()) } answers {
            val block=arg<(Location?)-> Unit>(0)
            block(fakeLocation)
        }

        every { locationValidator.validate(any(),any(),any(),any(),any()) } answers {
            val block=arg<()->Unit>(3)
            block()
        }
        val message=this.async {
            viewModelObject.snackbarEvent.first()
        }
        val isInside=false
        viewModelObject.fetchLocation(isInside)

       advanceUntilIdle()

        val errorMessage=message.await()
        val screenState=viewModelObject.uiState.value
        assertEquals(ComplaintUiState.Idle,screenState)
        assertEquals("Location accuracy is low Try again",errorMessage)
        verify { locationValidator.validate(any(),any(),any(),any()) }

    }





    @OptIn(ExperimentalCoroutinesApi::class)
    @Test// test with valid Location and High 10f accuracy
    fun sendComplain_WhenAllDataValid_ShouldShowSuccessScreen()= runTest {

        dispatcher= StandardTestDispatcher(testScheduler)// attach test scheduler with the test dispatcher
        val viewModelClassObject=ComplaintPreviewScreenViewModel(repository,roomRepository,fetchRepository,locationValidator,dispatcher,dispatcher)


        // user inputs
        // complaint title
        viewModelClassObject.updateComplain("water leakage")
        // location
        setFakeLocation(lat = 23.45795, lng = 88.03848, accuracy = 10f)



        coEvery { fetchRepository.fetch(any(),any()) } answers {
            val block=arg<(Location?) -> Unit>(0)
            block(fakeLocation)
        }
        every { locationValidator.validate(any(),any(),any(),any(),false) } answers {
            val block=arg<(Location?,Boolean) -> Unit>(1)
            block(fakeLocation,false)
        }

        val complaintSlot= slot<FirstAppFireStoreDataClass>()
        coEvery { repository.sendComplain(capture(complaintSlot))// user capture to check right input pass in function or not
        }returns Result.success("id6969")


        coEvery { roomRepository.fetchComplaints(any(), any()) } returns Result.success(emptyList())


        viewModelClassObject.fetchLocation(inside = false)
        // call sendComplain after get location from flp and after validate it
        viewModelClassObject.sendComplain()

        advanceUntilIdle()

        val viewModelClassUIState=viewModelClassObject.uiState.value
        assertEquals(ComplaintUiState.Success("id6969"),viewModelClassUIState)
        coVerify { repository.sendComplain(any()) }
        verify { locationValidator.validate(any(),any(),any(),any()) }
        coVerify { roomRepository.fetchComplaints(any(),any()) }

        // check target parameter pass or not in function
        val sendComplaint=complaintSlot.captured// write after when the capture function run
        assertEquals("water leakage",sendComplaint.complain)
    }



    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun sendComplain_WhenComplainTitleIsEmpty_ShouldShowIdleScreenWithErrorMessage()= runTest{
        dispatcher= StandardTestDispatcher(testScheduler)// attach test scheduler with the test dispatcher

        val viewModelClassObject=ComplaintPreviewScreenViewModel(repository,roomRepository,fetchRepository,locationValidator,dispatcher,dispatcher)



        // location
        setFakeLocation(lat = 23.45795, lng = 88.03848, accuracy = 20f)


        val message= async {
            viewModelClassObject.snackbarEvent.first()
        }
        viewModelClassObject.sendComplain()

      advanceUntilIdle()


        val errorMessage=message.await()
        val screenState=viewModelClassObject.uiState.value
        assertEquals(ComplaintUiState.Idle,screenState)
        assertEquals("choose complain type",errorMessage)
    }




    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun sendComplain_WhenUnableToFetchOldComplaints_ShouldShowIdleScreenWithErrorMessage()= runTest {
        dispatcher= StandardTestDispatcher(testScheduler)
        val viewModelClassObject=ComplaintPreviewScreenViewModel(repository,roomRepository,fetchRepository,locationValidator,dispatcher,dispatcher)

        viewModelClassObject.updateComplain("water leakage")
        setFakeLocation(lat = 23.45795, lng = 88.03848, accuracy = 10f)

        coEvery { locationValidator.validate(any(),any(),any(),any()) } answers {
            val block=arg<(Location?,Boolean)-> Unit>(1)
            block(fakeLocation,false)
        }

        coEvery { roomRepository.fetchComplaints(any(),any()) } returns Result.failure(Exception("network low"))
        coEvery { fetchRepository.fetch(any(),any()) } answers {
            val block=arg<(Location?) -> Unit>(0)
           block(fakeLocation)
        }

        val isInsideComplaint=false
        viewModelClassObject.fetchLocation(isInsideComplaint)

        val message= this.async {
            viewModelClassObject.snackbarEvent.first()
        }
        viewModelClassObject.sendComplain()

        advanceUntilIdle()

        val errorMessage=message.await()
        val screenState=viewModelClassObject.uiState.value
       assertEquals("network low",errorMessage)
        assertEquals(ComplaintUiState.Idle,screenState)

        coVerify { roomRepository.fetchComplaints(any(),any()) }
    }



    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun sendComplaint_WhenComplaintSendingFails_ShouldShowIdleScreenWithErrorMessage() = runTest {
        dispatcher= StandardTestDispatcher(testScheduler)
        val viewModelClassObject=ComplaintPreviewScreenViewModel(repository,roomRepository,fetchRepository,locationValidator,dispatcher,dispatcher)

        viewModelClassObject.updateComplain("water leakage")
        setFakeLocation(lat = 23.45795, lng = 88.03848, accuracy = 10f)

        coEvery { repository.sendComplain(any()) } returns Result.failure(Exception("time out internet slow try again"))
        coEvery { fetchRepository.fetch(any(),any()) } answers {
            val block=arg<(Location?)->Unit>(0)
            block(fakeLocation)
        }
        every { locationValidator.validate(any(),any(),any(),any()) } answers {
            val block= arg<(Location,Boolean)-> Unit>(1)
            block(fakeLocation,false)
        }
        coEvery { roomRepository.fetchComplaints(any(),any()) } returns Result.success(emptyList())

        val isInside=false
        viewModelClassObject.fetchLocation(isInside)
        val message=async {
            viewModelClassObject.snackbarEvent.first()
        }
        viewModelClassObject.sendComplain()

        advanceUntilIdle()

        val errorMessage=message.await()
        val screenState=viewModelClassObject.uiState.value
        assertEquals("time out internet slow try again",errorMessage)
        assertEquals(ComplaintUiState.Idle,screenState)
        coVerify { repository.sendComplain(any()) }
    }



}
