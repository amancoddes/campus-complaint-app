//package com.example.soul
//
//import android.content.Context
//import android.content.Intent
//import android.net.Uri
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.wrapContentHeight
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.Button
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.style.TextDecoration
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.core.text.parseAsHtml
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//import org.intellij.lang.annotations.Language
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import retrofit2.http.GET
//import retrofit2.http.Query
//import retrofit2.http.Url
//
//data class NewSData(
////    val status:String,
////    val toResult: Int,
//    val totalArticles:Int,
//    val articles:List<ArticleNews>
//)
//
//data class ArticleNews(
//    val title:String,
//    val content:String,
//    val url: String
//)
//
//// Retrofit setup
//
//interface NewsFetch{
//    @GET("search")
//    suspend fun getNews(//internment
//        @Query("q") query: String="science",
//        @Query("country")  countryName:String,
//        @Query("category") category:String="technology",
//        @Query("max") max:Int=5,
//        @Query("token")   apiKey:String,
//        @Query("page") page:Int=2,
//        @Query("sortBy") sortBy:String="publishedAt"
//
//    ):NewSData
//}
//
//
//// Retrodit instance
//
//object RetrofitInstance{
//    val api:NewsFetch by lazy {
//        Retrofit.Builder()
//
//
//            // .baseUrl("https://newsapi.org/v2/")
//            .baseUrl("https://gnews.io/api/v4/")
//            .addConverterFactory(GsonConverterFactory.create()).build()// ye return kar raha hai Retrofit obejct
//            .create(NewsFetch::class.java)
//    }
//}
//
////  Repositry
//
//
//sealed class NewsState<out T>{
//    data object Loading:NewsState<Nothing>()
//    data class SuccessState<T>(val state:List<ArticleNews>):NewsState<T>()
//    data class Error(val message:String):NewsState<Nothing>()
//}
//
//class NewsRepostry(){
//    suspend fun fetchData():NewsState<NewSData>{
//        return try{
//            val result= RetrofitInstance.api.getNews(countryName = "us", apiKey ="2e9ec9a0d6d79c53a65b47e36c4f5d8a" )
//            NewsState.SuccessState(state = result.articles)
//        }catch (e:Exception){
//            val error=mapErrorToMessage(e)
//            NewsState.Error(error)
//        }
//
//    }
//    private fun mapErrorToMessage(e: Throwable): String {
//        return when (e) {
//            is java.net.UnknownHostException -> "No Internet Connection"
//            is java.net.SocketTimeoutException -> "Server is taking too long"
//            is retrofit2.HttpException -> {
//                when (e.code()) {
//                    403 -> "Invalid or expired API key / limit exceeded"
//                    429-> "limit exceed"
//                    404 -> "Data not found"
//                    500 -> "Server is down, try again later"
//                    else -> "Unexpected server error"
//                }
//            }
//            else -> "Something went wrong"
//        }
//    }
//}
//
//
//
//
//
//
//
//class NewsViewModel:ViewModel(){
//    private val repoobj=NewsRepostry()
//    private val _news=MutableStateFlow<NewsState<NewSData>>(NewsState.Loading)
//    var new=_news.asStateFlow()
//    fun getLast(){
//        viewModelScope.launch {
//            _news.value=NewsState.Loading
//            val result=repoobj.fetchData()
//            _news.value=result
//        }
//    }
//
//}
//
//
//
//@Composable
//fun NewsScreen89(){
//   val  viewModel: NewsViewModel=NewsViewModel()
//    val  news by viewModel.new.collectAsState()
//    LaunchedEffect(Unit) {
//        viewModel.getLast()
//    }
//
//
//    when(val new=news){
//        is NewsState.Loading->{
//            Box(modifier = Modifier.fillMaxSize()){
//                CircularProgressIndicator()
//            }
//        }
//        is NewsState.SuccessState-> {
//            Box(modifier = Modifier
//                .fillMaxSize()
//                .background(Color.Red.copy(0.4f))
//                .padding(horizontal = 10.dp)){
//                LazyColumn(modifier = Modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
//                    items(new.state){
//
//
//                            index->
//
//                        Card(modifier = Modifier
//                            .fillMaxSize()
//                            .wrapContentHeight()
//                            .background(
//                                brush = Brush.verticalGradient(
//                                    colors = listOf(
//                                        Color.Yellow, Color.Cyan
//                                    )
//                                ), shape = RoundedCornerShape(20.dp)
//                            )
//                            .padding(10.dp), colors =CardDefaults.cardColors(containerColor = Color.Transparent)) {
//                            ArticleUrl(index.url)
//                            Spacer(modifier = Modifier.height(10.dp))
//                            Text(index.title, modifier = Modifier.padding(10.dp))
//                            Spacer(modifier = Modifier.height(10.dp))
//                            Box(
//                                modifier = Modifier
//                                    .height(200.dp) // jitna chaho utna fixed height
//                                    .verticalScroll(rememberScrollState())
//                                    .padding(10.dp)
//                            ) {
//                                Text(
//                                    text = index.content,
//                                    style = MaterialTheme.typography.bodyLarge
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//
//        }
//        is  NewsState.Error-> {
//            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
//                Column {
//                    Text(new.message)
//                    Button(onClick = { viewModel.getLast()}) { Text("retry") }
//                }
//            }
//        }
//    }
//}
//
//
//@Composable
//fun ArticleUrl(urlMain: String){
//
//    val context= LocalContext.current
//
//
//    Card (modifier = Modifier.clickable(onClick = {
//        val intent=Intent(Intent.ACTION_VIEW, Uri.parse(urlMain))
//        context.startActivity(intent)}), colors = CardDefaults.cardColors(containerColor = Color.Transparent)){
//        Text(urlMain, fontSize = 10.sp, style = TextStyle(
//            color = Color.Blue,
//            textDecoration = TextDecoration.Underline
//        ), modifier = Modifier.padding(5.dp))
//    }
//}