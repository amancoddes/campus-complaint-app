package com.example.demo.complaintApp

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.flowOf


@Composable
fun UserAllComplaintsScreen(navHostController: NavHostController, viewModel: UserAllComplaintsScreenViewModel, innerPadding: PaddingValues) {
    val items = viewModel.pagingFlow.collectAsLazyPagingItems()
    val viewCounts by viewModel.counts.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        viewModel.syncOnce()
        Log.e("syncOne","syncOne 🌞🌞")
    }
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }
    val uiState by viewModel.uiState.collectAsState()
    HomeScreen(uiState = uiState,snackbarHostState = snackbarHostState, onRetry = { viewModel.retrySync() }, loginRetry = {
        navHostController.navigate("login/signup") {
            popUpTo("login/signup") {
                inclusive = true
            }
        }
    },selectedTab = selectedTab, innerPadding = innerPadding,
        onTabChange = viewModel::onTabChange,counts=viewCounts,items, clickItem = { item ->
            navHostController.navigate("complaint_detail/${item.id}")
        })
}
@Composable
fun HomeScreen(uiState: HomeUiState, snackbarHostState: SnackbarHostState, onRetry:() -> Unit, loginRetry:()->Unit
               , selectedTab: HomeTab, innerPadding: PaddingValues,
               onTabChange: (HomeTab) -> Unit, counts: HomeCounts, items: LazyPagingItems<ComplaintDataRoom.ComplaintEntity>,clickItem:(ComplaintDataRoom.ComplaintEntity)->Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {

        Column( modifier = Modifier.fillMaxSize()
        ) {

            when(uiState) {

                is HomeUiState.Empty -> {
                    EmptySection()
                }
                is HomeUiState.Error -> {
                    Log.e("loading check "," there error ")
                    ErrorSection( message = uiState.message, onRetry = onRetry)
                }
                is HomeUiState.Loading -> {
                    Log.e("loading check "," there 2")
                    LoadingSection()
                }
                is HomeUiState.NotLogin -> {
                    LoginRequiredSection(onLoginClick = loginRetry)
                }
                is HomeUiState.Success -> {

                    HomeTopTabs(
                        selectedTab = selectedTab,
                        pendingCount = counts.pending,
                        resolvedCount = counts.resolved,
                        onTabChange = onTabChange
                    )

                    when (selectedTab) {
                        HomeTab.RECENT ->  ShowList(items, clickItem = clickItem)
                        HomeTab.PENDING -> ShowList(items,clickItem = clickItem)
                        HomeTab.RESOLVED -> ShowList(items,clickItem = clickItem)
                    }

                }
            }





        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }



    //  }
}





val fakeList = listOf(
    ComplaintDataRoom.ComplaintEntity("1", "Complaint 1", "PENDING", 0L, url = "", resolvedImageUrl = ""),
    ComplaintDataRoom.ComplaintEntity("2", "Complaint 2", "RESOLVED", 0L, url = "", resolvedImageUrl = "")
)


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {

    val items = flowOf(PagingData.from(fakeList))
        .collectAsLazyPagingItems()
    val fakeSnackbarHostState = SnackbarHostState()
    CityCareTheme {
        HomeScreen( uiState = HomeUiState.Success, snackbarHostState = fakeSnackbarHostState, onRetry = {  }, loginRetry = {}
            , selectedTab = HomeTab.RECENT, onTabChange = {}, innerPadding = PaddingValues(10.dp), counts = HomeCounts() ,
            items = items,
            clickItem = {}
        )
    }


}
