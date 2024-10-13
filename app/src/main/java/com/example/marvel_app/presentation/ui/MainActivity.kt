package com.example.marvel_app.presentation.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.marvel_app.presentation.ui.navigation.Navigation
import com.example.marvel_app.presentation.viewmodel.CharacterDetailsViewModel
import com.example.marvel_app.presentation.viewmodel.CharacterViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


            setContent {
                Navigation()
                Log.d("Character_check", "Navigation content set")
            }



    }

}

