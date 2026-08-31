package com.erno.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.erno.app.navigation.NavGraph
import com.erno.app.ui.theme.ErnoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ErnoTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}
