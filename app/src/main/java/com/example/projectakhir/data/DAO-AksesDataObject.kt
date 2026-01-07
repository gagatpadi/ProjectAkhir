package com.example.projectakhir.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.baginda.project_akhir.data.ServisEntity
import kotlinx.coroutines.flow.Flow

/**
 * Interface DAO untuk mendefinisikan operasi database.
 * Di sini kita mengatur alur data untuk 4 halaman aplikasi Baginda.
 */
@Dao
interface ServisDao {

    // Mengambil semua data berdasarkan status (untuk Page 2, 3, dan 4)
    @Query("SELECT * FROM servis_hp WHERE status = :status ORDER BY id DESC")
    fun getJobsByStatus(status: String): Flow<List<ServisEntity>>

    // Menambah data servis baru (Page 1)
    @Insert
    suspend fun insertServis(servis: ServisEntity)

    // Memperbarui status servis (Misal: dari in-progress ke done)
    @Update
    suspend fun updateServis(servis: ServisEntity)

    // Menghitung total saldo dari servis yang sudah diambil (TAKEN)
    @Query("SELECT SUM(harga) FROM servis_hp WHERE status = 'taken'")
    fun getTotalSaldo(): Flow<Long?>

    // Menghapus semua data history (Page 4)
    @Query("DELETE FROM servis_hp WHERE status = 'taken'")
    suspend fun clearHistory()
}