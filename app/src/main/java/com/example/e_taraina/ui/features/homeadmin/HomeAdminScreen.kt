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
import com.example.e_taraina.ui.common.theme.ETarainaGray
import com.example.e_taraina.ui.common.theme.ETarainaGreen
import com.example.e_taraina.ui.common.theme.ETarainaWhite

// écran Home-admin. Ajout d'une carte de stats en haut (total / traité /
// en attente) et des cartes de report retravaillées (coins arrondis,
// badge de statut coloré) pour que ça fasse moins brut que la V1.
@Composable
fun HomeAdminScreen() {
    val complaints by ComplaintStore.complaints.collectAsState()
    val validatedCount = complaints.count { it.status == ComplaintStatus.VALIDATED }
    val pendingCount = complaints.size - validatedCount

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = "Liste des réclamations",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            StatsCard(
                total = complaints.size,
                validated = validatedCount,
                pending = pendingCount
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
                        onValidateClick = { ComplaintStore.validate(complaint.id) },
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

// petit dashboard : total à gauche, traité / en attente à droite,
// pour que l'admin voie d'un coup d'œil où en est la charge de travail
@Composable
private fun StatsCard(total: Int, validated: Int, pending: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(ETarainaBlack)
            .padding(vertical = 18.dp, horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatItem(label = "Total", value = total, valueColor = ETarainaWhite)
        StatItem(label = "Validées", value = validated, valueColor = ETarainaGreen)
        StatItem(label = "En attente", value = pending, valueColor = ETarainaWhite)
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
    onValidateClick: () -> Unit,
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

        if (complaint.status == ComplaintStatus.PENDING) {
            ETarainaButton(
                text = "Valider la réclamation",
                onClick = onValidateClick,
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

// petite pastille de statut, vert si validé, gris sinon, plus lisible
// qu'un simple mot dans le corps de la carte
@Composable
private fun StatusBadge(status: ComplaintStatus) {
    val isValidated = status == ComplaintStatus.VALIDATED
    val backgroundColor = if (isValidated) ETarainaGreen else ETarainaGray
    val textColor = if (isValidated) ETarainaBlack else ETarainaBlack.copy(alpha = 0.7f)

    Text(
        text = if (isValidated) "Validée" else "En attente",
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.SemiBold,
        color = textColor,
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}
