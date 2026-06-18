package id.luqman.movieverse.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import id.luqman.movieverse.ui.screens.DetailScreen
import id.luqman.movieverse.ui.screens.HomeScreen
import id.luqman.movieverse.viewmodel.HomeUiState
import id.luqman.movieverse.viewmodel.MovieViewModel

@Composable
fun MovieApp(viewModel: MovieViewModel) {
    // Membuat kontroler untuk mengatur perpindahan antar layar
    val navController = rememberNavController()

    // Memantau data film yang tersimpan di database agar UI selalu update
    val savedMovies by viewModel.savedMovies.collectAsState(initial = emptyList())
    // Mengambil daftar ID film yang sudah disimpan untuk pengecekan status ikon
    val savedMovieIds = savedMovies.map { it.id }

    // NavHost adalah peta rute yang menentukan layar apa yang harus tampil
    NavHost(navController = navController, startDestination = "home_screen") {

        // Rute untuk Halaman Utama
        composable("home_screen") {
            val uiState by viewModel.homeState.collectAsState()

            HomeScreen(
                uiState = uiState,
                savedMovieIds = savedMovieIds, // Kirim daftar ID film tersimpan ke layar utama
                onMovieClick = { movieId ->
                    navController.navigate("detail_screen/$movieId") // Pindah ke halaman detail
                },
                onSaveClick = { movie ->
                    viewModel.saveMovie(movie) // Simpan film ke database
                }
            )
        }

        // Rute untuk Halaman Detail (menggunakan argumen movieId)
        composable(
            route = "detail_screen/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId")
            val uiState by viewModel.homeState.collectAsState()

            if (uiState is HomeUiState.Success && movieId != null) {
                val selectedMovie = (uiState as HomeUiState.Success).movies.find { it.id == movieId }

                if (selectedMovie != null) {
                    DetailScreen(
                        movie = selectedMovie,
                        isSaved = savedMovieIds.contains(selectedMovie.id), // Cek status simpan film ini
                        onBackClick = { navController.popBackStack() }, // Kembali ke halaman sebelumnya
                        onSaveClick = { movie -> viewModel.saveMovie(movie) }
                    )
                }
            }
        }
    }
}