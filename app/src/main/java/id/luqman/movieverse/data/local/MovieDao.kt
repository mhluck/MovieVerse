package id.luqman.movieverse.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    // Menyimpan film ke database (jika sudah pernah disimpan, timpa datanya)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: MovieEntity)

    // Membaca semua film yang tersimpan.
    // "Flow" adalah pipa air; UI akan otomatis ter-update jika ada data baru masuk.
    @Query("SELECT * FROM saved_movies")
    fun getSavedMovies(): Flow<List<MovieEntity>>
}