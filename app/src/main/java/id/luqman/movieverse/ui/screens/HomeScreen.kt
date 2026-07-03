package id.luqman.movieverse.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import id.luqman.movieverse.data.remote.Movie
import id.luqman.movieverse.viewmodel.HomeUiState

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    savedMovieIds: List<Int>, // Daftar ID film yang tersimpan di database lokal
    onMovieClick: (Int) -> Unit,
    onSaveClick: (Movie) -> Unit
) {
    // Menentukan apa yang harus ditampilkan berdasarkan status (Loading, Error, atau Success)
    when (uiState) {
        is HomeUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator() // Animasi berputar saat memuat data
            }
        }
        is HomeUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = uiState.message, color = MaterialTheme.colorScheme.error)
            }
        }
        is HomeUiState.Success -> {
            // Menampilkan daftar film dalam bentuk grid yang otomatis menyesuaikan lebar layar
            LazyVerticalGrid(
                // Jika portrait muat 2 kolom. Jika landscape, akan otomatis muat 3-5 kolom
                // karena lebar minimal setiap kartu adalah 160.dp
                columns = GridCells.Adaptive(minSize = 160.dp),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(uiState.movies) { movie ->
                    MovieCard(
                        movie = movie,
                        isSaved = savedMovieIds.contains(movie.id), // Cek apakah film ini sudah disimpan
                        onClick = { onMovieClick(movie.id) },
                        onSaveClick = { onSaveClick(movie) }
                    )
                }
            }
        }
    }
}

@Composable
fun MovieCard(
    movie: Movie,
    isSaved: Boolean, // Menerima status dari HomeScreen
    onClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    // // Kartu untuk menampilkan satu item film

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Box {
                // Menampilkan poster film dari internet
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
                    contentDescription = movie.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().height(220.dp),
                    placeholder = painterResource(android.R.drawable.ic_menu_report_image),
                    error = painterResource(android.R.drawable.ic_delete)
                )

                // Tombol bookmark untuk menyimpan/menghapus film
                IconButton(
                    onClick = onSaveClick,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        // Jika isSaved true = Pita Penuh. Jika false = Garis Tepi saja.
                        imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = "Simpan",
                        // Jika tersimpan warnanya emas, jika belum warnanya putih semi-transparan
                        tint = if (isSaved) Color(0xFFFFD700) else Color.White.copy(alpha = 0.8f)
                    )
                }
            }

            // Menampilkan judul dan rating film
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = movie.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "⭐ ${movie.voteAverage}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}