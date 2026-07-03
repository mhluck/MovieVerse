# MovieVerse - Jetpack Compose

**Nama:** Mohammad Luqman Hakim 
**NIM:** 235410085

Aplikasi Android yang menyajikan daftar film populer secara *real-time* dan menyediakan fitur penyimpanan lokal (bookmark). **Masalah yang diselesaikan oleh aplikasi ini adalah:** membantu pengguna melacak katalog film favorit mereka dengan mudah dan memecahkan masalah keterbatasan akses dengan memungkinkan pengguna tetap dapat melihat daftar film yang disimpan meskipun sedang tidak ada koneksi internet (Offline Mode).

## Fitur Utama & Implementasi Teknis

Berikut adalah detail teknis mengenai implementasi fitur dan materi dalam proyek ini:

### 1. Material Design (UI & Layout)
Aplikasi ini menggunakan komponen **Material 3** secara penuh tanpa XML.
* **Komponen Standar:** Menggunakan `Scaffold` untuk struktur dasar layar, `TopAppBar` untuk navigasi atas, `FloatingActionButton` untuk tombol simpan, dan `Card` untuk membungkus setiap item film dengan efek elevasi (bayangan).
* **Tata Letak Responsif:** Halaman detail menggunakan sensor orientasi layar (`LocalConfiguration`). Jika *portrait*, susunan menggunakan `Column` (atas-bawah). Jika *landscape*, susunan menggunakan `Row` (kiri-kanan) agar proporsional.

### 2. Arsitektur MVVM (Model-View-ViewModel)
Aplikasi mengikuti pola arsitektur **MVVM**:
* **Model (Data Layer):** Mengelola sumber data dari API (Remote) dan Room Database (Local) melalui kelas `MovieRepository`.
* **View (UI Layer):** Murni bertugas menampilkan UI berdasarkan *state* yang diberikan.
* **ViewModel (Logic Layer):** `MovieViewModel` mengelola *UI State* (Loading, Success, Error) menggunakan `StateFlow`. Jika internet mati (kondisi Error jaringan), ViewModel secara cerdas akan mengambil data dari Room Database sebagai fallback.

### 3. Navigasi Jetpack Compose
Mengimplementasikan navigasi perpindahan layar yang terpusat:
* **NavHost & NavController:** Diatur di dalam `MovieApp.kt` untuk berpindah dari `HomeScreen` ke `DetailScreen`.
* **Passing Data:** Rute navigasi membawa argumen berupa ID film (`"detail_screen/{movieId}"`), yang kemudian ditangkap oleh layar detail untuk menampilkan informasi spesifik.

### 4. Akses ke Internet & Offline Persistence
* **Retrofit & Coroutines:** Menggunakan pustaka Retrofit2 dan fungsi asinkron (`suspend fun` & `viewModelScope.launch`) untuk mengambil data JSON secara *real-time* dari endpoint TMDB.
* **Room Database (Nilai Tambah):** Menggunakan KSP (Kotlin Symbol Processing) untuk membuat SQLite lokal. Film dapat disimpan ke memori HP dan dibaca secara asinkron menggunakan aliran data `Flow`.

### 5. Parsing Data
Data JSON dari API dikonversi menjadi objek Kotlin menggunakan **Gson**:
* Anotasi `@field:SerializedName("...")` digunakan pada *data class* untuk memetakan nama variabel dengan gaya penulisan *snake_case* dari JSON TMDB (seperti `poster_path`) menjadi *camelCase* di Kotlin (`posterPath`).

### 6. Menampilkan Data & Gambar
* **LazyVerticalGrid Adaptif:** Menampilkan galeri film secara efisien. Grid diatur secara `Adaptive(minSize = 160.dp)` sehingga jumlah kolom otomatis bertambah (3-5 kolom) saat layar HP dalam posisi *landscape*.
* **Coil (AsyncImage):** Memuat poster film beresolusi tinggi langsung dari URL internet secara asinkron, lengkap dengan penanganan *placeholder* (gambar sementara saat *loading*) dan *error state* (saat gambar gagal dimuat).

---

## Struktur Direktori Proyek (Arsitektur MVVM)

```text
MovieVerse/
│
├── README.md                 // Berisi dokumentasi proyek, deskripsi fitur, dokumentasi API, dan cara instalasi..
├── build.gradle.kts          // (Project Level). Mengatur plugin utama proyek. Isinya ditambahkan deklarasi plugin KSP.
│
├── gradle/
│   └── libs.versions.toml    // Version Catalog. Tempat menyimpan daftar versi library, termasuk mendaftarkan versi Kotlin dan KSP.
│
└── app/
    ├── build.gradle.kts      // (Module Level). Berisi daftar library (Retrofit, Room, Coil), dan aktivasi KSP.
    │
    └── src/
        └── main/
            ├── AndroidManifest.xml  // File konfigurasi sistem aplikasi. Isinya ditambahkan izin akses internet (INTERNET permission).
            │
            └── java/
                └── id/luqman/movieverse/
                    │
                    ├── MainActivity.kt      // Titik masuk utama aplikasi. Isinya merakit/menginisialisasi Retrofit, Room, Repository, dan ViewModel sebelum menjalankan UI Compose.
                    │
                    ├── data/                // FOLDER MODEL (Data Layer) - Mengurus semua pengambilan dan penyimpanan data.
                    │   ├── local/           // --> Sub-folder untuk brankas offline (Room Database).
                    │   │   ├── MovieEntity.kt   // Cetakan data (Tabel) khusus untuk disimpan ke database HP.
                    │   │   ├── MovieDao.kt      // Berisi perintah/kueri SQL (seperti fungsi simpan dan ambil data) untuk berinteraksi dengan tabel database.
                    │   │   └── MovieDatabase.kt // File penyatu Entity dan Dao yang berfungsi sebagai brankas utama Room.
                    │   │
                    │   ├── remote/          // --> Sub-folder untuk internet/jaringan (Retrofit).
                    │   │   ├── Movie.kt         // Data class sebagai cetakan untuk satu film dari teks JSON API TMDB.
                    │   │   ├── MovieResponse.kt // Data class keranjang yang menampung daftar film (results) dari JSON TMDB.
                    │   │   └── TmdbApi.kt       // Interface pengantar jaringan berisi alamat endpoint REST API TMDB.
                    │   │
                    │   └── repository/      // --> Sub-folder untuk Manajer Data.
                    │       └── MovieRepository.kt // Pengatur kapan aplikasi harus mengambil data dari internet (Retrofit) atau brankas lokal (Room).
                    │
                    ├── viewmodel/           // FOLDER LOGIKA (ViewModel Layer) - Otak pengatur aplikasi.
                    │   └── MovieViewModel.kt  // Mengelola status UI (Loading/Success/Error), menyimpan API Key, menjalankan proses latar belakang (Coroutines), dan mengatur logika jatuh ke mode offline jika internet mati.
                    │
                    └── ui/                  // FOLDER TAMPILAN (View Layer) - Semua hal yang berkaitan dengan layar.
                        ├── navigation/      // --> Sub-folder pengatur perpindahan layar.
                        │   └── MovieApp.kt    // Bertindak sebagai NavHost. Mengatur rute antar layar dan mengoper data film dari Room (status tersimpan) ke layar-layar.
                        │
                        └── screens/         // --> Sub-folder berisi kanvas layar antarmuka.
                            ├── HomeScreen.kt  // Halaman utama. Berisi LazyVerticalGrid adaptif untuk galeri film, indikator memuat, pesan error, serta logika ikon pita.
                            └── DetailScreen.kt// Halaman detail. Menampilkan poster besar, sinopsis, dan detektor layar (menggunakan Row untuk landscape, dan Column untuk portrait).
```

## Dokumentasi API

Aplikasi ini menggunakan layanan data publik dari **The Movie Database (TMDB) API**.

* **Base URL**: `https://api.themoviedb.org/3/`
* **Endpoint yang Digunakan**: `GET movie/popular`
* **Query Parameter**: `api_key` (Diperlukan untuk autentikasi permintaan data).
* **Image Base URL**: `https://image.tmdb.org/t/p/w500/` (Digunakan untuk menggabungkan `poster_path` dari JSON menjadi bentuk gambar beresolusi 500px yang siap dirender).

## Screenshot Aplikasi

| Halaman Utama (Portrait) | Halaman Utama (Landscape) |
| :---: | :---: |
| ![Home](screenshots/HalamanUtama(Potrait).png) | ![Home Landscape](screenshots/HalamanUtama(Landscape).png) |


| Halaman Detail (Portrait) | Halaman Detail (Landscape) |
| :---: | :---: |
| ![Detail](screenshots/HalamanDetail(Potrait).png) | ![Detail](screenshots/HalamanDetail(Landscape).png) |

## Cara Menjalankan Proyek
1. Clone repositori ini.
2. Buka di Android Studio.
3. Pastikan perangkat atau emulator Anda memiliki koneksi internet.
4. Klik Run untuk menginstal dan menjalankan aplikasi.