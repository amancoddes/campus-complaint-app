package com.example.soul

import app.cash.turbine.test
import com.example.demo.complaintApp.ComplaintDataRoom
import com.example.demo.complaintApp.ProfileDataFetchRemoteSource
import com.example.demo.complaintApp.ProfileFetchRoom
import com.example.demo.complaintApp.ProfileRepository
import com.example.demo.complaintApp.ProfileRoom
import com.example.demo.complaintApp.UserComplaintsReadRepository
import com.example.demo.complaintApp.UserData
import com.example.demo.complaintApp.UserProfileData
import com.example.demo.complaintApp.UserProfileDataStateRepository
import com.example.demo.complaintApp.toEntity

import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.confirmVerified
import io.mockk.every

import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ProfileRepositoryTest {

private val fakeData=UserData(name = "animora", branch ="CSIT", rollNo = "cs89", phone = "89")
    private lateinit var backendProfile: ProfileDataFetchRemoteSource
    private lateinit var daoProfile:ProfileRoom.ProfileQueries
    private lateinit var daoComplaint:ComplaintDataRoom.ComplaintDao
    private lateinit var backendComplaint:UserComplaintsReadRepository

    @Before
    fun setMocks(){
        backendProfile= mockk()
        backendComplaint= mockk()
        daoProfile= mockk()
        daoComplaint= mockk()
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
            dispatcher
        )
    }

    /***
     ------------------------------------------------
     |      fetchProfileData() method tests          |
     ------------------------------------------------
    ***/
    @Test
    fun fetchProfileData_whenUserNotInRoom_fetchesFromRemoteAndInsertsIntoRoom() = runTest {

        val profileRepository=instance(testScheduler)

        val userId = "id69"
        every { backendComplaint.uidFlow } returns flowOf(userId)
        coEvery { daoProfile.getUser(any()) } returns null
        coEvery { backendProfile.userDataProfileFetch(any()) } returns UserProfileData.Success(fakeData)
        coEvery { daoProfile.insertProfile(any()) } just Runs

        val result = profileRepository.fetchProfileData()// call

        coVerifyOrder {
            backendProfile.userDataProfileFetch(any())
            daoProfile.insertProfile(any())
        }

        assertEquals(UserProfileDataStateRepository.Success, result)
        coVerify(exactly = 1) { daoProfile.insertProfile(match {it.uid == userId}) } // check important variable make it refractor friendly
        coVerify(exactly = 1) { backendProfile.userDataProfileFetch(userId) }
    }

    @Test
    fun  fetchProfileData_whenUserInRoom_skipsRemoteAndReturnsSuccess()= runTest{
        val profileRepository=instance(testScheduler)
        val userId="id34"
        every { backendComplaint.uidFlow } returns flowOf(userId)
        coEvery { daoProfile.getUser(any()) } returns ProfileRoom.ProfileEntity(uid = userId)

       val result= profileRepository.fetchProfileData()

        assertEquals(UserProfileDataStateRepository.Success,result)
        coVerify{ daoProfile.getUser(userId) }
        coVerify(exactly = 0) { backendProfile.userDataProfileFetch(any()) }
        coVerify(exactly = 0) { daoProfile.insertProfile(any()) }
    }

    @Test
    fun fetchProfileData_whenUidNull_returnsLogin()= runTest{


        every { backendComplaint.uidFlow } returns flowOf(null)
        val profileRepository=instance(testScheduler)
       val result=profileRepository.fetchProfileData()

        assertEquals(UserProfileDataStateRepository.Login,result)
        coVerify(exactly = 0){ daoProfile.getUser(any()) }
        coVerify(exactly = 0) { backendProfile.userDataProfileFetch(any()) }
        coVerify(exactly = 0) { daoProfile.insertProfile(any()) }
    }


    @Test
    fun fetchProfileData_whenRemoteNotFound_returnNotFound()= runTest{
        val profileRepository=instance(testScheduler)

        val userId = "id69"
        every { backendComplaint.uidFlow } returns flowOf(userId)
        coEvery { daoProfile.getUser(any()) } returns null
        coEvery { backendProfile.userDataProfileFetch(any()) } returns UserProfileData.NotFound

      val result=  profileRepository.fetchProfileData()
        assertEquals(UserProfileDataStateRepository.NotFound("user data not found add user data"),result)
        coVerify{ daoProfile.getUser(userId) }
        coVerify(exactly = 1) { backendProfile.userDataProfileFetch(userId) }
        coVerify(exactly = 0) { daoProfile.insertProfile(any()) }
    }


    @Test
    fun fetchProfileData_whenRemoteErrorReturn_returnError()= runTest{
        val profileRepository=instance(testScheduler)

        val userId = "id69"
        every { backendComplaint.uidFlow } returns flowOf(userId)
        coEvery { daoProfile.getUser(any()) } returns null
        coEvery { backendProfile.userDataProfileFetch(any()) } returns UserProfileData.Error("not connect network")

        val result=  profileRepository.fetchProfileData()
        assertEquals(UserProfileDataStateRepository.Error("not connect network"),result)
        coVerify{ daoProfile.getUser(userId) }
        coVerify(exactly = 1) { backendProfile.userDataProfileFetch(userId) }
        coVerify(exactly = 0) { daoProfile.insertProfile(any()) }
    }



    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun fetchProfileData_parallelCalls_shouldRemoteCalsOnce()= runTest {

        val profileRepository=instance(testScheduler)

        var isInserted=false
        val userId = "id69"
        every { backendComplaint.uidFlow } returns flowOf(userId)
        coEvery { daoProfile.getUser(any()) } coAnswers {
            if(isInserted){
                ProfileRoom.ProfileEntity(uid = userId)
            }
            else{
                null
            }
        }

        coEvery { backendProfile.userDataProfileFetch(any()) } coAnswers {
            isInserted=true
            UserProfileData.Success(fakeData)
        }

        coEvery { daoProfile.insertProfile(any()) } just Runs

        launch { profileRepository.fetchProfileData() }
        launch { profileRepository.fetchProfileData() }

        advanceUntilIdle()

        coVerify(exactly = 1) { backendProfile.userDataProfileFetch(userId) }
        coVerify(exactly = 1) { daoProfile.insertProfile(match { it.uid==userId }) }
    }






    /***
    ------------------------------------------------
    |      observeUserInfo()         |
    ------------------------------------------------
     ***/


    @Test
    fun observeUserInfo_whenUserDataNotExist_shouldEmitsLoadingAndEmpty()= runTest{
        val profileRepository=instance(testScheduler)
        val userId = "id69"
        every { backendComplaint.uidFlow } returns flowOf(userId)
        coEvery { daoProfile.observeUser(any()) } returns flowOf(null)
        profileRepository.observeUserInfo().test {
            assertEquals(ProfileFetchRoom.Loading,awaitItem())
            assertEquals(ProfileFetchRoom.Empty,awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        coVerify(exactly = 1) { daoProfile.observeUser(uid = userId) }
    }



    @Test
    fun observeInfo_whenUserNotLogin_shouldEmitsNotLogin()= runTest {

        val profileRepository=instance(testScheduler)
        every { backendComplaint.uidFlow } returns flowOf(null)
        profileRepository.observeUserInfo().test {
            assertEquals(ProfileFetchRoom.NotLogin,awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        coVerify (exactly = 0){ daoProfile.observeUser(any()) }
    }

    //val dbFlow = MutableSharedFlow<ProfileRoom.ProfileEntity?>(replay = 1)
    @Test
    fun observeInfo_whenUserDataExist_shouldEmitsLoadingAndSuccess()= runTest {
        val profileRepository=instance(testScheduler)
        val userId = "id69"
        every { backendComplaint.uidFlow } returns flowOf(userId)
        coEvery { daoProfile.observeUser(any()) } returns flowOf(ProfileRoom.ProfileEntity(uid = userId) )

        profileRepository.observeUserInfo().onEach { println(" -=-- $it") }.test {
            assertEquals(ProfileFetchRoom.Loading,awaitItem())
            val result=awaitItem() as ProfileFetchRoom.Success
            assertEquals(userId,result.data.uid)
            cancelAndIgnoreRemainingEvents()
        }
        coVerify (exactly = 1){ daoProfile.observeUser(userId) }
    }


    @Test
    fun observeInfo_whenRoomThrowError_shouldEmitsLoadingAndError()= runTest {// inner room exception wrap so now the upper flow will not terminated
        val profileRepository=instance(testScheduler)
        val userId = "id69"
        every { backendComplaint.uidFlow } returns flowOf(userId)
        coEvery { daoProfile.observeUser(any()) } returns flow {// use  there flow instead of flowOf because flowOf only emt not throw  error so we manually throw it
            throw RuntimeException("DB error")
        }

        profileRepository.observeUserInfo().test {
            assertEquals(ProfileFetchRoom.Loading, awaitItem())

            val result =  awaitItem() as ProfileFetchRoom.Error
            assertEquals("DB error",result.message)
            cancelAndIgnoreRemainingEvents()
        }
        coVerify(exactly = 1){ daoProfile.observeUser(userId) }
    }

    @Test
    fun observeInfo_whenSameDataEmitsTwice_shouldEmitsOnlyOnce()= runTest {
        // This test protects the distinctUntilChanged() behaviour because its stop emitting of same value more than one time
    // If distinctUntilChanged() is removed from the repository flow
    // duplicate Success emissions may occur when Room emits the same entity twice
    // This test ensures the repository emits Success only once
        // imp -> make sure use data classes ...
        val profileRepository=instance(testScheduler)
        val userId = "id69"
        every { backendComplaint.uidFlow } returns flowOf(userId)
        val dbFlow = MutableSharedFlow<ProfileRoom.ProfileEntity?>()

        coEvery { daoProfile.observeUser(any()) } returns dbFlow
        profileRepository.observeUserInfo().test {
            assertEquals(ProfileFetchRoom.Loading, awaitItem())// awaitItem() its return the value from turbine queue
            val  entity1=ProfileRoom.ProfileEntity(uid = userId)
            dbFlow.emit(entity1)
            assertTrue(awaitItem() is ProfileFetchRoom.Success)
            val  entity2=ProfileRoom.ProfileEntity(uid = userId)
            dbFlow.emit(entity2)// its drop by the distinctUnitChanged() so not need awaitItem
            expectNoEvents()// its check is turbine queue isEmpty
            cancelAndIgnoreRemainingEvents()
        }
        coVerify(exactly = 1){ daoProfile.observeUser(userId) }
    }



    @Test
    fun observerInfo_whenUpdateEmptyRoom_shouldEmitsLoadingEmptyThenSuccess()= runTest {
        val profileRepository=instance(testScheduler)
        val userId = "id69"
        val dbFlow = MutableSharedFlow<ProfileRoom.ProfileEntity?>()
        every { backendComplaint.uidFlow } returns flowOf(userId)
        coEvery { daoProfile.observeUser(any()) } returns dbFlow
        profileRepository.observeUserInfo().test {
            assertEquals(ProfileFetchRoom.Loading, awaitItem())

            dbFlow.emit(null)
            assertEquals(ProfileFetchRoom.Empty, awaitItem())

            val  entity1=ProfileRoom.ProfileEntity(uid = userId)
            dbFlow.emit(entity1)
            val result=awaitItem() as ProfileFetchRoom.Success
            assertEquals(userId,result.data.uid)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
        coVerify(exactly = 1) { daoProfile.observeUser(uid = userId) }
    }

    // old cancel test write when use multiple account switching from the profile screen









}
