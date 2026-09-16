package com.example.e_taraina.ui.common.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.e_taraina.ui.common.theme.ETarainaGreen

// bouton réutilisé partout dans l'app, on change juste la couleur selon
// le contexte (vert pour login, rouge pour "Fill a complaint", etc.)
@Composable
fun ETarainaButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = ETarainaGreen,
    contentColor: Color = Color.Black,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        enabled = enabled && !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.height(20.dp),
                color = contentColor,
                strokeWidth = 2.dp
            )
        } else {
            Text(text)
        }
    }
}
