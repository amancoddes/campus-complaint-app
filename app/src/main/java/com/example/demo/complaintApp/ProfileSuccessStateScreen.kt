package com.example.demo.complaintApp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun UserProfileContent(
    user: ProfileRoom.ProfileEntity,
    onLogout: () -> Unit,
    padding: PaddingValues = PaddingValues()
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF5C6BC0),
                        Color(0xFF3949AB),
                        Color(0xFF1A237E)
                    )
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            Box(contentAlignment = Alignment.Center) {

                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .background(
                            Color.White.copy(alpha = 0.1f),
                            CircleShape
                        )
                )

                Surface(
                    shape = CircleShape,
                    shadowElevation = 12.dp
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier
                            .size(90.dp)
                            .padding(20.dp),
                        tint = Color(0xFF5C6BC0)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = user.name,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp)
            ) {

                Column(modifier = Modifier.padding(16.dp)) {

                    ProfileItem("Roll No", user.rollNo)
                    HorizontalDivider()

                    ProfileItem("Phone", user.phone)
                    HorizontalDivider()

                    ProfileItem("Branch", user.branch)
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = onLogout,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE53935)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Logout")
            }
        }
    }
}


@Composable
fun UserProfileCard(
    user: ProfileRoom.ProfileEntity,
    nav: NavHostController,
    viewModel: ProfileScreenViewModel,
    padding: PaddingValues
) {

    UserProfileContent(
        user = user,
        padding = padding,

        onLogout = {
            FirebaseAuth.getInstance().signOut()
            viewModel.onLogout {
                nav.navigate("login/signup") {
                    popUpTo("main_Graph") { inclusive = true }
                }
            }

        }
    )
}

@Composable
fun ProfileItem(title: String, value: String) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = title,
            color = Color.Gray,
            fontSize = 13.sp
        )

        Text(
            text = value,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp
        )
    }
}



@Preview(showBackground = true)
@Composable
fun ProfilePreview() {

CityCareTheme {

    UserProfileContent(user = ProfileRoom.ProfileEntity(name = "soul", uid = "1923"), onLogout = {}, padding = PaddingValues(10.dp))
}


}
