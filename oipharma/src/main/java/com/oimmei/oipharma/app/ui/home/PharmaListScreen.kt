package com.oimmei.oipharma.app.ui.home

import android.Manifest
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.oimmei.oipharma.app.R
import com.oimmei.oipharma.app.comms.model.Pharmacy
import com.oimmei.oipharma.app.ui.common.OnLifecycleEvent
import com.oimmei.oipharma.app.ui.home.viewmodel.PharmaDetailViewModel
import com.oimmei.oipharma.app.ui.home.viewmodel.PharmaListViewModel
import com.oimmei.oipharma.app.ui.theme.OIPharmaTheme
import com.oimmei.oipharma.app.utils.Constants
import java.text.NumberFormat


/**
 * Created by Andrea Fastame
 * @email andrea@oimmei.com
 * @since 08/11/2023 - 18:06
 * Copyright Oimmei Digital Consulting Srl 2015-2023 - www.oimmei.com
 */

val nf = NumberFormat.getNumberInstance().apply {
    maximumFractionDigits = 2
}

@Preview(showBackground = true, group = "screen")
@Composable
fun HomeFeedScreenPreview() {
    OIPharmaTheme {
        PharmaListScreen(activity = MainActivity(), navController = rememberNavController())
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PharmaListScreen(activity: MainActivity, navController: NavHostController) {
    val factory = PharmaListViewModel.HomeViewModelFactory(false)
    val viewModel = remember { factory.create(PharmaListViewModel::class.java) }
    var tabIndex by remember { mutableIntStateOf(0) }
    val tabs = arrayOf("Farmacie", "Servizi")
    val pharmacies = remember { viewModel.pharmacies }
    val services = remember { viewModel.services }
    val lazyListState = rememberLazyListState()

    val uistate = viewModel.uiState.collectAsState()

//    val currentLocation = viewModel.currentLocation.collectAsState()

    val permissionState =
        rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION) {
            if (it) {
                // viewModel.getPharmacies()
            }
        }

    LaunchedEffect(key1 = permissionState.status, block = {
        permissionState.launchPermissionRequest()
    })

//    LaunchedEffect(key1 = currentLocation, block = {
//        viewModel.getPharmacies()
//    })

    val coroutineScope = rememberCoroutineScope()

    OnLifecycleEvent { owner, event ->
        when (event) {
            Lifecycle.Event.ON_START -> {
//                coroutineScope.launch { viewModel.getUserLocation(activity) }
                if (permissionState.status.isGranted)
                    viewModel.getUserLocation(activity)
                else
                    viewModel.getPharmacies()
            }

            else -> {}
        }
    }

    OIPharmaTheme {

        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Card(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.surface)
                        .padding(vertical = 8.dp, horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(id = R.mipmap.big_pill),
                        contentDescription = "Pillolone",
                        modifier = Modifier.size(height = 48.dp, width = 48.dp)
                    )
                    Spacer(Modifier.size(16.dp))
                    Text(
                        text = "Farmacie",
                        color = Color.Black,
                        fontSize = TextUnit(32f, TextUnitType.Sp),
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
            Spacer(modifier = Modifier.size(8.dp))
            TabRow(selectedTabIndex = tabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(text = {
                        Text(
                            text = title,
                            fontWeight = FontWeight.Bold,
                            fontSize = TextUnit(18f, TextUnitType.Sp)
                        )
                    },
                        selected = (tabIndex == index),
                        onClick = { tabIndex = index }
                    )
                }
            }
            Spacer(modifier = Modifier.size(16.dp))
            when (tabIndex) {
                0 -> {
                    OutlinedTextField(
                        value = uistate.value.queryString,
                        onValueChange = viewModel::setQueryString,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Ricerca farmacia") },
                        leadingIcon = {
                            Image(
                                imageVector = Icons.Rounded.Search,
                                contentDescription = "ricerca farmacia"
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            capitalization = KeyboardCapitalization.Sentences,
                            imeAction = ImeAction.Done,
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                (activity
                                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                                    .hideSoftInputFromWindow(
                                        activity.window.decorView.windowToken,
                                        0
                                    )
                            }
                        ),
                        trailingIcon = {
                            if (uistate.value.isQueryPresent()) {
                                Image(
                                    imageVector = Icons.Rounded.Clear,
                                    contentDescription = "Cancella testo",
                                    modifier = Modifier.clickable {
                                        viewModel.setQueryString("")
                                    }
                                )
                            }
                        }

                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    LazyColumn(
                        state = lazyListState,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            count = pharmacies.size,
                            key = { pos -> pharmacies[pos].id }) { position ->
                            PharmaCell(
                                activity = activity,
                                item = pharmacies[position]
                            ) {
//                                val json = Gson().toJson(it)
                                PharmaDetailViewModel.pharmacy = it
                                try {
                                    navController.navigate(Constants.Companion.ROUTES_PHARMA_LIST.pharmaDetail.route)
//                                    navController.navigate(
//                                        "%s/%s".format(
//                                            Constants.Companion.ROUTES_PHARMA_LIST.pharmaDetail.name,
//                                            json
//                                        )
//                                    )
                                } catch (ex: Exception) {
                                    ex.printStackTrace()
                                    Toast.makeText(
                                        activity,
                                        "Impossibile visualizzare i dati della farmacia in questo momento",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                }

                1 -> {
                    viewModel.getServices()
                    LazyColumn(
                        state = lazyListState,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            count = services.size,
                            key = { pos -> services[pos] }) { position ->
                            ServiceCell(activity = activity, item = services[position])
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, group = "cells")
@Composable
fun PCellPreview() {
    OIPharmaTheme {
        PharmaCell(activity = MainActivity(), item = Pharmacy.fooInstance()) {

        } // , LatLng(43.10, 10.11))
    }
}

@Preview(showBackground = true, group = "cells")
@Composable
fun ServicesCellPreview() {
    OIPharmaTheme {
        ServiceCell(activity = MainActivity(), item = "Servizio di prova")
    }
}

@Composable
fun ServiceCell(activity: MainActivity, item: String) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = item,
                    fontSize = TextUnit(20f, TextUnitType.Sp),
                    fontWeight = FontWeight.Bold,
                )
            },
            modifier = Modifier.wrapContentSize()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PharmaCell(activity: MainActivity, item: Pharmacy, itemListener: (Pharmacy) -> Unit) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        onClick = {
            itemListener.invoke(item)
        }
    ) {
        ListItem(
            overlineContent = {
                Text(
                    text = item.address, fontSize = TextUnit(16f, TextUnitType.Sp),
                    color = Color.Gray
                )
            },
            headlineContent = {
                Text(
                    text = item.name,
                    fontSize = TextUnit(18f, TextUnitType.Sp),
                    fontWeight = FontWeight.Bold
                )
            },
            supportingContent = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Image(
                        painterResource(id = R.drawable.baseline_local_pharmacy_24),
                        contentDescription = "Farmacia locale"
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    val status = item.isNowOpen()
                    if (status.first) {
                        Text(
                            text = "Aperto", color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(text = "fino alle ${status.second}", color = Color.Black)
                    } else
                        Text(
                            text = "Chiuso", color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold
                        )
                    Spacer(modifier = Modifier.weight(1f))
                    item.distance?.run {
                        item.distanceFormatted?.run {
                            Text(
                                text = "a $this",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }

//                        val meters = item.distance
//
//                        meters?.run {
//                            val dist: String = when (meters) {
//                                in 0f..999f -> {
//                                    "%d m".format(meters.toInt())
//                                }
//
//                                else -> {
//                                    "%s Km".format(nf.format(meters.div(1000f)))
//                                }
//                            }
//                            Text(
//                                text = "a $dist",
//                                fontWeight = FontWeight.Bold,
//                                color = Color.Black
//                            )
//                        }
                    }
//                        ?: run {
//                        Text(
//                            text = "Distanza non calcolabile",
//                            fontWeight = FontWeight.Light,
//                            fontSize = TextUnit(13f, TextUnitType.Sp)
//                        )
//                    }

                }
            })
    }
}
