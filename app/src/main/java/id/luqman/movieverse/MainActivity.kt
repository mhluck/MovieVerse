package id.luqman.movieverse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import id.luqman.movieverse.data.local.MovieDatabase
import id.luqman.movieverse.data.remote.TmdbApi
import id.luqman.movieverse.data.repository.MovieRepository
import id.luqman.movieverse.ui.navigation.MovieApp
import id.luqman.movieverse.ui.theme.MovieVerseTheme // Sesuaikan jika nama theme-mu berbeda
import id.luqman.movieverse.viewmodel.MovieViewModel
import id.luqman.movieverse.viewmodel.MovieViewModelFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Membangun Retrofit (API)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create()) // Alat penterjemah JSON
            .build()
        val tmdbApi = retrofit.create(TmdbApi::class.java)

        // Membangun Brankas Room Database (Offline)
        val database = Room.databaseBuilder(
            applicationContext,
            MovieDatabase::class.java,
            "movie_db"
        ).build()

        // Membangun Repository yang menyatukan Retrofit dan Room
        val repository = MovieRepository(tmdbApi, database.movieDao())

        setContent {
            // Gunakan tema bawaan dari Android Studio
            MovieVerseTheme {

                // Membangun Otak (ViewModel) menggunakan Pabrik Factory
                val viewModel: MovieViewModel = viewModel(
                    factory = MovieViewModelFactory(repository)
                )

                // Menjalankan Aplikasi Navigasi!
                MovieApp(viewModel = viewModel)
            }
        }
    }
}