package com.example.demo.complaintApp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter

////
//@Composable
//fun SubmitSuccessScreen(navigation:NavHostController){
//Box(modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background)) {
//    Image(
//        painter = rememberAsyncImagePainter(model = R.drawable.image780),
//        contentDescription = null,
//        modifier = Modifier.fillMaxSize(),
//        contentScale = ContentScale.FillBounds
//    )
//
//    Button(onClick = {
//        navigation.navigate(AllRoute.Home.route){
//            popUpTo(AllRoute.AddReport.route){
//                inclusive=true
//            }
//            launchSingleTop=true
//        }
//    }, modifier = Modifier.align(Alignment.BottomCenter)) {
//        Text("    Home   ")
//    }
//
//
//}
//}

@Composable
fun SubmitSuccessContent(
    onGoHome: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize().padding(vertical = 20.dp)
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF4CAF50),
                        Color(0xFF2E7D32)
                    )
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(100.dp)
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Complaint Submitted 🎉",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = "Your issue has been reported successfully.\nWe’ll take action soon.",
                textAlign = TextAlign.Center,
                color = Color.White.copy(alpha = 0.9f)
            )
Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = onGoHome,
                modifier = Modifier

                    .padding(20.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                )
            ) {
                Text(
                    "Go to Home",
                    color = Color(0xFF2E7D32),
                    fontWeight = FontWeight.Bold
                )
            }
        }


    }
}
@Composable
fun SubmitSuccessScreen(nav: NavHostController) {

    SubmitSuccessContent(
        onGoHome = {
            nav.navigate(AllRoute.Home.route) {
                popUpTo(AllRoute.AddReport.route) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun SubmitSuccessPreview() {

    SubmitSuccessContent(
        onGoHome = {}
    )
}