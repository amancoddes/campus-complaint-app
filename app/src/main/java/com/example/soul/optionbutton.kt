package com.example.soul

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun SimpleDropDownDemo() {

    val items = listOf(
        "Garbage not collected",
        "Water leakage",
        "Street light not working",
        "Road pothole"
    )

    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf("Tap to choose") }

    Box(modifier = Modifier.fillMaxWidth()) {

        // BUTTON
        Button(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(selected)
        }

        // DROPDOWN POPUP
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        selected = item
                        expanded = false
                    }
                )
            }
        }
    }
}