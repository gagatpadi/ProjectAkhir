package com.example.projectakhir.viewmodel

import com.baginda.project_akhir.data.ServisEntity
import com.example.projectakhir.data.ServisDao
import kotlinx.coroutines.flow.Flow

/**
 * Repository untuk mengabstraksi akses ke DAO.
 * Baginda, ini membantu kita menjaga kode tetap rapi jika nanti ada sumber data lain.
 */
class ServisRepository(private val servisDao: ServisDao) {

    // Mengambil data berdasarkan status
    fun getJobsByStatus(status: String): Flow<List<ServisEntity>> =
        servisDao.getJobsByStatus(status)

    // Menghitung total saldo
    val totalSaldo: Flow<Long?> = servisDao.getTotalSaldo()

    // Fungsi-fungsi aksi
    suspend fun insert(servis: ServisEntity) = servisDao.insertServis(servis)

    suspend fun update(servis: ServisEntity) = servisDao.updateServis(servis)

    suspend fun clearHistory() = servisDao.clearHistory()
}