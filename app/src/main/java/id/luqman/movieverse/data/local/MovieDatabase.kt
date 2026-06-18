package id.luqman.movieverse.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

// Mengeksekusi KSP untuk membangun kode database dari Entity yang dibuat
@Database(entities = [MovieEntity::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {

    // Mendaftarkan Dao agar bisa digunakan nanti
    abstract fun movieDao(): MovieDao
}