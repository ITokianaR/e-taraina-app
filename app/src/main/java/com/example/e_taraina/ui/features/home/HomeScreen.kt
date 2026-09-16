package com.example.e_taraina.ui.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.e_taraina.data.ComplaintStore
import com.example.e_taraina.ui.common.components.ComplaintFormDialog
import com.example.e_taraina.ui.common.components.ETarainaButton
import com.example.e_taraina.ui.common.theme.ETarainaBlack
import com.example.e_taraina.ui.common.theme.ETarainaBlue
import com.example.e_taraina.ui.common.theme.ETarainaGray
import com.example.e_taraina.ui.common.theme.ETarainaRed
import com.example.e_taraina.ui.common.theme.ETarainaWhite
import com.example.e_taraina.ui.viewmodel.HomeViewModel

// écran Home-user, calqué sur la maquette : bloc noir avec le message de
// bienvenue + bouton "Fill a complaint", bande bleue en dessous, et la
// barre grise tout en bas comme sur les autres frames
@Composable
fun HomeScreen(
    username: String,
    onFillComplaintClick: () -> Unit = {},
    onComplaintSubmitted: () -> Unit = {},
    onViewComplaintsClick: () -> Unit = {},
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showComplaintDialog by remember { mutableStateOf(false) }

    LaunchedEffect(username) {
        viewModel.setUsername(username)
    }

    if (showComplaintDialog) {
        ComplaintFormDialog(
            onDismiss = { showComplaintDialog = false },
            onSubmit = { complaint ->
                ComplaintStore.add(complaint)
                showComplaintDialog = false
                onComplaintSubmitted()
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .background(ETarainaBlack)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
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

            Column(
                modifier = Modifier.width(220.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ETarainaButton(
                    text = "Fill a complaint",
                    onClick = {
                        showComplaintDialog = true
                        onFillComplaintClick()
                    },
                    containerColor = ETarainaRed,
                    contentColor = ETarainaWhite
                )

                Spacer(modifier = Modifier.height(8.dp))

                ETarainaButton(
                    text = "View complaints",
                    onClick = onViewComplaintsClick
                )
            }
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(ETarainaBlue)
        )

        // rien ici, juste l'espace blanc vide comme sur la maquette
        Spacer(modifier = Modifier.weight(1f))

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .background(ETarainaGray)
        )
    }
}
