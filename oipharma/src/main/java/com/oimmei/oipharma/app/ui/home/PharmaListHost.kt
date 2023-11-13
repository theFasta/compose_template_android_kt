package com.oimmei.oipharma.app.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.oimmei.oipharma.app.comms.model.Pharmacy
import com.oimmei.oipharma.app.ui.home.viewmodel.PharmaDetailViewModel
import com.oimmei.oipharma.app.ui.theme.OIPharmaTheme
import com.oimmei.oipharma.app.utils.Constants


/**
 * Created by Andrea Fastame
 * @email andrea@oimmei.com
 * @since 10/11/2023 - 09:25
 * Copyright Oimmei Digital Consulting Srl 2015-2023 - www.oimmei.com
 */

@Composable
fun PharmaListHost(activity: MainActivity) {
    val navController = rememberNavController()
    val title = remember { mutableStateOf("Farmacie") }
    OIPharmaTheme {
        NavHost(
            navController = navController,
            // set the start destination as home
            startDestination = Constants.Companion.ROUTES_PHARMA_LIST.pharmaList.route,

            // Set the padding provided by scaffold
            modifier = Modifier,

            builder = {
                // route : Home
                composable(Constants.Companion.ROUTES_PHARMA_LIST.pharmaList.route) {
                    title.value = "Farmacie"
                    PharmaListScreen(
                        activity = activity,
                        navController = navController,
                    )
                }
                composable(
                    Constants.Companion.ROUTES_PHARMA_LIST.pharmaDetail.route,
//                    arguments = listOf(navArgument("json") { type = NavType.StringType })
                )
                { backStackEntry ->
//                    val argsJSON = backStackEntry.arguments?.getString("json")
//                    val pharmacy = Gson().fromJson(argsJSON, Pharmacy::class.java)
//                    title = .listName?: stringResource(R.string.dettaglio_lista)
                    title.value = "Farmacia"
                    PharmaDetailScreen(
                        activity = activity,
                        navController = navController
                    )
                }
            })
    }
}