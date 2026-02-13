//package com.example.soul.di
//
////import com.example.soul.ComplaintRepository
//import android.content.Context
//import androidx.room.Room
//import androidx.room.RoomDatabase
//import com.example.soul.AppDataBase
//import com.example.soul.ComplaintDataRoom
//import com.example.soul.FirstAppModuleRepository
//import com.example.soul.ProfileRoom
//import com.example.soul.ReportsRepoFirebase
//import com.example.soul.ReportsRepoRoom
//import com.example.soul.UserDataRepo
//import com.example.soul.UserProfileDataFirebaseRepository
//import com.example.soul.UserProfileDataRepo
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.storage.FirebaseStorage
//import com.google.rpc.context.AttributeContext.Auth
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dagger.hilt.components.SingletonComponent
//import kotlinx.coroutines.CoroutineDispatcher
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.sync.Mutex
//import javax.inject.Qualifier
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//object FirebaseModule {
//
//    @Qualifier
//    @Retention(AnnotationRetention.BINARY)
//    annotation class MainDispatcher
//
//    @Qualifier
//    @Retention(AnnotationRetention.BINARY)
//    annotation class IoDispatcher
//
//
//    @Provides
//    @MainDispatcher
//    fun getMainThread():CoroutineDispatcher=Dispatchers.Main
//
//    @Provides
//    @IoDispatcher
//    fun getIoThread():CoroutineDispatcher=Dispatchers.IO
//
//
//
//
//    @Provides
//    @Singleton
//    fun returnFirebase()= FirebaseFirestore.getInstance()
//
////    @Provides
////    @Singleton
////    fun provideStorage(): FirebaseStorage =
////        FirebaseStorage.getInstance()
//
//
//
//    @Provides
//    @Singleton
//    fun returnRepo(fire:FirebaseFirestore,auth:FirebaseAuth)=FirstAppModuleRepository(fire,auth)
//    //storage:FirebaseStorage
//    //,storage)
//
//    @Provides
//    @Singleton// life of method ye pure aap mei rahe ga
//    fun returnAuthRep()=FirebaseAuth.getInstance()
//
//
//    @Provides
//    @Singleton
//    fun returnUserDataRepoObj(fire: FirebaseFirestore,auth:FirebaseAuth)=UserDataRepo(fire,auth)
//
//    /*
//    .AUTOMATIC
//    Room ka default decision-maker mode.
//    Room khud decide karta hai ki:
//        •	WAL enable karna hai ya
//        •	TRUNCATE mode use karna hai
//
//     */
//    /*
//    use Wal
//    ✅ Fast reads
//
//    ✅ Faster writes
//
//    ✅ Better concurrency
//
//    ✅ Crash-safe transactions
//
//     */
//
//
//
//
//    /*
//    🔥 Agar yahan Activity context use kar dete:
//
//Activity destroy
//➡️ Room ke paas dead Activity ka reference reh jata
//➡️ Memory leak
//➡️ crash chance (because Activity no longer valid)
//     */
//    @Provides
//    @Singleton
//            /*
//            Room context ko store karke rakhta hai (usually as a singleton DB instance).
//            Isi liye yahan Application context hi use karna zaroori hota hai.
//             */
//    fun returnRoomAppDatabaseImplObject(@ApplicationContext context: Context):AppDataBase {//NO. Room khud-se “background mei chalta” nahi hai.//Background work tab hota hai jab tum query call karte ho
//        return Room.databaseBuilder(context = context, klass = AppDataBase::class.java, name = "complainAppUserData")
//            .setJournalMode(RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING).
//            build()// fallback for migataion for app update
//    }
//    //✔ Call — jab kaam turant karwana ho
////✔ Pass — jab object ko future me OS ki zarurat padegi
//    @Provides
//    @Singleton
//    fun provideGlobalMutex(): Mutex = Mutex()
//
//    @Provides
//    @Singleton
//    fun returnDao(appDatabaseobj:AppDataBase)=appDatabaseobj.profileQueries()
//
//
//    @Provides
//    @Singleton
//    fun returnDaoTable2(appDaoNextTable:AppDataBase)=appDaoNextTable.complaintQueries()
//
//    @Provides
//    @Singleton
//    fun returnUserDataRepoFirebase(fire: FirebaseFirestore)=UserProfileDataFirebaseRepository(fire)
//
//
//    @Provides
//    @Singleton
//    fun returnUserProfileDataRepo(dao:ProfileRoom.ProfileQueries,auth: FirebaseAuth,fireRepo:UserProfileDataFirebaseRepository,mutex: Mutex,dao2: ComplaintDataRoom.ComplaintDao, repo:ReportsRepoRoom)=
//        UserProfileDataRepo(dao,auth,fireRepo, mutex,dao2,repo)
//
//
//    @Provides
//    @Singleton
//    fun returnReportsRepofirebase(fire:FirebaseFirestore)=ReportsRepoFirebase(fire)
//
//
//    @Provides
//    @Singleton
//    fun returnComplaintDao(dao: ComplaintDataRoom.ComplaintDao,auth: FirebaseAuth,fire:ReportsRepoFirebase,mutex: Mutex)=ReportsRepoRoom(dao,auth,fire,mutex)
//
//}
//
///*
//@ApplicationContext Hilt ko clear instruction देता है:
//
//✔ “Mujhe application-level context do.”
//
//✔ “Activity destroy ho jaaye — phir bhi ye safe रहे।”
//
//✔ “Ye global context hai, memory leak free।”
// */
//
//
//
////✔ @HiltAndroidApp DI graph banata hai
//
//
//
///*
//
//why we manually pass context in method when we already write @HiltandroidApp
//🌟 THEN WHY @HiltAndroidApp cannot automatically inject context?
//
//Great question!
//Reason simple hai:
//
//✔ @HiltAndroidApp DI graph banata hai
//
//BUT
//
//❌ woh functions me auto-context inject nahi kar sakta
//
//because Hilt ko yeh nahi pata function kis type ka context expect kar raha hai.
//
//Hilt ke rule:
//
//“DI system binary choices auto resolve nahi karega.”
//
//Yani:
//	•	ActivityContext?
//	•	ApplicationContext?
//
//Hilt never guesses.
//Tumhe explicitly bolna padta hai.
//
// */