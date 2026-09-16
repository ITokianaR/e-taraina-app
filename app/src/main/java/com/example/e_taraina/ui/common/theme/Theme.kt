// Theme.kt
package com.example.e_taraina.ui.common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val ETarainaColorScheme = lightColorScheme(
    primary = ETarainaBlack,
    onPrimary = ETarainaWhite,
    secondary = ETarainaRed,
    onSecondary = ETarainaWhite,
    tertiary = ETarainaBlue,
    background = ETarainaBackground,
    onBackground = ETarainaBlack,
    surface = ETarainaWhite,
    onSurface = ETarainaBlack,
    error = ETarainaRed
)

@Composable
fun ETarainaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ETarainaColorScheme,
        typography = ETarainaTypography,
        content = content
    )
}