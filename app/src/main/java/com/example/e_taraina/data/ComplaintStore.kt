package com.example.e_taraina.data

import android.content.Context
import android.graphics.Bitmap
import com.example.e_taraina.data.local.AppDatabase
import com.example.e_taraina.data.local.ComplaintDao
import com.example.e_taraina.data.local.toComplaint
import com.example.e_taraina.data.local.toEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

// suivi du traitement d'une plainte côté admin : reçu -> en cours -> traité
enum class ComplaintStatus {
    RECEIVED,
    IN_PROGRESS,
    DONE
}

// une plainte remplie via le popup "Fill a complaint"
data class Complaint(
    val id: String = UUID.randomUUID().toString(),
    val cause: String,
    val place: String,
    val description: String,
    val photo: Bitmap? = null,
    val status: ComplaintStatus = ComplaintStatus.RECEIVED
)

// même API publique qu'avant (add/update/delete/updateStatus + un
// StateFlow "complaints"), mais persisté dans Room au lieu de la mémoire.
// Les écrans n'ont rien eu à changer, ComplaintStore reste juste une
// façade au-dessus du DAO, avec son propre CoroutineScope pour les writes.
object ComplaintStore {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var dao: ComplaintDao
    private var initialized = false

    private val _complaints = MutableStateFlow<List<Complaint>>(emptyList())
    val complaints: StateFlow<List<Complaint>> = _complaints

    // à appeler une fois au démarrage de l'app (voir ETarainaApplication)
    // avant que HomeScreen/ReportListScreen ne lisent complaints
    fun init(context: Context) {
        if (initialized) return
        initialized = true
        dao = AppDatabase.getInstance(context).complaintDao()

        scope.launch {
            dao.getAll()
                .map { entities -> entities.map { it.toComplaint() } }
                .stateIn(scope, SharingStarted.Eagerly, emptyList())
                .collect { list -> _complaints.value = list }
        }
    }

    fun add(complaint: Complaint) {
        scope.launch { dao.insert(complaint.toEntity()) }
    }

    // remplace la plainte qui a le même id, utilisé par le bouton "Modifier"
    fun update(complaint: Complaint) {
        scope.launch { dao.update(complaint.toEntity()) }
    }

    fun delete(id: String) {
        scope.launch { dao.deleteById(id) }
    }

    // change le statut de suivi d'une plainte (reçu / en cours / traité),
    // utilisé par les boutons d'avancement côté admin
    fun updateStatus(id: String, status: ComplaintStatus) {
        val current = _complaints.value.find { it.id == id } ?: return
        scope.launch { dao.update(current.copy(status = status).toEntity()) }
    }
}
