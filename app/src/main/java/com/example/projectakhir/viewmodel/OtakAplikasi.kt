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

class MyKonterViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ServisRepository

    val inProgressJobs: Flow<List<ServisEntity>>
    val doneJobs: Flow<List<ServisEntity>>
    val historyJobs: Flow<List<ServisEntity>>
    val totalSaldo: StateFlow<Long>

    init {
        // PERBAIKAN: Pastikan inisialisasi database tidak null
        val dao = AppDatabase.getDatabase(application).servisDao()
        repository = ServisRepository(dao)

        inProgressJobs = repository.getJobsByStatus("in-progress")
        doneJobs = repository.getJobsByStatus("done")
        historyJobs = repository.getJobsByStatus("taken")

        totalSaldo = repository.totalSaldo
            .map { it ?: 0L }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)
    }

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

    fun markAsDone(servis: ServisEntity) {
        viewModelScope.launch {
            repository.update(servis.copy(status = "done"))
        }
    }

    fun markAsTaken(servis: ServisEntity, tglAmbil: String) {
        viewModelScope.launch {
            repository.update(servis.copy(status = "taken", tglAmbil = tglAmbil))
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }
}