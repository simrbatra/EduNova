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
