## MovieVerse

Aplikasi ini menyajikan daftar film populer secara *real-time* dari internet dan memiliki fitur penyimpanan lokal cerdas yang memungkinkan pengguna untuk tetap mengakses daftar film favorit pengguna meskipun sedang tidak ada koneksi internet (Offline Mode).

### Fitur Utama
* **Daftar Film Real-time**: Menampilkan daftar film populer yang selalu diperbarui langsung dari The Movie Database (TMDB).
* **Mode Responsif (Adaptive Grid)**: Layar utama menggunakan sistem grid yang secara cerdas dan otomatis membagi jumlah kolom kartu film agar proporsional, baik saat HP dalam keadaan tegak (*portrait*) maupun mendatar (*landscape*).
* **Fitur Bookmark Interaktif**: Pengguna dapat menyimpan film ke dalam *database* lokal menggunakan tombol ikon pita (bookmark). Ikon ini memberikan umpan balik visual secara langsung (berubah warna) untuk membedakan film yang sudah disimpan dan yang belum.
* **Offline Mode Dinamis**: Aplikasi memiliki penanganan status jaringan yang mulus. Jika koneksi internet terputus, aplikasi tidak akan *crash*, melainkan secara otomatis membuka *database* lokal dan menampilkan daftar film yang telah disimpan sebelumnya.
* **State Management Cerdas**: Menangani status *Loading*, *Success*, dan *Error* secara responsif tanpa membekukan antarmuka pengguna.

### Teknologi & Arsitektur
Aplikasi ini dikembangkan menggunakan **Kotlin** murni dengan spesifikasi berikut:
* **UI Framework**: **Jetpack Compose** (Deklaratif UI penuh, tanpa menggunakan XML sama sekali).
* **Arsitektur**: **MVVM (Model-View-ViewModel)** dengan pemisahan tanggung jawab yang ketat antara Data, UI, dan Logika.
* **Network & Parsing**: **Retrofit2** & **Gson** untuk komunikasi REST API dan ekstraksi data JSON.
* **Local Persistence (Caching)**: **Room Database** (SQLite) dengan dukungan kompiler **KSP** (Kotlin Symbol Processing) yang cepat dan efisien.
* **Image Loading**: **Coil Compose** (AsyncImage) untuk memuat dan menyajikan poster film beresolusi tinggi secara *asynchronous*.
* **Asynchronous Programming**: **Coroutines** & **StateFlow** untuk mengalirkan data secara *real-time* dari latar belakang ke UI.

### Struktur Direktori Proyek
Berikut adalah struktur direktori aplikasi dengan standar arsitektur MVVM:

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

### Dokumentasi API

Aplikasi ini menggunakan layanan data publik dari **The Movie Database (TMDB) API**.

* **Base URL**: `https://api.themoviedb.org/3/`
* **Endpoint yang Digunakan**: `GET movie/popular`
* **Query Parameter**: `api_key` (Diperlukan untuk autentikasi permintaan data).
* **Image Base URL**: `https://image.tmdb.org/t/p/w500/` (Digunakan untuk menggabungkan `poster_path` dari JSON menjadi bentuk gambar beresolusi 500px yang siap dirender).

### Screenshot Aplikasi

| Halaman Utama (Portrait) | Halaman Utama (Landscape) |
| :---: | :---: |
| ![Home](screenshots/HalamanUtama(Potrait).png) | ![Home Landscape](screenshots/HalamanUtama(Landscape).png) |


| Halaman Detail (Portrait) | Halaman Detail (Landscape) |
| :---: | :---: |
| ![Detail](screenshots/HalamanDetail(Potrait).png) | ![Detail](screenshots/HalamanDetail(Landscape).png) |