<<<<<<< HEAD
# EduNova AI Tutor

An Android-based AI-powered personal tutor designed for rural students, working offline with multilingual support and adaptive learning capabilities.

## 🎯 Project Overview

EduNova is an intelligent tutoring system that brings quality education to rural areas with limited connectivity. The app works primarily offline, understands regional languages, and adapts to each student's learning pace.

### Problem Statement (SIH Problem ID: 25019)
- Shortage of skilled teachers in rural schools
- Poor or no internet connectivity
- One-size-fits-all teaching methods
- Lack of personalized feedback and progress tracking
- Language barriers between students and course content

## ✨ Core Features

### 1. Offline AI Learning Engine
- Embedded small-scale language model for on-device inference
- Dynamic content difficulty adjustment
- Works without active internet (syncs when available)

### 2. Voice-Interactive Tutor
- Multilingual voice chat using speech-to-text and text-to-speech
- Supports regional languages (Hindi, Telugu, Tamil, Marathi, Bengali, etc.)
- Voice-based quizzes and oral feedback

### 3. Adaptive Learning & Assessment
- AI tracks student behavior (speed, wrong answers, attention span)
- Personalized lesson recommendations
- Mastery-based learning path optimization

### 4. Local Language Support
- NLP layer for translating NCERT/State Board syllabus into native languages
- ML Kit Translation for offline translation
- Phonetic pronunciation support

### 5. Smart Analytics Dashboard
- Progress tracking with visualizations
- Time spent, weak areas, and confidence level analysis
- Heatmaps and growth charts

### 6. Gamified Learning
- XP points, badges, and streaks
- Dynamic difficulty adjustment
- Achievement system

## 🛠️ Tech Stack

### Core Technologies
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM + Clean Architecture
- **Dependency Injection**: Hilt
- **Local Database**: Room
- **Async Operations**: Kotlin Coroutines & Flow

### AI & ML
- **TensorFlow Lite**: On-device inference
- **ML Kit**: Text recognition, translation, image labeling
- **Custom AI Engine**: Adaptive learning algorithms

### Additional Libraries
- **Navigation**: Navigation Compose
- **Networking**: Retrofit + OkHttp (for optional cloud sync)
- **AR**: ARCore (for visual learning tools)
- **Camera**: CameraX

## 📱 App Structure

```
app/
├── data/
│   ├── local/           # Room database entities and DAOs
│   └── repository/      # Repository implementations
├── domain/
│   └── model/           # Domain models
├── ai/                  # AI engines (Learning, Voice, Translation, etc.)
├── ui/
│   ├── screens/         # Compose screens
│   ├── navigation/      # Navigation setup
│   └── theme/           # App theming
└── di/                  # Dependency injection modules
```

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- JDK 17 or higher
- Android SDK 24+ (minimum SDK for better device compatibility)

### Installation

1. Clone the repository
```bash
git clone <repository-url>
cd EduNova
```

2. Open the project in Android Studio

3. Sync Gradle files

4. Build and run the app

### Configuration

1. **ML Models**: Place TensorFlow Lite models in `app/src/main/assets/` directory
2. **Language Models**: ML Kit will download translation models on first use (requires internet initially)
3. **Offline Mode**: The app works offline after initial setup

## 📋 Features Implementation Status

- [x] Project setup and architecture
- [x] Room database with entities
- [x] AI Learning Engine (skeleton)
- [x] Voice Tutor (STT/TTS integration)
- [x] Language Translator (ML Kit)
- [x] Adaptive Learning Engine
- [x] Gamification Engine
- [x] UI with Jetpack Compose
- [x] Navigation setup
- [ ] TensorFlow Lite model integration
- [ ] AR features (ARCore)
- [ ] Handwriting recognition
- [ ] Cloud sync implementation
- [ ] Full assessment system

## 🎓 Usage

1. **Student Profile**: Create or select a student profile
2. **Language Selection**: Choose preferred regional language
3. **Download Lessons**: Download lessons for offline access
4. **Start Learning**: Begin lessons with voice interaction
5. **Track Progress**: View analytics and progress dashboard
6. **Earn Rewards**: Collect XP, badges, and maintain streaks

## 🔧 Development

### Building the Project
```bash
./gradlew build
```

### Running Tests
```bash
./gradlew test
```

### Generating APK
```bash
./gradlew assembleRelease
```

## 📝 License

This project is developed for Smart India Hackathon (SIH) 2024.

## 🤝 Contributing

This is a hackathon project. Contributions and improvements are welcome!

## 📞 Contact

For questions or support, please contact the development team.

---

**Built with ❤️ for rural education**

=======
📘 Edu-Nova – Smart Learning Android App

Edu-Nova is an Android application designed to provide a smart, accessible, and user-friendly learning experience for students. It includes authentication, course content, video learning, quizzes, and a clean material-based UI.

🚀 Features
✔ User Authentication

Login / Signup

Firebase Authentication or Custom Backend (depending on your implementation)

✔ Dashboard

Displays available courses

Quick navigation to subjects

User profile quick access

✔ Courses Module

List of available courses

Each course has:

Description

PDF notes

Video lessons

Assignments / Quizzes

✔ Video Learning

Integrated video player (ExoPlayer / YouTube API)

✔ Quizzes

MCQ-based quizzes

Score calculation

Result screen

✔ Profile Management

Update name, email, password

View learning progress

Logout functionality

🏗 Project Structure
Edu-Nova/
│── app/
│   ├── manifests/
│   │   └── AndroidManifest.xml
│   ├── java/
│   │   └── com.edunova/         # Activities, Adapters, Models
│   ├── res/
│   │   ├── layout/              # XML layouts
│   │   ├── drawable/            # Icons & images
│   │   ├── values/              # Colors, themes, strings
│── build.gradle (Project)
│── app/build.gradle (Module)
│── gradle.properties
│── settings.gradle

🛠 Tech Stack
Component	Technology
Frontend	Java / Kotlin (depending on your implementation)
Architecture	XML UI + Activity + Adapter + Model
Video Player	ExoPlayer / YouTube Player
Authentication	Firebase Authentication
Database	Firebase Firestore / Realtime DB / Local SQLite
Design	Material Design Components
📱 Screenshots (Optional Section)

Add your app screenshots here

/screenshots/home.png  
/screenshots/login.png  
/screenshots/courses.png  

⚙️ Setup Instructions
1️⃣ Clone the repository
git clone https://github.com/your-username/Edu-Nova.git

2️⃣ Open in Android Studio

File → Open → Select Edu-Nova folder

3️⃣ Sync Gradle
4️⃣ Configure Firebase (if used)

Download google-services.json

Place inside:

app/google-services.json

5️⃣ Run the app

Select Android Emulator / Physical Device

Press ▶ Run

📦 Build & Release
Generate APK:
Build → Build Bundle(s)/APK(s) → Build APK(s)


APK will be created inside:

Edu-Nova/app/release/

🤝 Contributing

Contributions are welcome!
Feel free to submit issues, fork, and create pull requests.

🧑‍💻 Developed By

Simar 
Android Developer • Cloud Enthusiast

📄 License

This project is licensed under the MIT License.
>>>>>>> 8e5f04d175b53e4e330ba53399f011da26b4be62
