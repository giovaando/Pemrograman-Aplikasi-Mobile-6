# 📰 NewsReader068

Proyek aplikasi pembaca berita lintas platform (Android, iOS, Desktop) yang dibangun menggunakan **Kotlin Multiplatform (KMP)** dan **Compose Multiplatform**.

## 🚀 Fitur & API
- **API Sumber**: [JSONPlaceholder](https://jsonplaceholder.typicode.com) (Endpoint: `/posts`).
- **Data Model**: Mengambil ID, User ID, Title, dan Body. Gambar di-generate menggunakan `https://picsum.photos`.
- **Arsitektur**: Menggunakan *Repository Pattern* dengan pemisahan *State* (`UiState` bawaan: Loading, Success, Error).
- **Networking**: Menggunakan *Ktor Client* dan *Kotlinx Serialization*.

## 📸 Tangkapan Layar (Screenshots)
*(Tambahkan gambar screenshot di folder `/screenshots` dan tautkan di sini)*

| Loading State | Success State | Error State |
|:---:|:---:|:---:|
| ![Loading](link_ke_gambar) | ![Success](link_ke_gambar) | ![Error](link_ke_gambar) |

## 🛠️ Cara Menjalankan Aplikasi
- **Android**: `./gradlew :composeApp:installDebug`
- **iOS**: Buka `iosApp/iosApp.xcworkspace` di Xcode dan jalankan di Simulator.
- **Desktop**: `./gradlew :composeApp:run`
