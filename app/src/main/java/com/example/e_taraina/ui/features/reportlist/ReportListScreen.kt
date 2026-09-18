package com.example.e_taraina.ui.features.reportlist

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.e_taraina.data.Complaint
import com.example.e_taraina.data.ComplaintStatus
import com.example.e_taraina.data.ComplaintStore
import com.example.e_taraina.ui.common.components.ComplaintFormDialog
import com.example.e_taraina.ui.common.components.ETarainaButton
import com.example.e_taraina.ui.common.theme.ETarainaGray

// écran Report-list, calqué sur la maquette : titre "Report list" puis une
// carte par plainte enregistrée, chacune avec un bouton pour la modifier
@Composable
fun ReportListScreen() {
    val complaints by ComplaintStore.complaints.collectAsState()
    var editingComplaint by remember { mutableStateOf<Complaint?>(null) }

    editingComplaint?.let { complaint ->
        ComplaintFormDialog(
            initialComplaint = complaint,
            onDismiss = { editingComplaint = null },
            onSubmit = {
                ComplaintStore.update(it)
                editingComplaint = null
            }
        )
    }

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

            Spacer(modifier = Modifier.height(24.dp))

            if (complaints.isEmpty()) {
                Text(
                    text = "Aucune réclamation pour l'instant",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                complaints.forEach { complaint ->
                    ComplaintCard(
                        complaint = complaint,
                        onEditClick = { editingComplaint = complaint },
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
private fun ComplaintCard(
    complaint: Complaint,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ETarainaGray)
            .padding(12.dp)
    ) {
        Text(
            text = complaint.cause.ifBlank { "(pas de cause)" },
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = complaint.place.ifBlank { "(pas de lieu)" },
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = if (complaint.status == ComplaintStatus.VALIDATED) "Validée" else "En attente",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )

        if (complaint.description.isNotBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = complaint.description,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        val photo = complaint.photo
        if (photo != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                bitmap = photo.asImageBitmap(),
                contentDescription = "Photo de la réclamation",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        ETarainaButton(
            text = "Modifier",
            onClick = onEditClick
        )

        Spacer(modifier = Modifier.height(8.dp))

        ETarainaButton(
            text = "Supprimer",
            onClick = onDeleteClick
        )
    }
}
