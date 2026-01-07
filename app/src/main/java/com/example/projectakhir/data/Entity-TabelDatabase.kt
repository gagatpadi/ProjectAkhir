package com.baginda.projectakhir.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity yang mendefinisikan tabel "servis_hp" di database lokal.
 * Baginda, ini adalah tempat kita menyimpan data setiap pekerjaan servis.
 */
@Entity(tableName = "servis_hp")
data class ServisEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val namaKonsumen: String,
    val tipeHp: String,
    val kerusakan: String,
    val harga: Long,
    val status: String, // in-progress, done, taken
    val tglMasuk: String,
    val tglAmbil: String? = null
)