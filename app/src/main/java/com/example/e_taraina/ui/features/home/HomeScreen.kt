package com.example.e_taraina.ui.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.e_taraina.ui.common.components.ETarainaButton
import com.example.e_taraina.ui.common.theme.ETarainaBlack
import com.example.e_taraina.ui.common.theme.ETarainaBlue
import com.example.e_taraina.ui.common.theme.ETarainaRed
import com.example.e_taraina.ui.common.theme.ETarainaWhite
import com.example.e_taraina.ui.viewmodel.HomeViewModel

/**
 * Home-user screen. Mirrors the "Home-user" frame: a black welcome
 * block containing the "Fill a complaint" CTA, and a blue band below.
 */
@Composable
fun HomeScreen(
    username: String,
    onFillComplaintClick: () -> Unit = {},
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(username) {
        viewModel.setUsername(username)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(ETarainaBlack)
                .padding(24.dp)
        ) {
            Text(
                text = if (uiState.username.isNotBlank()) {
                    "Welcome to e-taraina, ${uiState.username}"
                } else {
                    "Welcome to e-taraina"
                },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = ETarainaWhite
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Fill a complaint",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = ETarainaWhite
            )

            Spacer(modifier = Modifier.height(8.dp))

            ETarainaButton(
                text = "Fill a complaint",
                onClick = onFillComplaintClick,
                containerColor = ETarainaRed,
                contentColor = ETarainaWhite
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(ETarainaBlue)
        )
    }
}
