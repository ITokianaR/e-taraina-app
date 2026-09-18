package com.example.e_taraina.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ComplaintDao {
    // Flow direct depuis Room : dès qu'une ligne change, tous les écrans
    // qui observent complaints() reçoivent la nouvelle liste tout seuls
    @Query("SELECT * FROM complaints")
    fun getAll(): Flow<List<ComplaintEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(complaint: ComplaintEntity)

    @Update
    suspend fun update(complaint: ComplaintEntity)

    @Query("DELETE FROM complaints WHERE id = :id")
    suspend fun deleteById(id: String)
}
