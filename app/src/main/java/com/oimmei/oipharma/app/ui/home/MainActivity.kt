package com.oimmei.oipharma.app.ui.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.oimmei.oipharma.app.ui.common.BottomNavigationBar
import com.oimmei.oipharma.app.ui.common.Constants
import com.oimmei.oipharma.app.ui.home.viewmodel.HomeViewModel
import com.oimmei.oipharma.app.ui.profile.ProfileScreen
import com.oimmei.oipharma.app.ui.profile.viewmodel.ProfileViewModel
import com.oimmei.oipharma.app.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    val TAG: String = MainActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
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

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    private fun Scaffold() {
        var title by remember { mutableStateOf("Home") }
        val navController = rememberNavController()
        AppTheme {
            Scaffold(
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
                        startDestination = "home",

                        // Set the padding provided by scaffold
                        modifier = Modifier.padding(paddingValues = paddingValues),

                        builder = {
                            // route : Home
                            composable("home") {
                                title = "Home"
//                                val factory = HomeViewModel.HomeViewModelFactory(false)
                                val viewModel = viewModel<HomeViewModel>(
                                    viewModelStoreOwner = this@MainActivity,
                                    key = "homeViewModel",
                                    factory = HomeViewModel.HomeViewModelFactory(false),
                                    extras = CreationExtras.Empty
                                )
                                HomeScreen(
                                    activity = this@MainActivity,
                                    navController = navController,
                                    viewModel = viewModel
                                )
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