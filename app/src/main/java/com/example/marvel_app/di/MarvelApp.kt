package com.example.marvel_app.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MarvelApp : Application() {
    // This initializes Hilt in the application context
}
