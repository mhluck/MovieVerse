package id.luqman.movieverse.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import id.luqman.movieverse.data.remote.Movie

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    movie: Movie,
    isSaved: Boolean,
    onBackClick: () -> Unit,
    onSaveClick: (Movie) -> Unit
) {
    // Membaca orientasi layar (Portrait atau Landscape)
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val scrollState = rememberScrollState()

    Scaffold(
        // Bagian atas layar (Top Bar) dengan tombol kembali
        topBar = {
            TopAppBar(
                title = { Text("Detail Film") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        },
        // Tombol simpan melayang yang berubah ikon/warna berdasarkan status tersimpan
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onSaveClick(movie) },
                containerColor = if (isSaved) Color(0xFFFFD700) else MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = "Simpan Film",
                    tint = if (isSaved) Color.Black else Color.White
                )
            }
        }
    ) { paddingValues ->

        // Memilih tata letak (Layout) berdasarkan orientasi layar
        if (isLandscape) {
            // Tampilan Landscape: Gambar di kiri, detail teks di kanan
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
                    contentDescription = movie.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(220.dp)
                        .fillMaxHeight()
                        .background(Color.DarkGray)
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                        .verticalScroll(scrollState)
                ) {
                    MovieInfoContent(movie = movie)
                }
            }
        } else {
            // Tampilan Portrait: Gambar di atas, detail teks di bawah
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(scrollState)
            ) {
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
                    contentDescription = movie.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth() .height(500.dp) .background(Color.DarkGray),
                    placeholder = painterResource(android.R.drawable.ic_menu_report_image),
                    error = painterResource(android.R.drawable.ic_delete)
                )

                Column(modifier = Modifier.padding(16.dp)) {
                    MovieInfoContent(movie = movie)
                }
            }
        }
    }
}

// Komponen bantu agar konten teks tidak perlu ditulis berulang kali
@Composable
fun MovieInfoContent(movie: Movie) {
    Text(
        text = movie.title,
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(8.dp))

    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = "Rating",
            tint = Color.Yellow,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = "${movie.voteAverage}", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)

        Spacer(modifier = Modifier.width(16.dp))

        Text(text = "Rilis: ${movie.releaseDate ?: "-"}", color = Color.Gray)
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = "Sinopsis",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = movie.overview,
        fontSize = 15.sp,
        lineHeight = 24.sp
    )
}