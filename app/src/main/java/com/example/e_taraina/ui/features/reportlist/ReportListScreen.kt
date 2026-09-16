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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.e_taraina.data.Complaint
import com.example.e_taraina.data.ComplaintStore
import com.example.e_taraina.ui.common.theme.ETarainaGray

/**
 * Report-list screen. Mirrors the "Report-list" frame: a "Report list"
 * title followed by one card per submitted complaint, and the gray
 * bottom bar. Reads straight from [ComplaintStore], so it shows exactly
 * what was filled in through the "Fill a complaint" popup.
 */
@Composable
fun ReportListScreen() {
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
                    ComplaintCard(complaint)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        // gray bottom bar, like the other screens in the wireframe
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .background(ETarainaGray)
        )
    }
}

@Composable
private fun ComplaintCard(complaint: Complaint) {
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
                contentDescription = "Complaint photo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}
