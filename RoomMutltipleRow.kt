package com.example.soul

import android.app.Application
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Entity(tableName = "newsRows" )
data class NewsRow(
    @PrimaryKey val url:String,
    val title:String,
    val content:String,
)

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(news: List<NewsRow>)// <NewRow> object hai entity ka to table mei row mei convert ho jayega basak list mei hie kyu na pass karodo

    @Query("SELECT * FROM newsRows")
    fun getAllNews(): Flow<List<NewsRow>>
}



class ConverterNews18{
    // en code ko room compiler apne andar copy kar leta hai
    @TypeConverter
    fun convertToJson(articles: List<NewsRow>):String{

        return Gson().toJson(articles)

    }
    @TypeConverter
    fun toList(json:String):List<NewsRow>{
        val type69= object : TypeToken<List<NewsRow>>(){

        }.type
        return Gson().fromJson(json,type69)
    }
}

@Database(entities = [NewsRow::class], version = 1, exportSchema = false)
@TypeConverters(ConverterNews18::class)
abstract class NewsRoomData: RoomDatabase(){
    abstract fun newsDao18():NewsDao
    companion object{
        @Volatile
        var NewsInstance18:NewsRoomData?=null

        fun getImplementaionObject(context18: Context): NewsRoomData {

            return NewsInstance18 ?: synchronized(this){
                val instance18= Room.databaseBuilder(
                    context =context18,
                    klass = NewsRoomData::class.java,
                    name = "newRows_db"
                ).build()
                NewsInstance18=instance18
                instance18
            }

        }
    }
}


class NewsRowsRepostiry(private val dao: NewsDao,private val api:RetrofitNewsInterface18){


    fun getAllData():Flow<List<NewsRow>>{
        println("hey")
        return dao.getAllNews()
    }

    suspend fun apiFetch(){

        val response=api.getNewsFromApi18()
        println("api run ")
        val rows:List<NewsRow> =response.articles.map { NewsRow(
            it.url,
            it.title,
            it.content
        )
        }

        dao.insertAll(news = rows)
    }
}


class ViewModelRows(val repository:NewsRowsRepostiry):ViewModel(){
    private val _newsRows= MutableStateFlow<List<NewsRow>>(emptyList())
    val nesRows=_newsRows.asStateFlow()

    init {

        viewModelScope.launch {
            val yes=repository.getAllData().firstOrNull()
            if (yes.isNullOrEmpty()) {   // ✅ empty list case handle
                println("its run invoked api fucnitn 🙂‍↕️")
                repository.apiFetch()
            }


            repository.getAllData().collect{
                    news->
                _newsRows.value=news
            }
        }

    }
}


class NewsRowsFactory(val context:Context):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val ImmplemetnionObj=NewsRoomData.getImplementaionObject(context18 = context)
        val repo=ImmplemetnionObj.newsDao18()

        val x=NewsRowsRepostiry(dao = repo, api = RetrofitNewsInstance18.api)

        return ViewModelRows(repository = x) as T
    }
}


@Composable
fun MultipleRows(){
    val contextBase= LocalContext.current
    val context=contextBase.applicationContext as Application

    val view: ViewModelRows = viewModel(factory = NewsRowsFactory(context = context))

    val articles by view.nesRows.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(brush = Brush.linearGradient(colors = listOf(
        Color.Blue, Color.White))).padding(20.dp)){

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(articles){
                    aritcle->
                Card(modifier = Modifier.fillMaxWidth().wrapContentHeight(), colors = CardDefaults.cardColors(
                    Color.Red.copy(0.4f))){

                    Column(modifier = Modifier.fillMaxSize().padding(10.dp)) {
                        Text(aritcle.url)
                        Text(aritcle.title)
                        Text(aritcle.content)
                    }

                }
            }
        }
    }

}