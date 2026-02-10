//package com.example.soul
//
//import androidx.compose.foundation.background
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
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Button
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import androidx.lifecycle.viewmodel.compose.viewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import retrofit2.http.GET
//import retrofit2.http.Query
//
//data class NewSData(
//    val status:String,
//    val toResult: Int,
//    val articles:List<ArticleNews>
//)
//
//data class ArticleNews(
//    val title:String,
//    val url: String
//)
//
//// Retrofit setup
//
//interface NewsFetch{
//    @GET("top-headlines")
//    suspend fun getNews(
//        @Query("country")  country:String,
//        @Query("apiKey")   apiKey:String
//    ):NewSData
//}
//
//
//// Retrodit instance
//
//object RetrofitInstance{
//    val api:NewsFetch by lazy {
//        Retrofit.Builder()
//            .baseUrl("https://newsapi.org/v2/")
//            .addConverterFactory(GsonConverterFactory.create()).build()// ye return kar raha hai Retrofit obejct
//            .create(NewsFetch::class.java)
//    }
//}
//
////  Repositry
//
//
//class NewsRepostry(){
//    suspend fun fetchData():Result<NewSData>{
//        return kotlin.runCatching {
//            RetrofitInstance.api.getNews(country = "us", apiKey ="14876577fe9c460b8e22e2317d179b59" )
//        }
//
//    }
//}
//
//sealed class NewsState{
//    data object Loading:NewsState()
//    data class SuccessState(val state:List<ArticleNews>):NewsState()
//    data class Error(val message:String):NewsState()
//}
//
//class NewsViewModel:ViewModel(){
//    private val repoobj=NewsRepostry()
//    private val _news=MutableStateFlow<NewsState>(NewsState.Loading)
//    var new=_news.asStateFlow()
//    fun getLast(){
//        viewModelScope.launch {
//            _news.value=NewsState.Loading
//            val response=repoobj.fetchData()
//            response.onSuccess {
//                    state2->
//                _news.value=NewsState.SuccessState(state = state2.articles)
//            }.onFailure { set->
//                val resultError=mapErrorToMessage(set)
//                _news.value=NewsState.Error(message = resultError)
//            }
//        }
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
//@Composable
//fun NewsScreen89(){
//    val viewModel: NewsViewModel= viewModel()
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
//            Box(modifier = Modifier.fillMaxSize().background(Color.Red.copy(0.4f)).padding(horizontal = 10.dp)){
//                LazyColumn(modifier = Modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
//                    items(new.state){
//
//
//                            index->
//
//                        Card(modifier = Modifier.fillMaxSize().wrapContentHeight().background(brush = Brush.verticalGradient(colors = listOf(
//                            Color.Yellow, Color.Cyan)), shape = RoundedCornerShape(20.dp)
//                        ).padding(10.dp), colors =CardDefaults.cardColors(containerColor = Color.Transparent)) {
//                            Text(index.url)
//                            Spacer(modifier = Modifier.height(10.dp))
//                            Text(index.title)
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
