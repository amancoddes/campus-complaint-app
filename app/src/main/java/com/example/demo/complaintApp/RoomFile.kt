package com.example.demo.complaintApp

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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



    @Dao
    interface ProfileQueries{
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertProfile(dataProfile:ProfileEntity)


        @Query("SELECT * FROM profile_table WHERE uid = :uid LIMIT 1")
        suspend fun getUser(uid: String): ProfileEntity?

        @Query("SELECT * FROM profile_table WHERE uid = :uid LIMIT 1")
        fun observeUser(uid: String): Flow<ProfileEntity?>



        @Query("DELETE FROM profile_table WHERE uid = :uid")
        suspend fun deleteUserProfileData(uid: String)
    }



}


val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {

        db.execSQL(
            "ALTER TABLE complaints ADD COLUMN updatedTime INTEGER NOT NULL DEFAULT 0"
        )
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "ALTER TABLE complaints ADD COLUMN url TEXT"
        )
    }
}



val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "ALTER TABLE complaints ADD COLUMN resolvedImageUrl TEXT"
        )
    }
}

// version increse for updateTime
@Database(entities = [ProfileRoom.ProfileEntity::class,ComplaintDataRoom.ComplaintEntity::class],version=4, exportSchema = false)
abstract class AppDataBase:RoomDatabase(){
    abstract fun profileQueries():ProfileRoom.ProfileQueries// its return the object which type ProfileQueries so its means it can return the class obejct which implement this
    abstract fun complaintQueries():ComplaintDataRoom.ComplaintDao
}



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
        val userId: String = "",
        val updatedTime:Long=0L,
        val url:String?=null,
        val resolvedImageUrl: String?=null
    )




    @Dao
    interface ComplaintDao {
// check userid data hai ya nhi // esme COUNT(*) to ye int return karegi ki kitni hai data
        @Query("SELECT COUNT(*) FROM complaints WHERE userId = :uid")
        suspend fun countUserComplaints(uid: String): Int

        // Insert list
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertAll(list: List<ComplaintEntity>)

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insert(complaint: ComplaintEntity)

        // Observe all complaints for a user
        @Query("SELECT * FROM complaints WHERE userId = :uid ORDER BY timestamp DESC")
        fun observeComplaints(uid: String): Flow<List<ComplaintEntity>>


        @Query("SELECT * FROM complaints ORDER BY timestamp DESC")
        fun getRecentComplaints(): PagingSource<Int,ComplaintEntity>

        @Query(" SELECT * FROM complaints ORDER BY timestamp DESC LIMIT 5")
        fun observeRecentComplaints(): Flow<List<ComplaintEntity>>

        @Query("SELECT * FROM complaints WHERE status = 'PENDING' ORDER BY timestamp DESC")
        fun getPendingComplaints(): PagingSource<Int, ComplaintEntity>

        @Query("SELECT * FROM complaints WHERE status = 'RESOLVED' ORDER BY timestamp DESC")
        fun getResolvedComplaints(): PagingSource<Int, ComplaintEntity>

        @Query("SELECT COUNT(*) FROM complaints WHERE status = 'PENDING'")
        fun observePendingCount(): Flow<Int>

        @Query("SELECT COUNT(*) FROM complaints WHERE status = 'RESOLVED'")
        fun observeResolvedCount(): Flow<Int>


        // Insert one complaint
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertComplaint(item: ComplaintEntity)



        @Query("DELETE FROM complaints WHERE id = :id")
        suspend fun deleteById(id: String)

        // Check if complaint exists
        @Query("SELECT * FROM complaints WHERE id = :id LIMIT 1")
        suspend fun getComplaint(id: String): ComplaintEntity?




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