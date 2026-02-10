package com.example.soul

import android.os.Message
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp


@Composable
fun OutlinedFeed(view: ChatAppViewModel,message:String){
    OutlinedTextField(
        value = message,
        onValueChange = {view.messageInput(it)},
        placeholder = { Text( "message") },
        maxLines = 20,
        trailingIcon = {
            IconButton(onClick = {view.sendMessage(message)}, modifier = Modifier.padding(2.dp)) {
                Icon(imageVector = Icons.AutoMirrored.Filled.Send ,contentDescription = null,
                    Modifier.size(30.dp), tint = Color.White)
            }
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            autoCorrectEnabled = true,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Default
        )
        , colors = TextFieldDefaults.colors(focusedContainerColor = Color.Black, unfocusedContainerColor = Color.Black, unfocusedPlaceholderColor = Color.White, focusedPlaceholderColor = Color.White, focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent )

        , modifier = Modifier.fillMaxWidth().border(width = 5.dp, brush = Brush.linearGradient(colors = listOf(
            Color.Black, Color.Black.copy(0.7f), Color.Cyan)), shape = RoundedCornerShape(10.dp)
        ).clip(shape = RoundedCornerShape(20))
    )
}
