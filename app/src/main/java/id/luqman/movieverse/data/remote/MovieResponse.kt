package id.luqman.movieverse.data.remote

import com.google.gson.annotations.SerializedName

// Ini adalah keranjang utama yang membungkus daftar film dari TMDB
data class MovieResponse(
    val page: Int,
    // "results" adalah list/array yang berisi banyak objek Movie
    @SerializedName("results")
    val results: List<Movie>
)