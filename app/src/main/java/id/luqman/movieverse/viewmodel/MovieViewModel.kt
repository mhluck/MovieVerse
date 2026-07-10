package id.luqman.movieverse.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModelProvider
import id.luqman.movieverse.data.local.MovieEntity
import id.luqman.movieverse.data.remote.Movie
import id.luqman.movieverse.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Mendefinisikan State (Kondisi) Jaringan / Wadah untuk menyimpan status UI: Sedang memuat, Sukses mendapat data, atau Error
sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Success(val movies: List<Movie>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {

    // StateFlow untuk Halaman Utama (Data dari Internet), Menyimpan status UI agar tetap terjaga meski HP di-rotasi
    private val _homeState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val homeState: StateFlow<HomeUiState> = _homeState.asStateFlow()

    // Flow untuk Halaman Offline (Data dari Room)
    // Room sudah mengembalikan nilai berupa Flow, jadi kita tinggal meneruskannya ke UI
    val savedMovies = repository.getSavedMovies() // Mengalirkan real-time data Flow dari Room

    init {
        // Saat ViewModel pertama kali dibuat, langsung ambil data dari internet
        getPopularMovies()
    }

    // Fungsi mengambil data dari API (Internet) atau Database (Offline)
    fun getPopularMovies() {
        viewModelScope.launch { // Menjalankan coroutine di latar belakang
            _homeState.value = HomeUiState.Loading // Set status memuat animasi / Tampilkan animasi loading

            try {
                // API Key TMDB untuk akses data
                val apiKey = "a0684745d731ebc563e6e5eddc525b2e"

                // Mencoba mengambil data dari internet
                val response = repository.getPopularMovies(apiKey)
                _homeState.value = HomeUiState.Success(response.results)

            } catch (e: Exception) {
                // INTERNET MATI / GAGAL API
                // Jika internet gagal, cek data di brankas (Room Database)

                repository.getSavedMovies().collect { savedEntities ->
                    if (savedEntities.isNotEmpty()) {
                        // Jika ada data offline, tampilkan ke layar (konversi dari Entity ke Movie)
                        // kembali menjadi wujud Movie (UI) agar bisa digambar di layar
                        val offlineMovies = savedEntities.map { entity ->
                            Movie(
                                id = entity.id,
                                title = entity.title,
                                overview = entity.overview,
                                posterPath = entity.posterPath,
                                releaseDate = entity.releaseDate,
                                voteAverage = entity.voteAverage
                            )
                        }
                        // Tampilkan film dari brankas
                        _homeState.value = HomeUiState.Success(offlineMovies)
                    } else {
                        // Jika internet mati DAN brankas benar-benar kosong
                        _homeState.value = HomeUiState.Error("Tidak ada internet dan belum ada film yang disimpan.")
                    }
                }
            }
        }
    }

    // Fungsi menyimpan film ke Room Database (Offline Mode)
    fun saveMovie(movie: Movie) {
        viewModelScope.launch { // Coroutines: Membuka thread latar belakang
            // Ubah format Movie (dari API) menjadi MovieEntity (untuk Room)
            val entity = MovieEntity(
                id = movie.id,
                title = movie.title,
                overview = movie.overview,
                posterPath = movie.posterPath,
                releaseDate = movie.releaseDate,
                voteAverage = movie.voteAverage
            )
            repository.saveMovieToDb(entity) // Perintah diteruskan ke Repository
        }
    }
}

// Pabrik untuk membuat instance ViewModel dengan membawa Repository
class MovieViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}