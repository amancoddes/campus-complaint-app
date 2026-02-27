package com.example.soul

import com.example.demo.complaintApp.ComplaintSubmissionRemoteDataSource
import com.example.demo.complaintApp.ComplaintSubmissionRepository
import com.example.demo.complaintApp.FireBaseComplaintSubmissionRemoteDataSource
import com.example.demo.complaintApp.FirstAppFireStoreDataClass
import com.google.common.base.Verify
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ComplaintSubmissionRepositoryTest {

    private lateinit var backedResult:ComplaintSubmissionRemoteDataSource
    private lateinit var repository: ComplaintSubmissionRepository
    @Before
    fun set(){
       backedResult= mockk<ComplaintSubmissionRemoteDataSource>()// proxy class object create which implement interface note interface can not make object
        repository=ComplaintSubmissionRepository(backendRepo = backedResult)
    }

    private fun fakeLocation():FirstAppFireStoreDataClass{
        return FirstAppFireStoreDataClass(complain = "water leakage ")
    }

    @Test
    fun sendComplaint_WhenFirebaseSuccessful_ShouldReturnResultSuccess()= runTest{

        coEvery { backedResult.sendComplaint(any()) } returns Result.failure(Exception("network low"))
        val result=repository.sendComplain(fakeLocation())
        print(result.exceptionOrNull())
        assertTrue(result.isFailure)
        assertEquals("network low",result.exceptionOrNull()?.message)
        coVerify { backedResult.sendComplaint(any()) }
    }

}





//assertEquals(Exception("C"),result.exceptionOrNull()) -> not use it its always failed because both exception() have different instances
//assertEquals(null,result.getOrNull()) -> use to find its run failure or not because when Result fail so getOrNul() give null
//ssertEquals("C", result.exceptionOrNull()?.message) -> use this to match message