package com.casajojo.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.casajojo.app.ui.navigation.AppNavigation
import com.casajojo.app.ui.theme.CasaJoJoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CasaJoJoTheme {
                AppNavigation()
            }
        }
    }
}