package com.oimmei.oipharma.app.ui.profile

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.oimmei.oipharma.app.ui.profile.viewmodel.ProfileViewModel
import com.oimmei.oipharma.app.ui.theme.AppTheme

@Composable
fun ProfileScreen(
    activity: ComponentActivity,
    navController: NavController,
    viewModel: ProfileViewModel,
    itemListener: () -> Unit
) {
    AppTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(text = "Profile screen")
        }
    }
}