package id.luqman.movieverse.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApi {
    // Meminta daftar film populer dari TMDB.
    // Kata "suspend" menandakan fungsi ini berjalan di latar belakang (Coroutine)
    @GET("movie/popular") // HTTP Method GET dan Endpoint API
    suspend fun getPopularMovies(
        // Di sinilah lokasi API Key
        @Query("api_key") apiKey: String
    ): MovieResponse
}