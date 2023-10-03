package com.oimmei.composetest.app.ui.common

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.oimmei.composetest.app.ui.theme.AppTheme
import com.oimmei.composetest.app.R


/**
 * Created by Andrea Fastame
 * @email andrea@oimmei.com
 * @since 21/08/2023 - 15:46
 * Copyright Oimmei Digital Consulting Srl 2015-2023 - www.oimmei.com
 */

data class BottomNavItem(
    val label: String,
    val icon: Int,
    val route: String,
)

object Constants {

    val BottomNavItems = listOf(
        BottomNavItem(
            label = "Home",
            icon = R.drawable.baseline_local_pharmacy_24,
            route = "home"
        ),
        BottomNavItem(
            label = "Utente",
            icon = R.drawable.baseline_account_circle_24,
            route = "profile"
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavigationBar(navController: NavHostController, canPop: Boolean, title: String) {
    val bgColor = MaterialTheme.colorScheme.primaryContainer
    val contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    if (canPop)
        TopAppBar(
            title = {
                //composable function for title
                Text(title)
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = bgColor,
                navigationIconContentColor = contentColor,
                actionIconContentColor = contentColor,
                titleContentColor = contentColor
            ),
            actions = {

            },
            navigationIcon = {
                if (canPop) {
                    IconButton(onClick = {
                        navController.navigateUp()
                    }) {
                        Icon(Icons.Rounded.ArrowBack, "Indietro")
                    }
                }
            },
        ) else
        TopAppBar(
            title = {
                //composable function for title
                Text("Compose Test")
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = bgColor,
                navigationIconContentColor = contentColor,
                actionIconContentColor = contentColor,
                titleContentColor = contentColor
            ),
            actions = {
            },
        )
}

@Composable
fun BottomNavigationBar(navController: NavHostController, items: List<BottomNavItem>) {
    AppTheme {
        BottomNavigation(
            elevation = 6.dp,
            backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
        ) {


            // observe the backstack
            val navBackStackEntry by navController.currentBackStackEntryAsState()

            // observe current route to change the icon
            // color,label color when navigated
            val currentRoute = navBackStackEntry?.destination?.route

            // Bottom nav items we declared
            items.forEach { navItem ->

                // Place the bottom nav items
                BottomNavigationItem(

                    // it currentRoute is equal then its selected route
                    selected = currentRoute == navItem.route,

                    // navigate on click
                    onClick = {
                        navController.navigate(navItem.route)
                    },

                    // Icon of navItem
                    icon = {
//                        Icon(imageVector = navItem.icon, contentDescription = navItem.label)
                        Icon(
                            painter = painterResource(id = navItem.icon),
                            contentDescription = navItem.label
                        )
                    },

                    // label
                    label = {
                        Text(text = navItem.label, fontSize = 12.sp)
                    },
                    alwaysShowLabel = false,
                    unselectedContentColor = MaterialTheme.colorScheme.onError.copy(
                        0.3f
                    ),
                    selectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}