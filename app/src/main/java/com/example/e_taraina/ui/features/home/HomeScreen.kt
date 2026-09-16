package com.example.e_taraina.ui.features.home

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.e_taraina.data.Complaint
import com.example.e_taraina.data.ComplaintStore
import com.example.e_taraina.ui.common.components.ETarainaButton
import com.example.e_taraina.ui.common.components.ETarainaTextField
import com.example.e_taraina.ui.common.theme.ETarainaBlack
import com.example.e_taraina.ui.common.theme.ETarainaBlue
import com.example.e_taraina.ui.common.theme.ETarainaGray
import com.example.e_taraina.ui.common.theme.ETarainaGreen
import com.example.e_taraina.ui.common.theme.ETarainaRed
import com.example.e_taraina.ui.common.theme.ETarainaWhite
import com.example.e_taraina.ui.viewmodel.HomeViewModel

/**
 * Home-user screen. Mirrors the "Home-user" frame: a black welcome
 * block containing the "Fill a complaint" CTA, a blue band below,
 * and a gray bottom bar like the other frames in the wireframe.
 *
 * Clicking "Fill a complaint" opens a simple popup form (cause, place,
 * photo, description). No backend yet, so submit just closes the popup.
 */
@Composable
fun HomeScreen(
    username: String,
    onFillComplaintClick: () -> Unit = {},
    onComplaintSubmitted: () -> Unit = {},
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
                onClick = {
                    showComplaintDialog = true
                    onFillComplaintClick()
                },
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

        // empty white space, just like the wireframe
        Spacer(modifier = Modifier.weight(1f))

        // gray bottom bar, like the other screens in the wireframe
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .background(ETarainaGray)
        )
    }
}

/**
 * Popup form for "Fill a complaint": cause, place, photo, description.
 * The photo is picked from the real device gallery via the system
 * photo picker (no permission needed) and shown as a preview. On
 * submit, the filled-in [Complaint] is passed to [onSubmit].
 */
@Composable
private fun ComplaintFormDialog(
    onDismiss: () -> Unit,
    onSubmit: (Complaint) -> Unit
) {
    val context = LocalContext.current
    var cause by remember { mutableStateOf("") }
    var place by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var photoBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            photoBitmap = uri.toBitmapOrNull(context)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Fill a complaint") },
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
                            androidx.activity.result.PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
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
                text = "Submit",
                onClick = {
                    onSubmit(
                        Complaint(
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

/**
 * Decodes a picked gallery image into a [Bitmap]. Uses [ImageDecoder]
 * on newer Android versions and falls back to the older MediaStore API
 * on API levels below 28 (the app's minSdk is 24).
 */
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
