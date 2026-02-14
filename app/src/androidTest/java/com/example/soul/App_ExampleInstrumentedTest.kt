package com.example.soul

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */


//
////  its defien konsa Runner class use karna hai agar nhi lagaya hai to default Runner use hota hai
//@RunWith(AndroidJUnit4::class)
//class ExampleInstrumentedTest {
//    //26.50493 , longitude =83.87572
//    @Test// ye Runner ko bata hai ki ye mathod add karna hai
//    fun buildingCheck_returnsTrue_whenInsideRadius() {
//
//
//}
//}


//
//Runner reflection se methods scan karta hai,
//@Test wale methods ko ek internal list me “add” karta hai,
//aur baad me isi list ke methods ko invoke() karke JVM/ART se execute karwata hai.

//
//@Test
//fun useAppContext() {
//    // Context of the app under test.
//    val appContext = InstrumentationRegistry.getInstrumentation().targetContext
//    assertEquals("com.example.soul", appContext.packageName)
//}

//
//1️⃣ Runner ke paas data hota hai
//
//Runner ke paas ye 3 cheezein hoti hain:
//•	testClass (jaise ExampleUnitTest.class)
//•	testMethods ki list (sirf @Test wale)
//•	Runner ka already-written run() method


//
//Run ▶️
//→ Gradle
//→ Build
//→ Runtime (JVM / ART)
//→ Runner
//→ Test method
//→ Result