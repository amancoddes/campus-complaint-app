package com.example.demo.complaintApp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
@Composable
fun ProfileSetupContent(
    state: UserData,
    onNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onRollChange: (String) -> Unit,
    onBranchChange: (String) -> Unit,
    onSubmit: () -> Unit,
    isLoading: Boolean,
    error: String?
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
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

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .align(Alignment.Center),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(10.dp)
        ) {

            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // 🔥 Title
                Text(
                    text = "Complete Your Profile 👤",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Tell us about yourself",
                    color = Color.Gray
                )

                Spacer(Modifier.height(20.dp))

                if (error != null) {
                    Text(error, color = Color.Red)
                    Spacer(Modifier.height(10.dp))
                }

                if (isLoading) {
                    CircularProgressIndicator()
                } else {

                    // 🔥 Name
                    OutlinedTextField(
                        value = state.name,
                        onValueChange = onNameChange,
                        label = { Text("Full Name") },
                        leadingIcon = {
                            Icon(Icons.Default.Person, null)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(Modifier.height(12.dp))

                    // 🔥 Phone
                    OutlinedTextField(
                        value = state.phone,
                        onValueChange = onPhoneChange,
                        label = { Text("Phone Number") },
                        leadingIcon = {
                            Icon(Icons.Default.Phone, null)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(Modifier.height(12.dp))

                    // 🔥 Roll No
                    OutlinedTextField(
                        value = state.rollNo,
                        onValueChange = onRollChange,
                        label = { Text("Roll Number") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(Modifier.height(12.dp))

                    // 🔥 Branch Dropdown
                    TitleDropdownSelector(
                        boxShow = "Department",
                        modifier = Modifier.fillMaxWidth(),
                        listTitles = listOf(
                            "CSIT", "ECE", "ME", "EI",
                            "EE", "MBA", "HU", "PHARMACY", "Law dept"
                        ),
                        complainTitle = state.branch,
                        onTitleSelected = onBranchChange
                    )

                    Spacer(Modifier.height(20.dp))

                    // 🔥 Submit Button
                    Button(
                        onClick = onSubmit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Save & Continue", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}
@Composable
fun ProfileSetupScreen(
    view: ProfileSetupScreenViewModel,
    nav: NavHostController
) {

    val screenState by view.state.collectAsState()
    val uiState by view.uiData
    LaunchedEffect(screenState) {
        if (screenState is SaveState.Success) {
            nav.navigate("main_Graph") {
                popUpTo("login/signup") { inclusive = true }
            }
        }
    }

    ProfileSetupContent(
        state = uiState,
        onNameChange = { view.update(SetEnum.Name, it) },
        onPhoneChange = { view.update(SetEnum.Phone, it) },
        onRollChange = { view.update(SetEnum.RollNo, it) },
        onBranchChange = { view.update(SetEnum.Branch, it) },
        onSubmit = { view.sendData() },
        isLoading = screenState is SaveState.Loading,
        error = (screenState as? SaveState.Error)?.msg
    )
}
@Preview(showBackground = true)
@Composable
fun ProfileSetupPreview() {

    ProfileSetupContent(
        state = UserData(
            name = "Aman",
            phone = "9876543210",
            rollNo = "22CS123",
            branch = "CSIT"
        ),

        onNameChange = {},
        onPhoneChange = {},
        onRollChange = {},
        onBranchChange = {},
        onSubmit = {},

        isLoading = false,
        error = null
    )
}