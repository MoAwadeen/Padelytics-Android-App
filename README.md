# Padelytics - Android App

Padelytics is an Android application built for padel players. It combines AI-powered match analysis with community features like court booking, tournament discovery, and a padel equipment shop — all in one place.

## Features

### AI Match Analysis
Upload a video of your padel match and receive a detailed breakdown of your performance:
- Player trajectories and heatmaps
- Speed, distance, and acceleration metrics
- Zone presence percentages
- Ball trajectory and hit locations
- Reaction time, shot effectiveness, and stamina analysis
- Role identification (e.g., defender, attacker) with personalized advice
- Radar performance chart across multiple metrics
- Animated frame-by-frame player/ball visualization
- PDF export of analysis results

### Court Booking
Browse padel courts near you, view details (location, rating, pricing, photos), and book via direct links. Filter by city and player count (2 or 4 players).

### Tournaments
Discover upcoming padel tournaments with details on location, prize, registration fees, date, and type. Register directly through external links.

### Shop
Browse padel equipment sourced from the Amazon product API. View product details and navigate to listings to purchase.

### Favorites
Save courts, tournaments, and products to a personal favorites list for quick access.

### User Profile
Manage your profile including name, username, city, skill level, gender, and profile photo. Update your username at any time.

### About Sections
- **About the Game** — Learn the basics of padel
- **About the App** — Overview of app features
- **About Us** — Meet the development team

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Navigation | Navigation Compose |
| Architecture | MVVM (ViewModel per feature) |
| Auth | Firebase Authentication (Email/Password + Google Sign-In) |
| Database | Firebase Firestore |
| File Storage | Firebase Storage + Cloudinary |
| Networking | Retrofit + OkHttp |
| Animations | Lottie Compose |
| Image Loading | Coil |
| Local Storage | DataStore Preferences |
| HTML Parsing | Jsoup |

## Project Structure

```
app/src/main/java/grad/project/padelytics/
├── MainActivity.kt
├── appComponents/          # Shared UI components and utilities
├── data/                   # App-wide data models and preferences
│   ├── UserModel.kt
│   ├── UserPreferences.kt
│   ├── MatchNotificationHelper.kt
│   └── NotificationPreferencesManager.kt
├── features/
│   ├── about/              # About the game, app, and team screens
│   ├── analysis/           # Match analysis UI, data models, PDF export
│   ├── auth/               # Login, signup, Google Sign-In
│   ├── courtBooking/       # Court listing and detail screens
│   ├── favorites/          # Saved courts, tournaments, products
│   ├── home/               # Home screen
│   ├── profile/            # User profile management
│   ├── results/            # Previous match results
│   ├── shop/               # Product browsing via Amazon API
│   ├── tournaments/        # Tournament listing and details
│   └── videoUpload/        # Video upload and cloud processing
├── navigation/
│   ├── AppNavigation.kt    # NavHost and route definitions
│   └── Routes.kt           # Route constants
└── ui/theme/               # Colors, typography, and theme
```

## Requirements

- Android **API 26+** (Android 8.0 Oreo and above)
- Target SDK: **35**
- A `google-services.json` file placed in `app/` (Firebase project configuration)

## Building the Project

1. Clone the repository.
2. Add your `google-services.json` to the `app/` directory.
3. Open the project in **Android Studio Ladybug** or newer.
4. Sync Gradle and run on an emulator or physical device.

```bash
./gradlew assembleDebug
```

## Permissions

| Permission | Purpose |
|---|---|
| `INTERNET` | Network requests (Firebase, APIs, video upload) |
| `ACCESS_NETWORK_STATE` | Check connectivity before requests |
| `POST_NOTIFICATIONS` | Match result push notifications |
