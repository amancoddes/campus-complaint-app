package com.example.demo.complaintApp.di

//import  _> ComplaintRepository
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.demo.complaintApp.AppDataBase
import com.example.demo.complaintApp.ComplaintDataRoom
import com.example.demo.complaintApp.ComplaintSubmissionRepository
import com.example.demo.complaintApp.LocationValidator
import com.example.demo.complaintApp.ProfileRoom
import com.example.demo.complaintApp.ReportsRepoFirebase
import com.example.demo.complaintApp.UserRepository
import com.example.demo.complaintApp.UserProfileDataFirebaseRepository
import com.example.demo.complaintApp.ProfileRepository
import com.example.demo.complaintApp.UserComplaintsReadRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
object HiltDependencies {

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
    fun returnRepo(fire:FirebaseFirestore,auth:FirebaseAuth)=ComplaintSubmissionRepository(fire,auth)


    @Provides
    @Singleton// live full app life
    fun returnAuthRep()=FirebaseAuth.getInstance()


    @Provides
    @Singleton
    fun returnUserRepositoryObj(fire: FirebaseFirestore,auth:FirebaseAuth)=UserRepository(fire,auth)




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
    fun returnLocationValidation()=LocationValidator(currentTime = { System.currentTimeMillis()})

    @Provides
    @Singleton
    fun returnDaoTable2(appDaoNextTable:AppDataBase)=appDaoNextTable.complaintQueries()

    @Provides
    @Singleton
    fun returnUserDataRepoFirebase(fire: FirebaseFirestore)=UserProfileDataFirebaseRepository(fire)


    @Provides
    @Singleton
    fun returnUserProfileDataRepo(dao:ProfileRoom.ProfileQueries,fireRepo:UserProfileDataFirebaseRepository,mutex: Mutex,dao2: ComplaintDataRoom.ComplaintDao, repo:UserComplaintsReadRepository)=
        ProfileRepository(dao, fireRepo, mutex, dao2, repo)


    @Provides
    @Singleton
    fun returnReportsRepofirebase(fire:FirebaseFirestore)=ReportsRepoFirebase(fire)


    @Provides
    @Singleton
    fun returnComplaintDao(dao: ComplaintDataRoom.ComplaintDao,auth: FirebaseAuth,fire:ReportsRepoFirebase,mutex: Mutex)=
        UserComplaintsReadRepository(dao,auth,fire,mutex)

}
