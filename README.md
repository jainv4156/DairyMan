# DairyMan - Milkman App

**DairyMan** is an Android application designed to simplify the daily operations of milkmen. The app allows milkmen to log customer transactions, keep a record of daily deliveries, and manage accounts effortlessly with cloud synchronization.

![App Screenshot](link-to-screenshot)

## Features
- **Customer Management**: Add, update, and delete customer details with ease.
- **Daily Transactions**: Record and review daily deliveries for each customer.
- **Account Balancing**: One-click option to update all customer balances at the end of the day.
- **Cloud Sync**: Customer data is synchronized with the cloud for easy access from any device.
- **Offline Support**: Keep working even without an internet connection; data will sync when you're back online.
- **User-Friendly Interface**: Built with Jetpack Compose, ensuring a smooth and intuitive user experience.

## Technology Stack
- **Kotlin**
- **Jetpack Compose**
- **Room Database**
- **Coroutines & Flow**
- **Firebase Firestore** for cloud syncing

## How to Use
Since the app is currently not available on the Play Store, you can download and install the APK from the following link:

[Google Drive Link](your-google-drive-link) or [Website Link](your-website-link)

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
