//package com.example.soul
//
//import android.content.Intent
//import android.net.Uri
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.lazy.rememberLazyListState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.style.TextDecoration
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.ViewModel
//import androidx.paging.Pager
//import androidx.paging.PagingConfig
//import androidx.paging.PagingData
//import androidx.paging.PagingSource
//import androidx.paging.PagingState
//import androidx.paging.compose.collectAsLazyPagingItems
//import kotlinx.coroutines.flow.Flow
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import retrofit2.http.GET
//import retrofit2.http.Query
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.viewModelScope
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.paging.LoadState
//import androidx.paging.LoadStates
//import androidx.paging.cachedIn
//import coil.compose.AsyncImage
//
//
//data class NewsDataStore(
//    val articles:List<Articles>
//)
//
//data class Articles(
//    val title:String,
//    val content:String,
//    val url:String,
//    val image:String
//)
//
//
//// Retrofit INTERFACE
//interface RetrofitInterface{
//    @GET("search")
//    suspend fun getNewsApi(
//        @Query("q")  query: String="sports",// category deffernet hoti hai q se
//        @Query("country") countryName:String="us",
//        @Query("page") page:Int,
//        @Query("max") pageSize:Int=10,
//        @Query("token") apiKey:String="2e9ec9a0d6d79c53a65b47e36c4f5d8a"
//    ):NewsDataStore
//}
//
////Retro Instance
//
//object RetrofitInstance{
//    val api:RetrofitInterface by lazy {
//        Retrofit.Builder().baseUrl("https://gnews.io/api/v4/").addConverterFactory(GsonConverterFactory.create()).build().create(RetrofitInterface::class.java)
//    }
//}
//
//// Inherit PagingSource class because its a abstraction so we have to implement it
//class NewPagingSource():NewPagingSourcePagingSource<Int,Articles>(){
//
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Articles> {
//        return try {
//            val page=params.key?:1
//            val pageSize=params.loadSize
//
//            val data1= RetrofitInstance.api.getNewsApi(page = page, pageSize = pageSize)
//
//            LoadResult.Page(data = data1.articles, prevKey = if (page == 1) null else page-1, nextKey = if (data1.articles.isEmpty()) null else page +1)
//        }catch (e:Exception){
//            // Timber.e(e, "Paging error")
//            LoadResult.Error(e)
//        }
//
//    }
//    // esme or bhi update kar sake ta hai
//    override fun getRefreshKey(state: PagingState<Int, Articles>): Int? {
////        return state.anchorPosition?.let { position ->
////            val page = state.closestPageToPosition(position)// jab ye ho ga tab ya hi par esko apnna page mil jaye ga
////            page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
////        } // eme hum jab use karege jab api dyanmic ho
//
//        return 4
//    }
//}
//
//// Repository class
//
//class NewsRepository(){
//
//    fun getNewsData():Pager<Int,Articles>{// manager
//        return Pager(
//            config = PagingConfig(pageSize = 9, prefetchDistance = 2,enablePlaceholders = false),
//            pagingSourceFactory = {NewPagingSource()}
//        )
//    }
//}
//
//// news ViewModel
//
//class PagingProjectViewModel( private val repo:NewsRepository):ViewModel(){
//
//    val fetchData: Flow<PagingData<Articles>> = repo.getNewsData().flow.cachedIn(viewModelScope)//	•	Matlab tumhe viewModelScope.launch { … } likhne ki zarurat hi nahi hai, kyunki Paging library khud internally scope handle karti hai.
//    /*
//    Jab tum cachedIn(viewModelScope) use karte ho, Paging khud ViewModel ke scope mein run ho jaata hai.
//	•	Agar tum repository se koi normal suspend function call karte, tab manually viewModelScope.launch use karna padta.
//     */
//}
//
//class NoteViewModelFactory2(private val repository:NewsRepository):ViewModelProvider.Factory{
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {// esme hum validation kar sakte hai for in mutiple viewmodel vase
//        return PagingProjectViewModel(repository) as T
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun NewsScreen11() {
//    val viewModel: PagingProjectViewModel = viewModel(factory = NoteViewModelFactory2(repository = NewsRepository()))// factory
//    val newDataByFetch = viewModel.fetchData.collectAsLazyPagingItems()
//    val stateElement = rememberLazyListState()
//
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.Yellow.copy(0.3f))
//           // .pullRefresh(pullRefreshState) // 👈 attach here
//    ) {
//        LazyColumn(
//            state = stateElement,
//            verticalArrangement = Arrangement.spacedBy(15.dp),
//            modifier = Modifier.fillMaxSize()
//        ) {
//            items(newDataByFetch.itemCount) { index ->
//                val finalResult = newDataByFetch[index]
//
//                finalResult?.let {
//                    Card(
//                        modifier = Modifier
//                            .padding(20.dp)
//                            .background(
//                                brush = Brush.linearGradient(
//                                    colors = listOf(
//                                        Color.Blue.copy(0.4f),
//                                        Color.Blue.copy(0.1f)
//                                    )
//                                ),
//                                shape = RoundedCornerShape(20.dp)
//                            )
//                            .padding(20.dp),
//                        colors = CardDefaults.cardColors(Color.Transparent)
//                    ) {
//                        Lick(finalResult)
//                        Text(finalResult.title)
//                        Spacer(modifier = Modifier.height(10.dp))
//                        Text(finalResult.content)
//                        val imageUrl=finalResult.image
//
//                        AsyncImage(
//                            model = imageUrl,
//                            contentDescription = "null",
//                            contentScale = ContentScale.Crop,
//                            placeholder = painterResource(id=R.drawable.ic_launcher_foreground),
//                            error = painterResource(id=R.drawable.ic_launcher_foreground),
//                            modifier = Modifier.fillMaxWidth()
//                        )
//                    }
//                }
//            }
//
//            // 🔹 Append LoadState handling
//            when (val x = newDataByFetch.loadState.append) {
//                is LoadState.Loading -> {
//                    item { CircularSpinner() }
//                }
//                is LoadState.Error -> {
//                    item {
//                        Column {
//                            val finalError = mapErrorToMessage(e = x.error)
//                            Text(finalError)
//                            Button(
//                                onClick = { newDataByFetch.retry() },
//                                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue.copy(0.4f))
//                            ) {
//                                Text("Retry")
//                            }
//                        }
//                    }
//                }
//                else -> {}
//            }
//        }
//
//        // 🔹 Pull refresh indicator (top side loader)
////        PullRefreshIndicator(
////            refreshing = isRefreshing,
////            state = pullRefreshState,
////            modifier = Modifier.align(Alignment.TopCenter)
////        )
//    }
//
//    // 🔹 Refresh LoadState handling (Error full screen)
//    when (val x = newDataByFetch.loadState.refresh) {
//        is LoadState.Loading -> {
//
//        }
//        is LoadState.Error -> {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color.Red.copy(0.3f)),
//                contentAlignment = Alignment.Center
//            ) {
//                Column {
//                    val finalError = mapErrorToMessage(e = x.error)
//                    Text(finalError)
//                    Button(
//                        onClick = { newDataByFetch.retry() },
//                        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue.copy(0.4f))
//                    ) {
//                        Text("Retry")
//                    }
//                }
//            }
//        }
//        else -> {}
//    }
//}
//@Composable
//fun Lick(fetch: Articles){
//
//    val context= LocalContext.current
//
//    Column {
//        Card (modifier = Modifier.clickable(onClick = {
//            val intent=Intent(Intent.ACTION_VIEW, Uri.parse(fetch.url))
//            context.startActivity(intent)}), colors = CardDefaults.cardColors(containerColor = Color.Transparent)){
//            Text(fetch.url, fontSize = 10.sp, style = TextStyle(
//                color = Color.Blue,
//                textDecoration = TextDecoration.Underline
//            ), modifier = Modifier.padding(4.dp))
//
//
//        }
//    }
//
//}
//
//
//@Composable
//fun mapErrorToMessage(e: Throwable): String {
//    return when (e) {
//        is java.net.UnknownHostException -> "No Internet Connection"
//        is java.net.SocketTimeoutException -> "Server is taking too long"
//        is retrofit2.HttpException -> {
//            when (e.code()) {
//                403 -> "Invalid or expired API key / limit exceeded"
//                429-> "limit exceed"
//                404 -> "Data not found"
//                500 -> "Server is down, try again later"
//                else -> "Unexpected server error"
//            }
//        }
//        else -> "Something went wrong"
//    }
//}
//
//
//
//
//@Composable
//fun CircularSpinner(){
//    Box(modifier = Modifier.fillMaxSize().background(Color.Cyan), contentAlignment = Alignment.Center){
//        CircularProgressIndicator(color = Color.Blue.copy(0.5f), strokeWidth = 4.dp)
//    }
//}