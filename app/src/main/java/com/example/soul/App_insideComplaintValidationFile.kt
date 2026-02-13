package com.example.soul

import android.location.Location
import android.util.Log

// village
//26.50493° N, 83.87572

//26.50551° N, 83.87486
//26.50674° N, 83.87319° E


data class Zone(
    val latitude: Double,
    val longitude: Double,
    val radius:Float

)


class Location2(
    val latitude: Double =0.0,
    val longitude:Double=0.0,
    val accuracy:Float=0f
)

val DepartmentsBuilding= mapOf( "CSIT" to Zone(latitude =26.50493 , longitude =83.87572, radius = 100f  )
    ,"ME" to Zone(latitude =26.50551 , longitude =83.87486, radius = 80f  ) ,
    "EE" to Zone(latitude =26.50674 , longitude = 83.87319, radius = 80f ))
////fun checkBuilding(location:Location,building:String):Boolean{
//    return  true
//}, for testing
suspend fun checkBuilding(location:Location,building:String, buildNotMatch: suspend (String)->Unit):Boolean{
    Log.e("checkNL"," check locaiton -> $location")
    val buildingZone= DepartmentsBuilding[building]?: run {
        buildNotMatch("building not present in list")
        return false
    }

    val result= floatArrayOf(1F)


    Location.distanceBetween(
        buildingZone.latitude,
        buildingZone.longitude,
        location.latitude,
        location.longitude,
        result
    )

    val distanceBetweenTwoLocation= result[0]
    Log.e("TestLocation","value is $distanceBetweenTwoLocation")
    if(distanceBetweenTwoLocation<= buildingZone.radius){
        return true
    }

    val distanceBetweenRadiusAndLocation= distanceBetweenTwoLocation - buildingZone.radius
    Log.e("TestLocation","value is $distanceBetweenRadiusAndLocation")

    val nearOutsideRange=25f
    val accuracyCap=25f
    if(distanceBetweenRadiusAndLocation > nearOutsideRange){
        return false
    }

    Log.e("TestLocation"," accuracuy cap check ")

    return location.accuracy <= accuracyCap


//
//    val effectiveRadius = buildingZone.radius + location.accuracy
//    return result[0] <= effectiveRadius
}



enum class DecisionInside{
    Reject,
    Accept
}


fun validateInsideOldComplaints(old:List<FirstAppFireStoreDataClass>):DecisionInside{

    return if(old.isEmpty()){
        DecisionInside.Accept
    }
    else{
        DecisionInside.Reject
    }

}