package com.example.e_taraina.ui.common.components

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.e_taraina.data.Complaint
import com.example.e_taraina.ui.common.theme.ETarainaBlack
import com.example.e_taraina.ui.common.theme.ETarainaGray
import com.example.e_taraina.ui.common.theme.ETarainaGreen

// formulaire "Fill a complaint", utilisé pour ajouter une nouvelle plainte
// depuis Home, et pour la modifier depuis Report-list. On lui passe une
// plainte existante via [initialComplaint] pour pré-remplir les champs.
@Composable
fun ComplaintFormDialog(
    initialComplaint: Complaint? = null,
    onDismiss: () -> Unit,
    onSubmit: (Complaint) -> Unit
) {
    val context = LocalContext.current
    var cause by remember { mutableStateOf(initialComplaint?.cause.orEmpty()) }
    var place by remember { mutableStateOf(initialComplaint?.place.orEmpty()) }
    var description by remember { mutableStateOf(initialComplaint?.description.orEmpty()) }
    var photoBitmap by remember { mutableStateOf(initialComplaint?.photo) }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            photoBitmap = uri.toBitmapOrNull(context)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialComplaint != null) "Edit complaint" else "Fill a complaint") },
        text = {
            Column {
                ETarainaTextField(
                    value = cause,
                    onValueChange = { cause = it },
                    label = "Cause"
                )

                Spacer(modifier = Modifier.height(12.dp))

                ETarainaTextField(
                    value = place,
                    onValueChange = { place = it },
                    label = "Place"
                )

                Spacer(modifier = Modifier.height(12.dp))

                val photo = photoBitmap
                if (photo != null) {
                    Image(
                        bitmap = photo.asImageBitmap(),
                        contentDescription = "Selected photo",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                ETarainaButton(
                    text = if (photo != null) "Change photo" else "Choose photo",
                    onClick = {
                        photoPicker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    containerColor = ETarainaGray,
                    contentColor = ETarainaBlack
                )

                Spacer(modifier = Modifier.height(12.dp))

                ETarainaTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = "Description"
                )
            }
        },
        confirmButton = {
            ETarainaButton(
                text = if (initialComplaint != null) "Save" else "Submit",
                onClick = {
                    onSubmit(
                        Complaint(
                            id = initialComplaint?.id ?: java.util.UUID.randomUUID().toString(),
                            cause = cause,
                            place = place,
                            description = description,
                            photo = photoBitmap
                        )
                    )
                },
                containerColor = ETarainaGreen,
                contentColor = Color.Black
            )
        },
        dismissButton = {
            ETarainaButton(
                text = "Cancel",
                onClick = onDismiss,
                containerColor = ETarainaGray,
                contentColor = ETarainaBlack
            )
        }
    )
}

// l'utilisateur choisit une image dans sa galerie, ici on la charge en Bitmap
// pour pouvoir l'afficher direct avec un Image() sans lib externe genre Coil
private fun Uri.toBitmapOrNull(context: android.content.Context): Bitmap? {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, this)
            ImageDecoder.decodeBitmap(source)
        } else {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(context.contentResolver, this)
        }
    } catch (e: Exception) {
        null
    }
}
