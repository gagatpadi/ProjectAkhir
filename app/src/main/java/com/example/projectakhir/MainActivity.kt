package com.example.projectakhir

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.projectakhir.TampilanUi.InputPage
import com.example.projectakhir.TampilanUi.ListPage
import com.example.projectakhir.TampilanUi.MyKonterTopBar
import com.example.projectakhir.viewmodel.MyKonterViewModel
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyKonterApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyKonterApp(viewModel: MyKonterViewModel = viewModel()) {
    val navController = rememberNavController()
    val saldo by viewModel.totalSaldo.collectAsState()

    val inProgressJobs by viewModel.inProgressJobs.collectAsState(initial = emptyList())
    val doneJobs by viewModel.doneJobs.collectAsState(initial = emptyList())
    val historyJobs by viewModel.historyJobs.collectAsState(initial = emptyList())

    Scaffold(
        topBar = { MyKonterTopBar(saldo) },
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                listOf("input", "progress", "done", "history").forEach { route ->
                    NavigationBarItem(
                        selected = currentRoute == route,
                        onClick = { navController.navigate(route) },
                        label = { Text(route.replaceFirstChar { it.uppercase() }) },
                        icon = {
                            Icon(
                                when(route) {
                                    "input" -> Icons.Default.Add
                                    "progress" -> Icons.Default.Build
                                    "done" -> Icons.Default.CheckCircle
                                    else -> Icons.Default.History
                                },
                                contentDescription = null
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = "progress", modifier = Modifier.padding(innerPadding)) {
            composable("input") {
                InputPage { nama, hp, kerusakan, harga ->
                    val tgl = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
                    viewModel.addServis(nama, hp, kerusakan, harga, tgl)
                    navController.navigate("progress")
                }
            }
            composable("progress") {
                // PERBAIKAN: Tambahkan blok onAction
                ListPage(
                    jobs = inProgressJobs,
                    emptyMsg = "Antrian kosong",
                    actionLabel = "Selesai",
                    onAction = { job ->
                        viewModel.markAsDone(job)
                        navController.navigate("done")
                    }
                )
            }
            composable("done") {
                ListPage(
                    jobs = doneJobs,
                    emptyMsg = "Tidak ada HP siap ambil",
                    actionLabel = "Sudah Diambil",
                    showAi = true,
                    onAction = { job ->
                        val tgl = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
                        viewModel.markAsTaken(job, tgl)
                    },
                    onAiClick = { job ->
                        // Logika Gemini akan ditambahkan nanti
                    }
                )
            }
            composable("history") {
                Column {
                    if (historyJobs.isNotEmpty()) {
                        TextButton(onClick = { viewModel.clearHistory() }, modifier = Modifier.padding(start = 16.dp)) {
                            Icon(Icons.Default.DeleteSweep, null)
                            Spacer(Modifier.padding(horizontal = 4.dp))
                            Text("Hapus Semua History")
                        }
                    }
                    // PERBAIKAN: Tambahkan blok onAction kosong jika tidak ada aksi di history
                    ListPage(
                        jobs = historyJobs,
                        emptyMsg = "Belum ada riwayat",
                        actionLabel = "Sudah Diambil",
                        onAction = { /* Tidak ada aksi di history */ }
                    )
                }
            }
        }
    }
}