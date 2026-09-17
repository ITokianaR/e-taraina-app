package com.example.e_taraina.ui.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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

// écran Home-user — version retravaillée : tout le contenu (hero noir +
// bande bleue) est centré verticalement dans l'écran au lieu d'être collé
// en haut avec un grand vide bleu qui suit. La bande bleue devient un
// accent de hauteur fixe sous la carte, pas un bloc qui pousse pour
// remplir l'espace restant.
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

    Box(modifier = Modifier.fillMaxSize()) {

        // --- Contenu principal centré verticalement ET horizontalement
        // dans tout l'écran, donc plus de grand espace vide en dessous.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- Carte noire : hauteur qui suit son contenu.
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(ETarainaBlack)
                    .padding(horizontal = 24.dp, vertical = 36.dp),
                verticalArrangement = Arrangement.spacedBy(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Welcome to",
                        style = MaterialTheme.typography.titleSmall,
                        color = ETarainaWhite.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = if (uiState.username.isNotBlank()) {
                            uiState.username
                        } else {
                            "e-taraina"
                        },
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = ETarainaWhite,
                        textAlign = TextAlign.Center
                    )
                }

                Column(
                    modifier = Modifier.width(240.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Have an issue to report?",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = ETarainaWhite.copy(alpha = 0.85f),
                        textAlign = TextAlign.Center
                    )

                    ETarainaButton(
                        text = "Fill a complaint",
                        onClick = {
                            showComplaintDialog = true
                            onFillComplaintClick()
                        },
                        containerColor = ETarainaRed,
                        contentColor = ETarainaWhite
                    )

                    ETarainaButton(
                        text = "View complaints",
                        onClick = onViewComplaintsClick
                    )
                }
            }

            // --- Bande bleue : accent de hauteur fixe collé sous la
            // carte, pas un bloc qui étire pour remplir l'écran.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .height(28.dp)
                    .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                    .background(ETarainaBlue)
            )
        }

        // --- Barre du bas : fine, discrète, ancrée en bas de l'écran
        // indépendamment du contenu centré au-dessus.
        Spacer(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(6.dp)
                .background(ETarainaGray)
        )
    }
}