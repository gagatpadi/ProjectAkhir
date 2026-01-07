package com.example.projectakhir.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.baginda.project_akhir.data.ServisEntity

/**
 * Class Database utama.
 * Baginda, class ini berfungsi sebagai akses tunggal ke database MyKonter.
 */
@Database(entities = [ServisEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun servisDao(): ServisDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mykonter_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}