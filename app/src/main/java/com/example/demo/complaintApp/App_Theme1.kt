package com.example.demo.complaintApp


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// light theme
private val LightColors = lightColorScheme(
    primary = SoulPrimary,
    onPrimary = SoulTextOnPrimary,
    secondary = SoulSecondary,
    onSecondary = SoulTextOnSecondary,
    background = SoulBackground,
    onBackground = SoulTextOnBackground,
    surface = SoulSurface,
    onSurface = SoulTextOnSurface,
    error = SoulError,
    onError = SoulTextOnError
)

// dark theme color hai
private val DarkColors = darkColorScheme(
    primary = SoulPrimaryDark,
    onPrimary = Color.Black,
    secondary = SoulSecondaryDark,
    onSecondary = Color.White,
    background = SoulBackgroundDark,
    onBackground = SoulTextOnDark,
    surface = SoulSurfaceDark,
    onSurface = SoulTextOnDark,
    error = SoulError,
    onError = Color.White
)

//  light + dark theme
@Composable
fun CityCareTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Auto adjust based on system
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography_file1,
        //shapes = Shapes,
        content = content
    )
}