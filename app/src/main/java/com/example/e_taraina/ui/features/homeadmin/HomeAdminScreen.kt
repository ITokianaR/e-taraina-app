package com.example.e_taraina.ui.features.homeadmin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.e_taraina.data.Complaint
import com.example.e_taraina.data.ComplaintStatus
import com.example.e_taraina.data.ComplaintStore
import com.example.e_taraina.ui.common.components.ETarainaButton
import com.example.e_taraina.ui.common.theme.ETarainaBlack
import com.example.e_taraina.ui.common.theme.ETarainaBlue
import com.example.e_taraina.ui.common.theme.ETarainaGray
import com.example.e_taraina.ui.common.theme.ETarainaGreen
import com.example.e_taraina.ui.common.theme.ETarainaWhite

// écran Home-admin. Carte de stats en haut (total / reçu / en cours /
// traité) et cartes de report avec un vrai suivi en 3 étapes :
// reçu -> en cours -> traité, plutôt qu'un simple bouton "valider"
@Composable
fun HomeAdminScreen(onLogoutClick: () -> Unit = {}) {
    val complaints by ComplaintStore.complaints.collectAsState()
    val receivedCount = complaints.count { it.status == ComplaintStatus.RECEIVED }
    val inProgressCount = complaints.count { it.status == ComplaintStatus.IN_PROGRESS }
    val doneCount = complaints.count { it.status == ComplaintStatus.DONE }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Liste des réclamations",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.width(110.dp)) {
                    ETarainaButton(
                        text = "Déconnexion",
                        onClick = onLogoutClick
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            StatsCard(
                total = complaints.size,
                received = receivedCount,
                inProgress = inProgressCount,
                done = doneCount
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (complaints.isEmpty()) {
                Text(
                    text = "Aucune réclamation pour l'instant",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                complaints.forEach { complaint ->
                    ComplaintPreview(
                        complaint = complaint,
                        onAdvanceClick = {
                            val next = when (complaint.status) {
                                ComplaintStatus.RECEIVED -> ComplaintStatus.IN_PROGRESS
                                ComplaintStatus.IN_PROGRESS -> ComplaintStatus.DONE
                                ComplaintStatus.DONE -> ComplaintStatus.DONE
                            }
                            ComplaintStore.updateStatus(complaint.id, next)
                        },
                        onDeleteClick = { ComplaintStore.delete(complaint.id) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .background(ETarainaGray)
        )
    }
}

// petit dashboard avec les 4 chiffres clés, pour que l'admin voie d'un
// coup d'œil où en est la charge de travail
@Composable
private fun StatsCard(total: Int, received: Int, inProgress: Int, done: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(ETarainaBlack)
            .padding(vertical = 18.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatItem(label = "Total", value = total, valueColor = ETarainaWhite)
        StatItem(label = "Reçu", value = received, valueColor = ETarainaWhite)
        StatItem(label = "En cours", value = inProgress, valueColor = ETarainaBlue)
        StatItem(label = "Traité", value = done, valueColor = ETarainaGreen)
    }
}

@Composable
private fun StatItem(label: String, value: Int, valueColor: androidx.compose.ui.graphics.Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = valueColor
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = ETarainaWhite.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ComplaintPreview(
    complaint: Complaint,
    onAdvanceClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(ETarainaWhite)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = complaint.cause.ifBlank { "(pas de cause)" },
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )

            StatusBadge(complaint.status)
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = complaint.place.ifBlank { "(pas de lieu)" },
            style = MaterialTheme.typography.bodyMedium,
            color = ETarainaBlack.copy(alpha = 0.6f)
        )

        val photo = complaint.photo
        if (photo != null) {
            Spacer(modifier = Modifier.height(10.dp))
            Image(
                bitmap = photo.asImageBitmap(),
                contentDescription = "Photo de la réclamation",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (complaint.status != ComplaintStatus.DONE) {
            val nextLabel = when (complaint.status) {
                ComplaintStatus.RECEIVED -> "Passer en cours"
                ComplaintStatus.IN_PROGRESS -> "Marquer comme traité"
                ComplaintStatus.DONE -> ""
            }

            ETarainaButton(
                text = nextLabel,
                onClick = onAdvanceClick,
                containerColor = ETarainaGreen
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        ETarainaButton(
            text = "Supprimer",
            onClick = onDeleteClick
        )
    }
}

// pastille de statut, une couleur par étape du suivi : gris pour reçu,
// bleu pour en cours, vert pour traité
@Composable
private fun StatusBadge(status: ComplaintStatus) {
    val backgroundColor = when (status) {
        ComplaintStatus.RECEIVED -> ETarainaGray
        ComplaintStatus.IN_PROGRESS -> ETarainaBlue
        ComplaintStatus.DONE -> ETarainaGreen
    }
    val label = when (status) {
        ComplaintStatus.RECEIVED -> "Reçu"
        ComplaintStatus.IN_PROGRESS -> "En cours"
        ComplaintStatus.DONE -> "Traité"
    }

    Text(
        text = label,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.SemiBold,
        color = ETarainaBlack,
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}
