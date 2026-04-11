package com.example.demo.complaintApp
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController


@Composable
fun Home_Screen(navHostController: NavHostController,viewModel:HomeScreenViewModel,innerPadding: PaddingValues) {
    val state by viewModel.complaintsUiState.collectAsState()
    val viewCounts by viewModel.counts.collectAsState()
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
    HomeScreen2(uiState = uiState,snackbarHostState = snackbarHostState, onRetry = { viewModel.retrySync() }, loginRetry = {
        navHostController.navigate("login/signup") {
            popUpTo("login/signup") {
                inclusive = true
            }
        }
    },innerPadding = innerPadding,counts=viewCounts, state = state, onViewCall = {
        navHostController.navigate(AllRoute.ReportList.route) {

            popUpTo("main_Graph") {
                saveState = true
            }

            launchSingleTop = true
            restoreState = true
        }
    })
}
@Composable
fun HomeScreen2(uiState: HomeUiState2, snackbarHostState: SnackbarHostState,onRetry:() -> Unit,loginRetry:()->Unit
,innerPadding: PaddingValues, counts: HomeCounts,state:ComplaintUiStates,onViewCall:()-> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {



        Column( modifier = Modifier.fillMaxSize()
        ) {


            val greeting = when (uiState) {
                is HomeUiState2.Loading -> uiState.greeting
                is HomeUiState2.Success -> uiState.greeting
                is HomeUiState2.Error -> uiState.greeting
                is HomeUiState2.Empty -> uiState.greeting
                is HomeUiState2.NotLogin -> uiState.greeting
            }

            GreetingBanner(greeting)

            when(uiState) {

                is HomeUiState2.Empty -> {
                    EmptySection()
                }
                is HomeUiState2.Error -> {
                    ErrorSection( message = uiState.message, onRetry = onRetry)
                }
                is HomeUiState2.Loading -> {
                    LoadingSection()
                }
                is HomeUiState2.NotLogin -> {
                    LoginRequiredSection(onLoginClick = loginRetry)
                }
                is HomeUiState2.Success -> {

                   when(state){
                       ComplaintUiStates.Empty -> EmptySection()
                       is ComplaintUiStates.Error -> { Text("error ${state.message}")
                       }
                       ComplaintUiStates.Loading -> LoadingSection()
                       is ComplaintUiStates.NotLogin ->                     LoginRequiredSection(onLoginClick = loginRetry)

                       is ComplaintUiStates.Success -> HomeContent(recent = state.data, pending = counts.pending, resolved = counts.resolved, onViewAllClick = onViewCall)
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



@Composable
fun GreetingBanner(greeting: String) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(3.0f)
            .padding(10.dp),
        shape = RoundedCornerShape(20.dp)
    ) {

        Box {

            Image(
                painter = painterResource(R.drawable.imagegreeting),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.CenterStart)
            ) {

                Text(
                    text = greeting,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold, color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Report issues around your campus",
                    fontSize = 14.sp,color = Color.Blue.copy(0.4f)

                )

            }

        }

    }

}

var list1= listOf(
    ComplaintDataRoom.ComplaintEntity(complain = "xy"),
            ComplaintDataRoom.ComplaintEntity(complain = "xy")
)

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview2() {
    val fakeSnackbarHostState = SnackbarHostState()
    CityCareTheme {
        HomeScreen2( uiState = HomeUiState2.Success(greeting = "good morning"), snackbarHostState = fakeSnackbarHostState, onRetry = {  }, loginRetry = {}, innerPadding = PaddingValues(10.dp), counts = HomeCounts(pending = 4, resolved = 4) ,
      state = ComplaintUiStates.Success(list1), onViewCall = {}  )
    }


}