//package com.example.soul.di
//
//import android.content.Context
//import androidx.room.Room
//import com.example.soul.SavedRoom
//import com.example.soul.RepositorySavedState
//import com.example.soul.DaoImplement
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.components.ViewModelComponent
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dagger.hilt.android.scopes.ViewModelScoped
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton
//
//
///*
//	@Module → Dagger ko batata hai ki “ye class dependencies provide karegi.”
//	•	❗️Lekin sirf @Module likhne se kuch bhi nahi hota — Dagger ko ye nahi pata ki kisko ye dependencies deni hain.
//	•	@InstallIn(SingletonComponent::class) → ye annotation Hilt compiler ko batata hai:
//“Is module ko is component ke andar install karo.”
//
// */
//// ✅ AppModule.kt
//@Module
//@InstallIn(SingletonComponent::class)
//object AppModule {
//
//    @Provides
//    @Singleton
//    fun provideDatabase(@ApplicationContext context: Context): SavedRoom {
//        return Room.databaseBuilder(
//            context,
//            SavedRoom::class.java,
//            "Login_db"
//        ).build()
//    }
//
//    @Provides
//    fun provideDao(db: SavedRoom): DaoImplement {
//        return db.daoImplement()
//    }
//}
//
//// ✅ RepositoryModule.kt
//@Module
//@InstallIn(ViewModelComponent::class)
//object RepositoryModule {
//
//    @Provides
//    @ViewModelScoped
//    fun provideRepository(dao: DaoImplement): RepositorySavedState {
//        return RepositorySavedState(dao)
//    }
//
//    /*
//    🔹 @Provides sirf “banana ka rule” deta hai,
//actual object nahi deta.
//
//🔹 @InstallIn(ViewModelComponent::class) ye rule ko ViewModel ke graph me register karta hai.
//
//🔹 Jab ViewModel inject hota hai, Dagger wo rule follow karke repository ka instance banaata hai aur cache kar leta hai (@ViewModelScoped).
//     */
//}