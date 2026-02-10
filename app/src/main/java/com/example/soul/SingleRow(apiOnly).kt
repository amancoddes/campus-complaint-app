//package com.example.soul
//
//import android.app.Application
//import android.content.Context
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.Divider
//import androidx.compose.material3.HorizontalDivider
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.viewModelScope
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.room.*
//import com.google.gson.Gson
//import com.google.gson.reflect.TypeToken
//import kotlinx.coroutines.flow.*
//import kotlinx.coroutines.launch
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import retrofit2.http.GET
//import retrofit2.http.Query
//
//// ---------- API models ----------
//data class NewSData2(
//    val articles: List<ArticleNews2>
//)
//
//data class ArticleNews2(
//    val title: String,
//    val url: String
//)
//
//// ---------- Retrofit ----------
//interface NewsFetch2 {
//    @GET("top-headlines")
//    suspend fun getNews(
//        @Query("country") country: String,
//        @Query("apiKey") apiKey: String
//    ): NewSData2
//}
//
//object RetrofitInstance2 {
//    val api: NewsFetch2 by lazy {
//        Retrofit.Builder()
//            .baseUrl("https://newsapi.org/v2/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(NewsFetch2::class.java)
//    }
//}
//
//// ---------- Room Models ----------
//@Entity(tableName = "newsApiData")
//data class NewsEntity(
//    @PrimaryKey val id: Int = 1,   // single row snapshot
//    val fetchedAt: Long = System.currentTimeMillis(),
//    val articles: List<ArticleNewsEntity>
//)
//
//data class ArticleNewsEntity(
//    val title: String,
//    val url: String
//)
//
//// ---------- DAO ----------
//@Dao
//interface ArticleDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertNews(news: NewsEntity)
//
//    // ✅ Single row fetch (no List nesting)
//    @androidx.room.Query("SELECT * FROM newsApiData LIMIT 1")
//    fun getNews(): Flow<NewsEntity?>
//}
//
//// ---------- Database ----------
//@Database(entities = [NewsEntity::class], version = 1, exportSchema = false)
//@TypeConverters(Converters::class)
//abstract class NewsDatabase : RoomDatabase() {
//    abstract fun newsDao(): ArticleDao
//
//    companion object {
//        @Volatile private var INSTANCE: NewsDatabase? = null
//
//        fun getInstance(context: Context): NewsDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val inst = Room.databaseBuilder(
//                    context,
//                    NewsDatabase::class.java,
//                    "news_db69"
//                ).build()
//                INSTANCE = inst
//                inst
//            }
//        }
//    }
//}
//
//// ---------- TypeConverter ----------
//class Converters {
//    private val gson = Gson()
//
//    @TypeConverter
//    fun fromArticlesList(articles: List<ArticleNewsEntity>?): String {
//        return gson.toJson(articles ?: " ")
//    }
//
//    @TypeConverter
//    fun toArticlesList(json: String?): List<ArticleNewsEntity> {
//        if (json.isNullOrEmpty()) return emptyList()
//        val type = object : TypeToken<List<ArticleNewsEntity>>() {}.type
//        return gson.fromJson(json, type)
//    }
//}
//
//// ---------- Repository ----------
//class NewsRepository2(
//    private val dao: ArticleDao,
//    private val api: NewsFetch2
//) {
//    fun getNews(): Flow<NewsEntity?> = dao.getNews()
//
//    suspend fun forceRefreshArticles(country: String, apiKey: String) {
//        try {
//            val response = api.getNews(country, apiKey)
//            // eska andar list aaye List<ArticleNews2> convert karega <List<ArticleNewsEntity> mei by map
//            val newsEntity = NewsEntity(articles=response.articles.map { ArticleNewsEntity(it.title,it.url)})// yaha par json jisne esk NewSData2 obejct diya hai uske andar se aritcles ko access karke uska data ko one by one list mei convert kar rahe hai or NewArtcile ke list mei sore kar rahe hai
//            // api se jo aaye hai unko list mei convert kar rahe hai
//            /*
//            after a change by map
//            listOf(
//    ArticleNewsEntity("News 1", "https://a.com"),
//    ArticleNewsEntity("News 2", "https://b.com")
//)
//             */
//            dao.insertNews(newsEntity)
//            println("✅ API Success: ${response.articles.size} articles fetched")
//        } catch (e: Exception) {
//            println("❌ API Failed: ${e.message}")
//            e.printStackTrace()// Exception ka poora stack trace (error ka path) console pe print kar deta hai.
//        }
//    }
//}
//
//// ---------- ViewModel ----------
//class NewsViewModel(private val repository: NewsRepository2) : ViewModel() {
//    private val _articles = MutableStateFlow<NewsEntity?>(null)
//    val articles: StateFlow<NewsEntity?> = _articles.asStateFlow()
//
//    init {
//        viewModelScope.launch {
//            // 1️⃣ Pehle DB check karo
//            /*
//            .firstOrNull() ka matlab hota hai:
//👉 “Flow start karo, pehla emission do (agar koi ho), warna null dedo — aur fir Flow ko turant close kar do.”
//             */
//            val localData = repository.getNews().firstOrNull()
//            if (localData == null) {
//                repository.forceRefreshArticles("us", "3c1e9ea7fbe94f95bf4afddb3052d84d")
//            }
//            /*
//                collect {} ek infinite suspending function hai → jaise hi yeh call hota hai, coroutine wahi ruk jaati hai aur niche ka code kabhi run hi nahi hota.
//                •	Isliye firstOrNull() kabhi reach hi nahi karega.
//             */
//            // 2️⃣ Continuous observe
//            repository.getNews().collect { _articles.value = it }// esko niche hie likho agar upar likha to coroutine upar hie ruk jaye ga infinite time ka liye esliye hum .firstornulll() ka use katre ahi
//        }
//    }
//}
//
//// ---------- ViewModel Factory ----------
//class NewsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
//            val db = NewsDatabase.getInstance(context)
//            val repo = NewsRepository2(db.newsDao(), RetrofitInstance2.api)
//            return NewsViewModel(repo) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}
//
//// ---------- Compose UI ----------
//@Composable
//fun NewsScreen2() {
//    val context = LocalContext.current.applicationContext as Application
//    val viewModel: NewsViewModel = viewModel(factory = NewsViewModelFactory(context))
//    val newsEntity by viewModel.articles.collectAsState()
//
//    LazyColumn(modifier = Modifier.fillMaxSize()) {
//        newsEntity?.articles?.let { list ->
//            items(list) { article ->
//                Column(modifier = Modifier.padding(16.dp)) {
//                    Text(text = article.title)
//                    Text(text = article.url)
//                }
//                HorizontalDivider()
//            }
//        }
//    }
//}