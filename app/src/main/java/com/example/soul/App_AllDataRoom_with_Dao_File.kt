package com.example.soul

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

class ProfileRoom{



    @Entity(tableName = "profile_table")
    data class ProfileEntity (
        @PrimaryKey val uid: String,
        val name:String = "",
        val rollNo:String="",
        val phone:String="",
        val branch:String=""
    )



    /*
    "SELECT * FROM profile_table WHERE uid = :uid LIMIT 1"
    .......
    Room, mujhe profile_table table  ki wo row dedoo
jisme column uid ka value is function ke parameter se match karta hai.”
     */
    @Dao
    interface ProfileQueries{

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertProfile(dataProfile:ProfileEntity)


        @Query("SELECT * FROM profile_table WHERE uid = :uid LIMIT 1")
        suspend fun getUser(uid: String): ProfileEntity?// check ki ue user hai ya nhi or uid pass kar rahe hai bask abhi aak user hai lekin baad ke liye multi account
        // or switch karte time hum pahle wale account ke data ko delete kar dege table se , ye method kaam ayaega in multiple account like child or parent


        @Query("SELECT * FROM profile_table WHERE uid = :uid LIMIT 1")
        fun observeUser(uid: String): Flow<ProfileEntity?>
      //  @Query("SELECT * FROM profile_table LIMIT 1")//Multi-account apps me observeUser(uid) required hota hai.//FirebaseAuth UID multi-account UI switching ke liye design nahi hai.
      //  fun observeUser(): Flow<ProfileEntity?>//
// ☘️ flow ka use karege to ye chalta rahe ga or repo kabhi bhi return nhi kar payega
        /*

        dao.observeUser().collect { user ->
        if (user == null) ...
    }, ye use kiya to bina ui ke open hua bhi observer start rahega efficieny dicrease
         */
        /*
        But repository me:

❌ tum continuous value nahi chaho

✔ tum ek value chaho (at that moment)

Isliye getUser() necessary hai.
         */


        @Query("DELETE FROM profile_table WHERE uid = :uid")
        suspend fun deleteUserProfileData(uid: String)
    }



}

// App
@Database(entities = [ProfileRoom.ProfileEntity::class,ComplaintDataRoom.ComplaintEntity::class],version=1, exportSchema = false)// yaha par room compiler entity ke metadata ko dekh kar unka sql quries bana kar add kar deta hai @Database ke java class mei
// or Dao ke java class jisme room pahle se hie sql queries ko likha hota hai , to eske obejct ko return kar wata hai AddDatabase ke java class ke aak mehtod se jo hai yaha par fun profileQueris() se
abstract class AppDataBase:RoomDatabase(){// lekin insert or get or other ke work ke sql Dao_Impl java code mei hote hai
    abstract fun profileQueries():ProfileRoom.ProfileQueries// jab room.databasebuilder().build() jo object deta hai AppDataBase_Imple ka ussa hum profileQueries() method ko call karte hai or Dao_Imple java class ka instance lete hia or eske andar sql quries ko run karte ha
    abstract fun complaintQueries():ComplaintDataRoom.ComplaintDao
}// or jab phali baar jab koie dao Querie run hoti hai to table create hoti hai




class ComplaintDataRoom{

    @Entity(
        tableName = "complaints",
        indices = [
            Index(value = ["userId"])   // <-- FAST lookup for user complaints
        ]
    )
    data class ComplaintEntity(
        @PrimaryKey val id: String = "",
        val complain: String = "",
        val description: String = "",
        val timestamp: Long = 0L,
        val address: String = "",
        val status: String = "",
        val userId: String = ""
    )




    @Dao
    interface ComplaintDao {
// check userid data hai ya nhi // esme COUNT(*) to ye int return karegi ki kitni hai data
        @Query("SELECT COUNT(*) FROM complaints WHERE userId = :uid")
        suspend fun countUserComplaints(uid: String): Int

        // Insert list
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertAll(list: List<ComplaintEntity>)

        // Observe all complaints for a user
        @Query("SELECT * FROM complaints WHERE userId = :uid ORDER BY timestamp DESC")
        fun observeComplaints(uid: String): Flow<List<ComplaintEntity>>



        // Insert one complaint
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertComplaint(item: ComplaintEntity)





        // Check if complaint exists
        @Query("SELECT * FROM complaints WHERE id = :id LIMIT 1")
        suspend fun getComplaint(id: String): ComplaintEntity?

        /*
        Soul, abhi ke flow me getComplaint(id) ki zarurat nahi hai,
kyunki tum complaint send karne ke turant baad usi ID ka data Firebase se fetch kar rahe ho.
Lekin future me duplicate prevention, offline fallback, re-syncing, or real-time updates ke liye ye method bahut important hoga.
         */


        // Delete on logout (optional but recommended)
        @Query("DELETE FROM complaints WHERE userId = :uid")
        suspend fun deleteUserComplaints(uid: String)
    }
}

















/*
🌟 6️⃣ PERFECT ANALOGY (Soul special)

observeUser()

“CCTV camera chal raha hai → jo bhi movement hogi, mujhe bata dena.”

getUser(uid)

“Ek baar batao, room ke andar koi hai ya nahi?”

Do alag kaam.
Dono necessary.
 */