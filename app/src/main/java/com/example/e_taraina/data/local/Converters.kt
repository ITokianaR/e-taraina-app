package com.example.e_taraina.data.local

import androidx.room.TypeConverter
import com.example.e_taraina.data.ComplaintStatus

// Room stocke les colonnes en types simples, donc l'enum passe par une
// String au lieu d'être stocké tel quel
class Converters {
    @TypeConverter
    fun fromStatus(status: ComplaintStatus): String = status.name

    @TypeConverter
    fun toStatus(value: String): ComplaintStatus = ComplaintStatus.valueOf(value)
}
