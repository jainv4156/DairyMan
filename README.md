# DairyMan - Milkman App

**DairyMan** is an Android application designed to simplify the daily operations of milkmen. The app allows milkmen to log customer transactions, keep a record of daily deliveries, and manage accounts effortlessly with cloud synchronization.

# ScreenShots

<img src="https://github.com/user-attachments/assets/7dd0812d-8c82-4a65-85de-be48117731d2" alt="App Screenshot" width="400"/>
<img src="https://github.com/user-attachments/assets/51efa743-3a48-445b-9b3f-9f5528a13cb5" alt="App Screenshot" width="400"/>
<img src="https://github.com/user-attachments/assets/1fd244fa-67f8-47b4-895e-e643280203d2" alt="App Screenshot" width="400"/>
<img src="https://github.com/user-attachments/assets/f23b31c3-cad2-4d6a-b96c-68fddb86b013" alt="App Screenshot" width="400"/>



## Features
- **Customer Management**: Add, update, and delete customer details with ease.
- **Daily Transactions**: Record and review daily deliveries for each customer.
- **Account Balancing**: One-click option to update all customer balances at the end of the day.
- **Temporary Rate Adjustment**: Easily modify the rate of milk for specific days, helping with temporary price changes and easing daily calculations.
- **Cloud Sync**: Customer data is synchronized with the cloud automatically every 24 hours in the background.
- **History Management**: Review past transactions and deliveries for better tracking and reporting.
- **User-Friendly Interface**: Built with Jetpack Compose, ensuring a smooth and intuitive user experience.


## Technology Stack
- **Kotlin**
- **Jetpack Compose**
- **Room Database**
- **Coroutines & Flow**
- **Firebase Firestore** for cloud syncing

## How to Use
Since the app is currently not available on the Play Store, you can download and install the APK from the following link:

[Google Drive Link](https://drive.google.com/file/d/1ne4MR7jwj3nN5c1brkpNOVyq0L6TVUzP/view?usp=sharing) or [Website Link](your-website-link)

> **Note:** The backend API included in the source code is invalid. You need to set up your own Firebase project and add your API key in order to sync the app with the cloud.

### Steps to Set Up Your API
1. Go to the [Firebase Console](https://console.firebase.google.com/).
2. Create a new Firebase project.
3. Set up **Firestore** for cloud data syncing.
4. Obtain the `google-services.json` file and replace the existing one in the `app` directory of this project.
5. Add your Firebase API key in the `build.gradle` (Module: app) file.

## Installation

### Clone the Repository
```bash
git clone https://github.com/yourusername/DairyMan.git
cd DairyMan
```
### Build the Project
1. Open the project in **Android Studio**.
2. Sync the Gradle files to resolve any dependencies.
3. Ensure you have the correct API key:
   - Replace the existing `google-services.json` file in the `app` directory with your own.
   - Add your Firebase API key in the `build.gradle` (Module: app) file.
4. Connect your Android device or launch an emulator.
5. Click on the **Run** button to install the app on your device or emulator.

## What I Learned
Building the **DairyMan** app allowed me to deepen my understanding of several key technologies and concepts:

1. **Jetpack Compose**: Developed a clean and modern UI with Google's declarative UI toolkit, ensuring a user-friendly experience.
2. **Room Database**: Implemented local data storage with Room to manage customer details and daily transactions efficiently.
3. **Coroutines & Flow**: Leveraged Kotlin Coroutines and Flow to handle background tasks and real-time data flow seamlessly.
4. **Firebase Firestore**: Integrated Firestore for cloud data storage, enabling background synchronization every 24 hours.
5. **State Management & MVVM**: Used the Model-View-ViewModel (MVVM) architecture to ensure separation of concerns and smooth state management across the app.
6. **Services**: Utilized Android Services for background tasks such as syncing data and processing tasks outside the app's lifecycle.
7. **WorkManager**: Implemented WorkManager to handle background tasks that need guaranteed execution, like the 24-hour sync.
8. **Animations**: Added smooth animations to enhance user experience and make interactions more fluid and engaging.

