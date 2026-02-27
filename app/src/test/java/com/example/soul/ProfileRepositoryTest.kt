package com.example.soul

import com.example.demo.complaintApp.ComplaintDataRoom
import com.example.demo.complaintApp.ProfileDataFetchRemoteSource
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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
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
        coVerify { daoProfile.getUser(userId) }
    }

    @Test
    fun  fetchProfileData_whenUserInRoom_skipsRemoteAndReturnsSuccess()= runTest{
        val profileRepository=instance(testScheduler)
        val userId="id34"
        every { backendComplaint.uidFlow } returns flowOf(userId)
        coEvery { daoProfile.getUser(any()) } returns ProfileRoom.ProfileEntity(uid = userId)

       val result= profileRepository.fetchProfileData()

        assertEquals(UserProfileDataStateRepository.Success,result)
        coVerify(exactly = 1) { daoProfile.getUser(userId) }
        coVerify(exactly = 0) { backendProfile.userDataProfileFetch(any()) }
    }












}
