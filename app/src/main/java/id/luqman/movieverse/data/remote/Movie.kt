package id.luqman.movieverse.data.remote

import com.google.gson.annotations.SerializedName

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,

    // SerializedName bertugas mencocokkan teks JSON ("poster_path")
    // ke variabel Kotlin ("posterPath")
    @field:SerializedName("poster_path")
    val posterPath: String?,

    @field:SerializedName("release_date")
    val releaseDate: String?,

    @field:SerializedName("vote_average")
    val voteAverage: Double
)
