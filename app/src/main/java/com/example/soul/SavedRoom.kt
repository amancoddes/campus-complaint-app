package com.example.soul

import android.app.Application
import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Room
import androidx.room.RoomDatabase
import retrofit2.http.GET
import retrofit2.http.Query

@Entity(tableName = "login")// ye table ka naam hoga
data class LoginTable(// data class ka naam ahi bas
    @PrimaryKey val id:Int=1,
    val userName:String=""
)

@Dao
interface DaoImplement{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note:LoginTable)

    @androidx.room.Query("SELECT * FROM login")
    suspend fun getData():LoginTable?
}

@Database(entities = [LoginTable::class], version = 1, exportSchema = false)
abstract class SavedRoom:RoomDatabase(){
    abstract fun daoImplement():DaoImplement


}


//    companion object{
//        @Volatile
//        var instance:SavedRoom?=null
//        fun check(context2: Context):SavedRoom{
//            return instance ?: synchronized(this){
//                val instance18= Room.databaseBuilder(
//                    context =context2,
//                    klass = SavedRoom::class.java,
//                    name = "Login_db"
//                ).build()
//               instance=instance18
//                instance18
//            }
//        }
//    }





//
//at android.app.LoadedApk.makeApplicationInner(LoadedApk.java:1510)
//at android.app.LoadedApk.makeApplicationInner(LoadedApk.java:1439)
//at android.app.ActivityThread.handleBindApplication(ActivityThread.java:7865)
//at android.app.ActivityThread.-$$Nest$mhandleBindApplication(Unknown Source:0)
//at android.app.ActivityThread$H.handleMessage(ActivityThread.java:2646)
//at android.os.Handler.dispatchMessage(Handler.java:108)
//at android.os.Looper.loopOnce(Looper.java:226)
//at android.os.Looper.loop(Looper.java:328)
//at android.app.ActivityThread.main(ActivityThread.java:9224)
//at java.lang.reflect.Method.invoke(Native Method)
//at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:594)
//at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:1099)
//Caused by: java.lang.NullPointerException: Attempt to invoke virtual method 'java.lang.Object android.content.Context.getSystemService(java.lang.String)' on a null object reference
//at android.content.ContextWrapper.getSystemService(ContextWrapper.java:934)
//at androidx.room.RoomDatabase$JournalMode.resolve$room_runtime_release(RoomDatabase.kt:669)
//at androidx.room.RoomDatabase$Builder.build(RoomDatabase.kt:1337)
//at com.example.soul.MyApplication2.db_delegate$lambda$0(MyApplication.kt:11)
//at com.example.soul.MyApplication2.$r8$lambda$yA4gwWNNTEncWjZIlIHjEChbCWM(Unknown Source:0)
//at com.example.soul.MyApplication2$$ExternalSyntheticLambda0.invoke(D8$$SyntheticClass:0)
//at kotlin.SynchronizedLazyImpl.getValue(LazyJVM.kt:74)
//at com.example.soul.MyApplication2.getDb(MyApplication.kt:10)
//at com.example.soul.MyApplication2.<init>(MyApplication.kt:13)
//at java.lang.Class.newInstance(Native Method)
//at android.app.AppComponentFactory.instantiateApplication(AppComponentFactory.java:76)
//at androidx.core.app.CoreComponentFactory.instantiateApplication(CoreComponentFactory.java:51)
//at android.app.Instrumentation.newApplication(Instrumentation.java:1282)
//at android.app.LoadedApk.makeApplicationInner(LoadedApk.java:1502)