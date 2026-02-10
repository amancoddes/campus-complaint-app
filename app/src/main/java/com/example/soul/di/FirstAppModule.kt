package com.example.soul.di

//import com.example.soul.ComplaintRepository
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.soul.AppDataBase
import com.example.soul.ComplaintDataRoom
import com.example.soul.FirstAppModuleRepository
import com.example.soul.ProfileRoom
import com.example.soul.ReportsRepoFirebase
import com.example.soul.ReportsRepoRoom
import com.example.soul.UserDataRepo
import com.example.soul.UserProfileDataFirebaseRepository
import com.example.soul.UserProfileDataRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.rpc.context.AttributeContext.Auth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class MainDispatcher

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class IoDispatcher


    @Provides
    @MainDispatcher
    fun getMainThread():CoroutineDispatcher=Dispatchers.Main

    @Provides
    @IoDispatcher
    fun getIoThread():CoroutineDispatcher=Dispatchers.IO




    @Provides
    @Singleton
    fun returnFirebase()= FirebaseFirestore.getInstance()



    @Provides
    @Singleton
    fun returnRepo(fire:FirebaseFirestore,auth:FirebaseAuth)=FirstAppModuleRepository(fire,auth)


    @Provides
    @Singleton// live full app life
    fun returnAuthRep()=FirebaseAuth.getInstance()


    @Provides
    @Singleton
    fun returnUserDataRepoObj(fire: FirebaseFirestore,auth:FirebaseAuth)=UserDataRepo(fire,auth)




    @Provides
    @Singleton
    fun returnRoomAppDatabaseImplObject(@ApplicationContext context: Context):AppDataBase {//NO. Room khud-se “background mei chalta” nahi hai.//Background work tab hota hai jab tum query call karte ho
        return Room.databaseBuilder(context = context, klass = AppDataBase::class.java, name = "complainAppUserData")
            .setJournalMode(RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING).
        build()// fallback for migataion for app update
    }
    @Provides
    @Singleton
    fun provideGlobalMutex(): Mutex = Mutex()

    @Provides
    @Singleton
    fun returnDao(appDatabaseobj:AppDataBase)=appDatabaseobj.profileQueries()


    @Provides
    @Singleton
    fun returnDaoTable2(appDaoNextTable:AppDataBase)=appDaoNextTable.complaintQueries()

    @Provides
    @Singleton
    fun returnUserDataRepoFirebase(fire: FirebaseFirestore)=UserProfileDataFirebaseRepository(fire)


    @Provides
    @Singleton
    fun returnUserProfileDataRepo(dao:ProfileRoom.ProfileQueries,auth: FirebaseAuth,fireRepo:UserProfileDataFirebaseRepository,mutex: Mutex,dao2: ComplaintDataRoom.ComplaintDao, repo:ReportsRepoRoom)=
        UserProfileDataRepo(dao,auth,fireRepo, mutex,dao2,repo)


    @Provides
    @Singleton
    fun returnReportsRepofirebase(fire:FirebaseFirestore)=ReportsRepoFirebase(fire)


    @Provides
    @Singleton
    fun returnComplaintDao(dao: ComplaintDataRoom.ComplaintDao,auth: FirebaseAuth,fire:ReportsRepoFirebase,mutex: Mutex)=ReportsRepoRoom(dao,auth,fire,mutex)

}
