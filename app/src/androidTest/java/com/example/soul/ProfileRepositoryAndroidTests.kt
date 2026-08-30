package com.example.soul
import org.junit.Assert.assertEquals
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.demo.complaintApp.AppDataBase
import com.example.demo.complaintApp.ComplaintDataRoom
import com.example.demo.complaintApp.ProfileDataFetchRemoteSource
import com.example.demo.complaintApp.ProfileRepository
import com.example.demo.complaintApp.ProfileRoom
import com.example.demo.complaintApp.UserComplaintsReadRepository
import com.example.demo.complaintApp.UserData
import com.example.demo.complaintApp.UserProfileData
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import app.cash.turbine.test
import com.example.demo.complaintApp.DataStoreManager
import com.example.demo.complaintApp.ProfileFetchRoom
import com.example.demo.complaintApp.UserProfileDataStateRepository
import com.example.demo.complaintApp.toEntity
import io.mockk.coVerify
import org.junit.After

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */



//  its defien konsa Runner class use karna hai agar nhi lagaya hai to default Runner use hota hai
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    private lateinit var backendProfile: ProfileDataFetchRemoteSource
    private lateinit var daoProfile: ProfileRoom.ProfileQueries
    private lateinit var daoComplaint: ComplaintDataRoom.ComplaintDao
    private lateinit var backendComplaint: UserComplaintsReadRepository
    private lateinit var db:AppDataBase
    private lateinit var dataStoreManager: DataStoreManager


    @Before
    fun setup() {
        backendProfile= mockk()
        backendComplaint= mockk()
        dataStoreManager= mockk()
       db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDataBase::class.java
        )
            .allowMainThreadQueries()
            .build()

        daoProfile=db.profileQueries()
        daoComplaint=db.complaintQueries()
    }

    @After
    fun tearDown() {
        db.close()
    }


    private fun instance(testScheduler: TestCoroutineScheduler):ProfileRepository{
        val dispatcher = StandardTestDispatcher(testScheduler)
        return ProfileRepository(
            dao = daoProfile,
            fireRepo = backendProfile,
            mutex = Mutex(),
            dao2 = daoComplaint,
            repo = backendComplaint,
            dispatcher,
            dispatcher,
            dataStoreManager
        )
    }
    private val fakeData= UserData(name = "animora", branch ="CSIT", rollNo = "cs89", phone = "89")

    @Test
    fun fetchProfileData_whenRemoteSuccess_observerEmitsSuccess()= runTest {

        val userId = "id69"
        every { backendComplaint.uidFlow } returns flowOf(userId)
        coEvery { backendProfile.userDataProfileFetch(any()) } returns UserProfileData.Success(fakeData)

        val profileRepository=instance(testScheduler)

        profileRepository.observeUserInfo().test {

            assertEquals(ProfileFetchRoom.Loading, awaitItem())
            assertEquals(ProfileFetchRoom.Empty,awaitItem())

           val result= profileRepository.fetchProfileData()
            assertEquals(UserProfileDataStateRepository.Success,result)

            val result2=awaitItem() as ProfileFetchRoom.Success
            assertEquals(userId,result2.data.uid)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) {
            backendProfile.userDataProfileFetch(userId)
        }
    }



    @Test
    fun observeUser_whenAlreadyDataExitsInRoom_observerEmitsSuccess()= runTest {

        val userId = "id69"
        every { backendComplaint.uidFlow } returns flowOf(userId)


        //coEvery { daoProfile.insertProfile(fakeData.toEntity(userId)) }
        daoProfile.insertProfile(fakeData.toEntity(userId))
        val profileRepository=instance(testScheduler)
        profileRepository.observeUserInfo().test {

            assertEquals(ProfileFetchRoom.Loading, awaitItem())

            val result2=awaitItem() as ProfileFetchRoom.Success
            assertEquals(userId,result2.data.uid)
            cancelAndIgnoreRemainingEvents()
        }


    }




    @Test
    fun observeUser_whenDataEdit_observerEmitsSuccess()= runTest {

        val fakeDataEdit= UserData(name = "aman", branch ="CSIT", rollNo = "CS05", phone = "89")

        val userId = "id69"
        every { backendComplaint.uidFlow } returns flowOf(userId)
        //coEvery { daoProfile.insertProfile(fakeData.toEntity(userId)) }
        daoProfile.insertProfile(fakeData.toEntity(userId))

        val profileRepository=instance(testScheduler)

        profileRepository.observeUserInfo().test {

            assertEquals(ProfileFetchRoom.Loading, awaitItem())
            // its check and than give success after check room
            val result=awaitItem() as ProfileFetchRoom.Success
            assertEquals(userId,result.data.uid)

// user manually edit the profile
            daoProfile.insertProfile(fakeDataEdit.toEntity(userId))// its store it and than the observeUser method give the success

            val result2=awaitItem() as ProfileFetchRoom.Success
            assertEquals(userId,result2.data.uid)
            assertEquals(fakeDataEdit.name,result2.data.name)


            cancelAndIgnoreRemainingEvents()
        }


    }


    @Test
    fun observeUser_whenRemoteFailure_RoomUnchanged()= runTest {

        val userId = "id69"
        every { backendComplaint.uidFlow } returns flowOf(userId)
        coEvery { backendProfile.userDataProfileFetch(any()) } returns UserProfileData.Error("not connect network")

        val profileRepository=instance(testScheduler)

        profileRepository.observeUserInfo().test {

            assertEquals(ProfileFetchRoom.Loading, awaitItem())
            assertEquals(ProfileFetchRoom.Empty,awaitItem())

            val result= profileRepository.fetchProfileData()
            assertEquals(UserProfileDataStateRepository.Error("not connect network"),result)
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
        coVerify(exactly = 1) {
            backendProfile.userDataProfileFetch(userId)
        }

    }

}









//Run
//→ Gradle
//→ Build
//→ Runtime (JVM / ART)
//→ Runner
//→ Test method
//→ Result