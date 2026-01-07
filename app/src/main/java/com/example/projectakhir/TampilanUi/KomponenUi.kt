package com.example.projectakhir.TampilanUi

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baginda.project_akhir.data.ServisEntity
import java.text.NumberFormat
import java.util.*

// Fungsi utilitas format Rupiah
fun formatRupiah(amount: Long): String {
    val format = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
    return format.format(amount)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyKonterTopBar(saldo: Long) {
    CenterAlignedTopAppBar(
        title = { Text("MyKonter", fontWeight = FontWeight.Black) },
        actions = {
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(formatRupiah(saldo), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    )
}

@Composable
fun InputPage(onSave: (String, String, String, Long) -> Unit) {
    var nama by remember { mutableStateOf("") }
    var hp by remember { mutableStateOf("") }
    var kerusakan by remember { mutableStateOf("") }
    var harga by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Input Servis Baru", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        OutlinedTextField(value = nama, onValueChange = { nama = it }, label = { Text("Nama Konsumen") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = hp, onValueChange = { hp = it }, label = { Text("Tipe HP") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = kerusakan, onValueChange = { kerusakan = it }, label = { Text("Kerusakan") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
        OutlinedTextField(value = harga, onValueChange = { harga = it }, label = { Text("Harga (Rp)") }, modifier = Modifier.fillMaxWidth())

        Button(
            onClick = { onSave(nama, hp, kerusakan, harga.toLongOrNull() ?: 0L) },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            enabled = nama.isNotBlank() && hp.isNotBlank() && harga.isNotBlank()
        ) {
            Text("Simpan Data")
        }
    }
}

@Composable
fun JobCard(job: ServisEntity, actionLabel: String, onAction: () -> Unit, showAi: Boolean = false, onAiClick: () -> Unit = {}) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            // PERBAIKAN: Gunakan horizontalArrangement, bukan justifyContent
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(job.tipeHp, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(formatRupiah(job.harga), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
            Text("Konsumen: ${job.namaKonsumen}", fontSize = 14.sp)
            Text("Masalah: ${job.kerusakan}", fontSize = 14.sp, modifier = Modifier.padding(vertical = 4.dp))
            Text("Tgl Masuk: ${job.tglMasuk}", fontSize = 12.sp, color = androidx.compose.ui.graphics.Color.Gray)

            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onAction, modifier = Modifier.weight(1f)) {
                    Text(actionLabel)
                }
                if (showAi) {
                    FilledTonalButton(onClick = onAiClick) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null)
                    }
                }
            }
        }
    }
}

@Composable
fun ListPage(
    jobs: List<ServisEntity>,
    emptyMsg: String,
    actionLabel: String,
    onAction: (ServisEntity) -> Unit,
    showAi: Boolean = false,
    onAiClick: (ServisEntity) -> Unit = {}
) {
    if (jobs.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(emptyMsg, color = androidx.compose.ui.graphics.Color.Gray)
        }
    } else {
        LazyColumn(contentPadding = PaddingValues(16.dp)) {
            items(jobs) { job ->
                JobCard(
                    job = job,
                    actionLabel = actionLabel,
                    onAction = { onAction(job) },
                    showAi = showAi,
                    onAiClick = { onAiClick(job) }
                )
            }
        }
    }
}