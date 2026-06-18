plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "id.luqman.movieverse"
    compileSdk = 37

    defaultConfig {
        applicationId = "id.luqman.movieverse"
        minSdk = 31
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // 1. Navigation Compose (Untuk pindah halaman)
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // 2. Lifecycle & ViewModel Compose (Untuk State Management)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

    // 3. Retrofit & Gson (Untuk ambil data internet & Parsing JSON)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // 4. Room Database (Untuk Local Persistence / Offline)
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    // Catatan: Pastikan kamu menggunakan KSP atau KAPT di plugin atas.
    // Untuk Compose modern, biasanya kita tambahkan plugin "com.google.devtools.ksp" di atas
    // dan gunakan: ksp("androidx.room:room-compiler:$room_version")
    ksp("androidx.room:room-compiler:$roomVersion")

    // 5. Coil (Untuk memuat gambar AsyncImage dari URL internet)
    implementation("io.coil-kt:coil-compose:2.6.0")

    implementation("com.google.code.gson:gson:2.10.1")

    implementation("androidx.compose.material:material-icons-extended")
}