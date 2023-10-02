package com.oimmei.oipharma.app.ui.home

import android.Manifest
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.oimmei.oipharma.app.ui.home.viewmodel.HomeViewModel
import com.oimmei.oipharma.app.ui.theme.AppTheme
import com.oimmei.testanything.utils.LocationHelper


/**
 * Created by Andrea Fastame
 * @email andrea@oimmei.com
 * @since 29/09/2023 - 13:15
 * Copyright Oimmei Digital Consulting Srl 2015-2023 - www.oimmei.com
 */

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    activity: ComponentActivity,
    navController: NavController,
    viewModel: HomeViewModel
) {

    val TAG: String = "HomeScreen"
    val factory = HomeViewModel.HomeViewModelFactory(aValue = false)
    val viewModel = ViewModelProvider(activity, factory)[HomeViewModel::class.java]
    var address: String? by remember { mutableStateOf(null) }

    var location: Location? by remember { mutableStateOf(null) }

    // Location permissions state
//    val locationPermissionsState = rememberMultiplePermissionsState(
//        permissions = listOf(
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.ACCESS_COARSE_LOCATION
//        )
//    )
    val locationPermissionState =
        rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)

    LaunchedEffect(key1 = locationPermissionState, block = {
        if (locationPermissionState.status.isGranted) LocationHelper.getUserLocation(activity) { loc, err ->
            err?.run {
                location = null
                Toast.makeText(
                    activity, "Impossbile recuperare la posizione dell'utente", Toast.LENGTH_SHORT
                ).show()
                Log.w(TAG, err.message ?: "Impossible recuperare la posizione dell'utente")
            } ?: run {
                location = loc
                loc?.run {
                    LocationHelper.geoCode(activity, this) { anAddress: String? ->
                        address = anAddress
                    }
                }
            }

        } else {
            Toast.makeText(activity, "Permessi di localizzazione non concessi", Toast.LENGTH_SHORT)
                .show()
        }

    })
    AppTheme {
        Column(Modifier.fillMaxSize().padding(all = 16.dp)) {
            if (locationPermissionState.status.isGranted) {
                Text(
                    text = address
                        ?: "Permessi di localizzazione non concessi"
                )
            } else Column {
                val textToShow = if (locationPermissionState.status.shouldShowRationale) {
                    // If the user has denied the permission but the rationale can be shown,
                    // then gently explain why the app requires this permission
                    "I permessi di localizzazioni sono importanti per questa App!!!"
                } else {
                    // If it's the first time the user lands on this feature, or the user
                    // doesn't want to be asked again for this permission, explain that the
                    // permission is required
                    "Sono richiesti i permessi di localizzazione per l'App. " + "Vuoi concederli ora?"
                }
                Text(textToShow)
                Button(onClick = { locationPermissionState.launchPermissionRequest() }) {
                    Text("Richiedi permessi")
                }
            }
            Spacer(modifier = Modifier.size(16.dp))
            Text(text = "HOme")
        }
    }
}