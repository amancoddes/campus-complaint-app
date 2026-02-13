
package com.example.soul

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CheckHashCreation{


    private lateinit var message:String

   @Before
   fun set(){
       message=""
   }

    @Test
    fun checkHashCreation() = runTest{
        //var message:String=

        val hash= makeHash(mode = Mode.OUTDOOR,"csi&*^t", " ", floor = "", onError = {
            message = it
        })
        assertEquals("Title required",message)
    }


@Ignore(" not here")
    @Test
    fun checkHashCreation2() = runTest{
        //var message:String=

        val hash= makeHash(mode = Mode.OUTDOOR,"45.45_67.555", "", floor = "", onError = {
            message = it
        })
        assertEquals("Title required",message)
    }




}