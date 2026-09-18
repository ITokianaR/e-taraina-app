package com.example.e_taraina.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.e_taraina.data.Complaint
import com.example.e_taraina.data.ComplaintStatus

// version "table" d'une Complaint, pour Room. La photo est stockée en
// bytes (JPEG compressé) direct dans la ligne, pas de fichier séparé —
// plus simple, largement suffisant pour le volume de photos ici.
@Entity(tableName = "complaints")
data class ComplaintEntity(
    @PrimaryKey val id: String,
    val cause: String,
    val place: String,
    val description: String,
    val photoBytes: ByteArray?,
    val status: ComplaintStatus
) {
    // ByteArray casse equals()/hashCode() par défaut (comparaison par
    // référence), on les redéfinit pour que Room/Kotlin comparent bien
    // le contenu et pas juste l'adresse mémoire
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ComplaintEntity) return false
        return id == other.id &&
            cause == other.cause &&
            place == other.place &&
            description == other.description &&
            status == other.status &&
            (photoBytes?.contentEquals(other.photoBytes) ?: (other.photoBytes == null))
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + cause.hashCode()
        result = 31 * result + place.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + status.hashCode()
        result = 31 * result + (photoBytes?.contentHashCode() ?: 0)
        return result
    }
}

// va-et-vient entre le modèle domain (Complaint, avec un Bitmap) et
// l'entité Room (ComplaintEntity, avec des bytes)
fun ComplaintEntity.toComplaint(): Complaint {
    val bitmap = photoBytes?.let {
        android.graphics.BitmapFactory.decodeByteArray(it, 0, it.size)
    }
    return Complaint(
        id = id,
        cause = cause,
        place = place,
        description = description,
        photo = bitmap,
        status = status
    )
}

fun Complaint.toEntity(): ComplaintEntity {
    val bytes = photo?.let {
        val stream = java.io.ByteArrayOutputStream()
        it.compress(android.graphics.Bitmap.CompressFormat.JPEG, 85, stream)
        stream.toByteArray()
    }
    return ComplaintEntity(
        id = id,
        cause = cause,
        place = place,
        description = description,
        photoBytes = bytes,
        status = status
    )
}
