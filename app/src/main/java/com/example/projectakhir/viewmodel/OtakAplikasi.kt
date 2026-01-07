package com.example.projectakhir.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.baginda.project_akhir.data.ServisEntity
import com.example.projectakhir.data.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel Utama MyKonter.
 * Di sinilah logika Page 1 hingga Page 4 dikelola agar UI tetap sinkron dengan data.
 */
class MyKonterViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ServisRepository

    // Data untuk masing-masing halaman
    val inProgressJobs: Flow<List<ServisEntity>>
    val doneJobs: Flow<List<ServisEntity>>
    val historyJobs: Flow<List<ServisEntity>>
    val totalSaldo: StateFlow<Long>

    init {
        val dao = AppDatabase.getDatabase(application).servisDao()
        repository = ServisRepository(dao)

        // Inisialisasi aliran data (Flow)
        inProgressJobs = repository.getJobsByStatus("in-progress")
        doneJobs = repository.getJobsByStatus("done")
        historyJobs = repository.getJobsByStatus("taken")

        // Mengonversi Flow saldo ke StateFlow agar mudah dibaca oleh UI Compose
        totalSaldo = repository.totalSaldo
            .map { it ?: 0L }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)
    }

    // Aksi: Simpan data baru (Page 1)
    fun addServis(nama: String, hp: String, kerusakan: String, harga: Long, tgl: String) {
        viewModelScope.launch {
            val newJob = ServisEntity(
                namaKonsumen = nama,
                tipeHp = hp,
                kerusakan = kerusakan,
                harga = harga,
                status = "in-progress",
                tglMasuk = tgl
            )
            repository.insert(newJob)
        }
    }

    // Aksi: Selesai Servis (Page 2 -> Page 3)
    fun markAsDone(servis: ServisEntity) {
        viewModelScope.launch {
            repository.update(servis.copy(status = "done"))
        }
    }

    // Aksi: Sudah Diambil (Page 3 -> Page 4 & Tambah Saldo)
    fun markAsTaken(servis: ServisEntity, tglAmbil: String) {
        viewModelScope.launch {
            repository.update(servis.copy(status = "taken", tglAmbil = tglAmbil))
        }
    }

    // Aksi: Hapus History (Page 4)
    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }
}