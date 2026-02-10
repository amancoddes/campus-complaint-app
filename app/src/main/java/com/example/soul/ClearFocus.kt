//package com.example.soul
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.material3.ripple
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.focus.FocusManager
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.SoftwareKeyboardController
//import androidx.compose.ui.unit.dp
//
//@Composable
//fun Modifier.clearSabFocus(x: FocusManager, y: SoftwareKeyboardController?): Modifier {
//    return this.clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {
//        x.clearFocus()
//        y?.hide()
//    }
//}
////indication = ripple(bounded = false, color = Color.Blue.copy(0.1f))