package com.example.e_taraina.ui.features.homeadmin

import androidx.compose.foundation.Image
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.e_taraina.data.Complaint
import com.example.e_taraina.data.ComplaintStore
import com.example.e_taraina.ui.common.components.ETarainaButton
import com.example.e_taraina.ui.common.theme.ETarainaGray

// écran Home-admin, calqué sur la maquette : même liste que Report-list
// côté user, mais l'admin peut juste supprimer pour l'instant (pas de
// Modifier ici, ça reste réservé à l'utilisateur qui a fait le report)
@Composable
fun HomeAdminScreen() {
    val complaints by ComplaintStore.complaints.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = "Report list",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (complaints.isEmpty()) {
                Text(
                    text = "No report yet",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                complaints.forEach { complaint ->
                    ComplaintPreview(
                        complaint = complaint,
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

@Composable
private fun ComplaintPreview(complaint: Complaint, onDeleteClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ETarainaGray)
            .padding(12.dp)
    ) {
        Text(
            text = complaint.cause.ifBlank { "(no cause)" },
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = complaint.place.ifBlank { "(no place)" },
            style = MaterialTheme.typography.bodyMedium
        )

        val photo = complaint.photo
        if (photo != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                bitmap = photo.asImageBitmap(),
                contentDescription = "Complaint photo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        ETarainaButton(
            text = "Supprimer",
            onClick = onDeleteClick
        )
    }
}
