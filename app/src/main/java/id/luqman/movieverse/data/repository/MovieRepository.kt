package id.luqman.movieverse.data.repository

import id.luqman.movieverse.data.local.MovieDao
import id.luqman.movieverse.data.local.MovieEntity
import id.luqman.movieverse.data.remote.MovieResponse
import id.luqman.movieverse.data.remote.TmdbApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class MovieRepository(
    private val api: TmdbApi,
    private val dao: MovieDao
) {

    // Meminta data film populer dari internet
    suspend fun getPopularMovies(apiKey: String): MovieResponse {
        return api.getPopularMovies(apiKey)
    }

    // Menyimpan film ke brankas HP (Room)
    suspend fun saveMovieToDb(movie: MovieEntity) {
        // WAJIB membungkus perintah simpan ini dengan "Dispatchers.IO".
        // Ini memastikan proses penyimpanan berjalan di latar belakang dan tidak membuat UI HP ngelag.
        withContext(Dispatchers.IO) {
            dao.insertMovie(movie)
        }
    }

    // Membaca semua film yang tersimpan di brankas
    fun getSavedMovies(): Flow<List<MovieEntity>> {
        return dao.getSavedMovies()
    }
}