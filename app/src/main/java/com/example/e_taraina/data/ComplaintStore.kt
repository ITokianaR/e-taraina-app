package com.example.e_taraina.data

import android.graphics.Bitmap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * A complaint filled in through the "Fill a complaint" popup.
 */
data class Complaint(
    val cause: String,
    val place: String,
    val description: String,
    val photo: Bitmap? = null
)

/**
 * In-memory holder for submitted complaints, shared between Home-user
 * (which adds one on submit) and Report-list (which displays them all).
 * A plain singleton object, same idea as [com.example.e_taraina.di.AppModule] —
 * no backend/database yet, so this is just kept in memory for now.
 */
object ComplaintStore {
    private val _complaints = MutableStateFlow<List<Complaint>>(emptyList())
    val complaints: StateFlow<List<Complaint>> = _complaints.asStateFlow()

    fun add(complaint: Complaint) {
        _complaints.update { it + complaint }
    }
}
