package com.example.fitnesstracker

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation() {
    val navController = rememberNavController()
    var expandedCardIndex by remember { mutableStateOf(-1) }

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginPage(navController = navController) }
        composable("dashboard") {
            DashboardPage(
                navController = navController
            )
        }
    }
}
