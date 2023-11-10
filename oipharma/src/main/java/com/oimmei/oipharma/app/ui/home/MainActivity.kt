package com.oimmei.oipharma.app.ui.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.oimmei.oipharma.app.ui.common.BottomNavigationBar
import com.oimmei.oipharma.app.ui.common.Constants
import com.oimmei.oipharma.app.ui.profile.ProfileScreen
import com.oimmei.oipharma.app.ui.profile.viewmodel.ProfileViewModel
import com.oimmei.oipharma.app.ui.theme.OIPharmaTheme

class MainActivity : ComponentActivity() {
    val TAG: String = MainActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OIPharmaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold()
                }
            }
        }
    }

    @Preview(showBackground = true, group = "scaffold")
    @Composable
    fun ScaffoldPreview() {
        Scaffold()
    }

    @OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
    @Composable
    private fun Scaffold() {
        var title by remember { mutableStateOf("Home") }
        val navController = rememberNavController()
        OIPharmaTheme {
            androidx.compose.material3.Scaffold(
                bottomBar = {
                    BottomNavigationBar(
                        navController = navController,
                        items = Constants.BottomNavItems
                    )
                },
                containerColor = MaterialTheme.colorScheme.surface,
                content = { paddingValues ->
                    NavHost(
                        navController = navController,
                        // set the start destination as home
                        startDestination = "pharmaListHost",

                        // Set the padding provided by scaffold
                        modifier = Modifier.padding(paddingValues = paddingValues),

                        builder = {
                            // route : Home
                            composable("pharmaListHost") {
                                title = "Home"
                                PharmaListHost(activity = this@MainActivity)
                            }

                            // route : profile
                            composable("profile") {
                                title = "Profilo utente"
                                val factory = ProfileViewModel.ProfileViewModelFactory(true)
                                val viewModel =
                                    ViewModelProvider(
                                        this@MainActivity,
                                        factory
                                    )[ProfileViewModel::class.java]
//                        val viewModel = viewModel(ProfileViewModel::class.java)
//                        viewModel.shopkeeper = true
//                                UserHelper.fillPlayerProfileViewModel(viewModel)
                                ProfileScreen(
                                    activity = this@MainActivity,
                                    viewModel = viewModel,
                                    navController = navController
                                ) {

                                }
                            }
                        })
                }
            )
        }
    }
}