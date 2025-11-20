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

>>>>>>> 8e5f04d175b53e4e330ba53399f011da26b4be62
