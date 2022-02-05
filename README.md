# Overview
This software is an Android application that acts as a scoreboard for a game of wiffleball. It displays the possible actions to the user through a series of menus each containing buttons. The user inputs the specific actions as they occur, and the updated state of the game is displayed on the screen. The user also has the option of saving the current game in progess to system memory. This allows the game to be loaded and completed at another time, even if the app is closed and the phone is shutdown.

I really enjoy playing wiffleball because it can be played with a small group of people. It can even be played with as few as two people. This is accomplished by placing marks around the backyard to indicate an out, single, double, triple, or homerun. Pretend baserunners are moved along the bases according to the results of each ball hit. However, it is often very easy to forget how many balls, strikes, outs, baserunners, and runs each person has. This was my purpose for creating the Wiffleball Scorkeeper App. The app can be used on any Android phone while the wiffleball game is being played. The user inputs each action as it occurs, and the app updates and displays the current game state, so that the players do not forget. The app also keeps track of all winning conditions. Any current game in progress can be saved into system memory. This allows the game to be paused when it is time for dinner, and completed at another time, even if the app has been closed and the phone shutdown.

An overview of Android development using the Java programming language, and a demonstration of the Wiffleball Scorekeeper App can be found here: [Demonstration of Android Development Basic Elements Using The Java Programming Language](https://youtu.be/ZslNo01Z7OU)

# Development Environment
* Android Studio Artic Fox | 2020.3.1 Patch 4
* Java Version 11.0.10 (Embedded with Android Studio)
* Google's Gson 2.8.9 library

# Useful Websites
* [Android Developer Fundamentals (Version 2) — Concepts](https://google-developer-training.github.io/android-developer-fundamentals-course-concepts-v2/unit-1-get-started/lesson-1-build-your-first-app/1-1-c-your-first-android-app/1-1-c-your-first-android-app.html) (Official Tutorial Site)
* [Codelabs for Android Developer Fundamental](https://developer.android.com/courses/fundamentals-training/toc-v2) (Official Tutorial Site)
* [Starting Activities with an Intent](https://developer.android.com/guide/components/intents-filters) (Official Site)
* [Saving data in Shared Preferences](https://developer.android.com/training/data-storage/shared-preferences) (Official Site)
* [How to create an Android app that persistently saves custom objects](https://levelup.gitconnected.com/how-to-create-an-android-app-that-persistently-saves-custom-objects-5362f277cb29) (Tutorial Site)

# Future Work
* Confirmation notifications, such as when overwriting/deleting a previously stored game.
* Possible optimization of undo/redo functionality
* Possibly rewriting the GameAndroid wrapper class to extend the built-in Application class.