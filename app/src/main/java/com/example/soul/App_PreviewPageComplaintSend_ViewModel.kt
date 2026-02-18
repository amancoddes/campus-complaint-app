package com.example.soul

import android.location.Location
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soul.di.FirebaseModule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@HiltViewModel
class PreviewScreenViewModelClass @Inject constructor(private val repository: FirstAppModuleRepository, private val userRepoComplint:ReportsRepoRoom,
                                             private val fetcher: LocationFetcher, private val validator: LocationValidator

                                             , @FirebaseModule.MainDispatcher private val mainDispatcher: CoroutineDispatcher,
                                       @FirebaseModule.IoDispatcher private val ioDispatcher: CoroutineDispatcher

):ViewModel() {

    //Complain building
    private val _building = mutableStateOf("")
    val building: State<String> = _building


    // floor

    private val _floor = mutableStateOf("")
    val floor: State<String> = _floor

    fun updateFloor(input: String) {
        _floor.value = input
    }

    fun updateBuilding(input: String) {
        _building.value = input
    }


    ///Address
    private val _address = mutableStateOf("")
    val addressView: State<String> = _address

    fun updateAddress(input: String) {
        _address.value = input
    }


    ////Description
    private val _description = mutableStateOf("")
    val descriptionView: State<String> = _description


    fun updateDescription(input: String) {
        _description.value = input
    }


    //Complain  title
    private val _complain = mutableStateOf("")
    val complainView: State<String> = _complain

    fun updateComplain(input: String) {
        _complain.value = input
    }


    private val _snackbarEvent = MutableSharedFlow<String>(extraBufferCapacity = 1, replay = 0)
    val snackbarEvent = _snackbarEvent

    private val _uiState = MutableStateFlow<ComplaintUiState>(ComplaintUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun sendComplain() =viewModelScope.launch(mainDispatcher) {

        _uiState.value = ComplaintUiState.Loading
        println(" send 1 before launch  😖")






            if (_complain.value.isBlank()){
                _uiState.value = ComplaintUiState.Idle
                _snackbarEvent.emit("choose complain type")
                return@launch
            }

            val loc = _location.value ?: run {
                _uiState.value = ComplaintUiState.Idle
                _snackbarEvent.emit("some thing wrong location not fetch")
                return@launch
            }



//            if (!isInsideCollege(loc)) {
//                viewModelScope.launch {
//                    _snackbarEvent.emit("you outside the campus")
//                }
//                return
//            }

            val confidence = getOutdoorConfidence(loc.accuracy)
            if (confidence == Confidence.REJECT) {
                _uiState.value = ComplaintUiState.Idle
                _snackbarEvent.emit("Location signal is weak. Please move to an open area and try again  ")
                return@launch
            }


            val titleKeyList = giveTieKeys(
                loc.latitude, loc.longitude,
                confidence = confidence,
                accuracy = loc.accuracy
            )


            if (titleKeyList.isEmpty()) {
                _uiState.value = ComplaintUiState.Idle
                _snackbarEvent.emit("Location signal is weak. Please move to an open area and try again..")
                return@launch
            }

            val hashList = hashCreate(titleKeyList, title = _complain.value, onError = {
                _snackbarEvent.emit(it)
            }, mode = Mode.OUTDOOR)
            if (hashList.isEmpty())
            {
                _uiState.value=ComplaintUiState.Idle
                _snackbarEvent.emit("something wrong :Location")
                return@launch
            }

            val cutoffTime = System.currentTimeMillis() - (14L * 24 * 60 * 60 * 1000)

            val storeComplainLists = withContext(ioDispatcher) {
                userRepoComplint.fetchComplaints(hashList, cutoffTime)
            }

        val decision = storeComplainLists.fold(
            onSuccess = { complaints ->
              decideComplaintAction(
                    candidates = complaints,
                    newLat = loc.latitude,
                    newLng = loc.longitude,
                    newConfidence = confidence
                )
            },
            onFailure = {
                _uiState.value=ComplaintUiState.Idle
                _snackbarEvent.emit(it.message?:"some thing wrong try again")
                println(" filaure 🥸")
                return@launch

            }
        )


            when (decision) {
                Decision.REJECT -> {
                    _uiState.value = ComplaintUiState.Idle
                    _snackbarEvent.emit("Same complaint already exists nearby")
                }

                Decision.ALLOW_NEW -> {

                    val centerTile = buildCenterKey(loc.latitude, loc.longitude, 4)
                    val complainPincode = makeHash(
                        mode = Mode.OUTDOOR,
                        tileKey = centerTile,
                        title = _complain.value,
                        onError = {
                            _snackbarEvent.emit(it)
                        })

                    val dataComplain = complainPincode?.let {
                        FirstAppFireStoreDataClass(
                            complain = _complain.value,
                            description = _description.value,
                            address = _address.value,
                            latitude = loc.latitude,
                            longitude = loc.longitude,
                            confidence = confidence,
                            hash = it,
                            accuracy = loc.accuracy,
                            mode = Mode.OUTDOOR
                        )
                    } ?: run {
                        _uiState.value = ComplaintUiState.Idle
                        _snackbarEvent.emit("something wrong try again")
                        return@launch
                    }



                    val result = withContext(ioDispatcher) {
                        println("send complain 🎃")
                        repository.sendComplain(dataComplain)
                    }


                    result.fold(
                        onSuccess = { id ->

                            _uiState.value = ComplaintUiState.Success(id)
                            userRepoComplint.fetchNewComplaint(id)
                            return@launch
                        },
                        onFailure = { e ->

                            _uiState.value = ComplaintUiState.Idle
                            _snackbarEvent.emit(e.message?:"something wrong")
                            print(" backend fail 🎃")
                            return@launch
                        }
                    )
                }


            }


            _uiState.value = ComplaintUiState.Idle

            return@launch// change here



}









//    private val fetcher = LocationFetcher(context)
//    private val validator = LocationValidator()


    private val _location = MutableStateFlow<Location?>(null)
    val location = _location.asStateFlow()
  //  private val _indoor = MutableStateFlow(true)
  //  val indoor = _indoor.asStateFlow()


    fun fetchLocation(inside:Boolean=false) {
        validator.reset()   //  reset
        fetchLocationInternal(inside)
    }

    private fun fetchLocationInternal(inside: Boolean) {
        println(" fetch location internal again run ☘️")
        _uiState.value = ComplaintUiState.Loading


        fetcher.fetch(
            onResult = { loc ->
                if (loc != null) {
                    validator.validate(
                        loc,
                        accept = { x:Location,y:Boolean ->
                            _location.value = x
                          //  _indoor.value = y
                            _uiState.value = ComplaintUiState.Idle
                            viewModelScope.launch(mainDispatcher) {
                                _snackbarEvent.emit("location fetch successfully")
                            }
                            println(" accept run 😖")
                                 },
                        retry = {
                            viewModelScope.launch(mainDispatcher) {
                                delay(900)        // delay add because fast request can can often flp error
                                print(" call retry 😚")
                                fetchLocationInternal(inside)
                            } },
                        showError = {
                            viewModelScope.launch(mainDispatcher) {
                                _uiState.value = ComplaintUiState.Idle
                                _snackbarEvent.emit("Location accuracy is low Try again")
                            }
                        }
                        ,
                        inside = inside
                    )
                }else{
                    viewModelScope.launch(mainDispatcher) {
                        print("else run 😃😖🥰")
                        _uiState.value=ComplaintUiState.Idle
                        _snackbarEvent.emit("some thing wrong location not precise")
                    }
                }
            },
            onError = { error ->
                viewModelScope.launch(mainDispatcher) {
                    _uiState.value = ComplaintUiState.Idle
                    _snackbarEvent.emit(error.message ?: "Location failed")
                }
            }

        )
    }

    override fun onCleared() {
        fetcher.cancel()
    }


    fun cancel(){
        fetcher.cancel()
    }

    /*
    	Latitude → 28.36993° N
 North–South position (upar–neeche)
	•	Longitude → 79.45669° E
 East–West position (daaye–baaye)
Latitude hamesha pehle, Longitude baad me
(Lat, Lng)
     */
   // 26.50490° N, 83.87571°
   // private val collegeLatitude =26.50490
  //  private val collegeLongitude =83.87571
//23-1-26
  //  private val collegeRadiusMeter = 300f


//    private fun isInsideCollege(location: Location): Boolean {
//        val result = FloatArray(1)
//
//        Location.distanceBetween(
//            location.latitude,
//            location.longitude,
//            collegeLatitude,
//            collegeLongitude,
//            result
//        )
//
//        val distance = result[0]   // meters
//
//        return distance <= collegeRadiusMeter
//    }


    fun insideSendComplain()=viewModelScope.launch(mainDispatcher) {
        _uiState.value = ComplaintUiState.Loading

        Log.e("validateInside"," start complaint validation")
        val loc = _location.value ?: run {
            _uiState.value = ComplaintUiState.Idle
            _snackbarEvent.emit("some thing wrong location not fetch null")
            return@launch
        }
Log.e("location22"," the ${loc.latitude} and ${loc.longitude}")
val building=_building.value.trim()
        if(building.isBlank()){
               _uiState.value = ComplaintUiState.Idle
                _snackbarEvent.emit("enter the building name")
            return@launch
        }
val check = checkBuilding(location = loc, building = _building.value, buildNotMatch = {
    _snackbarEvent.emit(it)
})



        if(!check){
            _uiState.value = ComplaintUiState.Idle

            _snackbarEvent.emit("you are not inside ${_building.value}")
            return@launch
        }

            val hashInside =
                makeHash(mode = Mode.INDOOR, tileKey = _building.value, title = _complain.value, floor = _floor.value,onError = {
                    _snackbarEvent.emit(it)
                }
                )

            if(hashInside==null) {
                _uiState.value = ComplaintUiState.Idle
                _snackbarEvent.emit("something wrong")
                return@launch
            }
            val tilesBackendInsideComplaint = withContext(ioDispatcher) {
                    userRepoComplint.fetchInsideTileKeys(hash = hashInside)
            }


        val decisionInside=tilesBackendInsideComplaint.fold(
            onSuccess = { oldComplaint->
                validateInsideOldComplaints(old = oldComplaint)
            },
            onFailure = {
                _uiState.value=ComplaintUiState.Idle
                _snackbarEvent.emit(it.message?:"some thing wrong")
                return@launch
            }
        )

            when(decisionInside){
                DecisionInside.Accept->{

                    val dataComplainInside = FirstAppFireStoreDataClass(
                        complain = _complain.value,
                      //  description = _description.value,
                        address = _building.value,
                       // latitude = loc.latitude,
                      //  longitude = loc.longitude,
                        mode = Mode.INDOOR,
                        hash = hashInside,
                      //  accuracy = loc.accuracy,
                        //mode = Mode.OUTDOOR
                    )

                    val resultInside = withContext(ioDispatcher) {
                        repository.sendComplain(dataComplainInside)
                    }


                    resultInside.fold(
                        onSuccess = { id ->

                            _uiState.value = ComplaintUiState.Success(id)
                            userRepoComplint.fetchNewComplaint(id)
                            return@launch
                        },
                        onFailure = { e ->

                            _uiState.value = ComplaintUiState.Idle
                            _snackbarEvent.emit( "error : $e")

                            return@launch
                        }
                    )

                }

                DecisionInside.Reject -> {
                    _uiState.value = ComplaintUiState.PriorityIncrease
                    _snackbarEvent.emit(  " complaint already exist we increase one priority ")
                    return@launch
                }
            }



    }

}


sealed class ComplaintUiState {

    data object Idle : ComplaintUiState()

    data object Loading : ComplaintUiState()

    data class Success(val documentId: String) : ComplaintUiState()

    data class Error(val message: String) : ComplaintUiState()

    data object PriorityIncrease : ComplaintUiState()
}



