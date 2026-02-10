//package com.example.soul
//
//import androidx.room.Dao
//import javax.inject.Inject
//
//class RepositorySavedState  @Inject constructor(private val dao: DaoImplement){
//
//
//   suspend fun inserted(input:Login2){
//       dao.insert(LoginTable(id = 1, userName = input.userName))
//    }
//
//    suspend fun display(): LoginTable? {
//        return dao.getData()
//    }
//}
