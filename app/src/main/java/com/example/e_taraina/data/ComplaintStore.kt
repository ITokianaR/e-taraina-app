package com.example.e_taraina.data

import android.graphics.Bitmap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

// une plainte remplie via le popup "Fill a complaint"
data class Complaint(
    val id: String = UUID.randomUUID().toString(),
    val cause: String,
    val place: String,
    val description: String,
    val photo: Bitmap? = null
)

// stocke les plaintes en mémoire le temps que l'app tourne, pas de vraie base
// pour l'instant. Home ajoute, Report-list affiche et permet de modifier.
object ComplaintStore {
    private val _complaints = MutableStateFlow<List<Complaint>>(emptyList())
    val complaints: StateFlow<List<Complaint>> = _complaints.asStateFlow()

    fun add(complaint: Complaint) {
        _complaints.update { it + complaint }
    }

    // remplace la plainte qui a le même id, utilisé par le bouton "Modifier"
    fun update(complaint: Complaint) {
        _complaints.update { list ->
            list.map { if (it.id == complaint.id) complaint else it }
        }
    }
}
