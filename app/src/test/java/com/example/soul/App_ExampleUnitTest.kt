//package com.example.soul
//
//import io.mockk.coEvery
//import io.mockk.mockk
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.test.StandardTestDispatcher
//import kotlinx.coroutines.test.TestDispatcher
//import kotlinx.coroutines.test.advanceUntilIdle
//import kotlinx.coroutines.test.runCurrent
//import kotlinx.coroutines.test.runTest
//import org.junit.Assert.assertEquals
//import org.junit.Before
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.junit.runners.JUnit4
//
//
///**
// * Example local unit test, which will execute on the development machine (host).
// *
// * See [testing documentation](http://d.android.com/tools/testing).
// */
////@get:Rule
////val mainDispatcherRule = MainDispatcherRule() <- use when we not want to pass parameter in viewmodel
//
//@RunWith(JUnit4::class)// opt because its default
//class ExampleUnitTest {
//
//    private lateinit var testDispatcher: TestDispatcher
//
//    private lateinit var repo:FirstAppModuleRepository
//    private lateinit var room:ReportsRepoRoom
//    private lateinit var locaiton: LocationFetcher
//    private lateinit var validator:LocationValidator
//    private lateinit var viewModelClass: PreviewScreenViewModelClass
//
//   // val handle = SavedStateHandle(mapOf("id" to "123"))
//    @Before
//    fun setBeforeTest(){
//        repo= mockk()
//        room= mockk(relaxed =true)
//        locaiton= mockk()
//        validator= mockk()
//
//      // viewModelClass= PreviewScreenViewModelClass(repository = repo, userRepoComplint = room, fetcher = locaiton, validator = validator, mainDispatcher = testDispatcher, ioDispatcher = testDispatcher)
//
//    }
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @Test
//    fun sendComplaintTestViewModel()= runTest{// its make a Test scheduler object and we ca control it by runCurrent() and AdvanceUnitlIdle()
//    testDispatcher = StandardTestDispatcher(testScheduler)// attach Test scheduler  with the dispatch
//    viewModelClass= PreviewScreenViewModelClass(repository = repo, userRepoComplint = room, fetcher = locaiton, validator = validator, mainDispatcher = testDispatcher, ioDispatcher = testDispatcher)
//
//
//
//
//        coEvery { repo.sendComplain(any())} returns Result.success("1222333")
//        //coEvery { locaiton.fetch() }
//
//    viewModelClass.sendComplain()
//
// //   advanceUntilIdle()
//   // runCurrent()
//
//    val state= viewModelClass.uiState.value
//
//    assertEquals( ComplaintUiState.PriorityIncrease,state)
//
//    }
//
//
//}
//
//
//
//
//
//
//
////coVerify { viewModelClass.sendComplain() }\\\ ye galt hai essa mockk wala class ka method run karo
//
//// verify { viewModelClass.sendComplain() } ye bhi galat essa mockk wala class ka method run karo
//
//
////
////    Data important ho to:
////~/AndroidStudioProjects/Soul/app/src/main/res/
////    •	match { }
////    •	capture(slot)
//
////	UnconfinedTestDispatcher vs StandardTestDispatcher
//
//
//
//
//
//
//
//
////
////Class reference aata hai framework se;
////method list banti hai Runner ke reflection se;
////execution hota hai JVM/ART se.
////Koi code likha ya generate nahi hota — sirf data pass hota hai.
//
////Gradle/JUnit runner ko kaunsi class chalani hai batata hai.
////@RunWith runner ko kaise chalani hai batata hai.
////
////
//// poora production app run nahi hota
////  sirf wahi production code run hota hai
////jo tum test ke andar explicitly CALL karte ho
//
////
////
////Haan — pehle Runner “define” hota hai (via annotation),
////phir sab code bytecode me convert hota hai,
////phir JVM un bytecode classes ko execute karta hai.