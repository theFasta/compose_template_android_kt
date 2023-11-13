package com.oimmei.oipharma.app.ui.home

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.oimmei.oipharma.OIApplication
import com.oimmei.oipharma.app.R
import com.oimmei.oipharma.app.comms.model.Pharmacy
import com.oimmei.oipharma.app.ui.home.viewmodel.PharmaDetailViewModel
import com.oimmei.oipharma.app.ui.theme.OIPharmaTheme
import java.util.Calendar


/**
 * Created by Andrea Fastame
 * @email andrea@oimmei.com
 * @since 10/11/2023 - 10:21
 * Copyright Oimmei Digital Consulting Srl 2015-2023 - www.oimmei.com
 */

@Preview(showBackground = true, group = "screens")
@Composable
fun PDScreenPreview() {
    OIPharmaTheme {
        PharmaDetailViewModel.pharmacy = Pharmacy.fooInstance()
        PharmaDetailScreen(
            activity = MainActivity(),
            navController = rememberNavController()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun PharmaDetailScreen(
    activity: ComponentActivity, navController: NavController
) {
    val (canPop, setCanPop) = remember { mutableStateOf(false) }
    navController.addOnDestinationChangedListener { controller, destination, arguments ->
        setCanPop(controller.previousBackStackEntry != null)
    }

    val viewModel = PharmaDetailViewModel
    assert(PharmaDetailViewModel.pharmacy != null)

    val permissionState = rememberPermissionState(permission = Manifest.permission.CALL_PHONE)

    OIPharmaTheme {
        Scaffold(topBar = {
            TopAppBar(title = {
//                    Row {
//                        Icon(
//                            painter = painterResource(id = R.mipmap.big_pill),
//                            contentDescription = "Pillola",
//                            Modifier.size(32.dp)
//                        )
                Text(text = "Dettaglio farmacia", color = Color.White)
//                    }
            }, navigationIcon = {
                if (canPop) IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        contentDescription = stringResource(id = R.string.indietro),
                        imageVector = Icons.Rounded.ArrowBack,
                        tint = Color.White
                    )
                }
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            )
            )
        }) {
            val scrollstate = rememberScrollState()
            Column(
                modifier = Modifier
                    .padding(it)
                    .padding(all = 16.dp)
                    .verticalScroll(state = scrollstate, enabled = true)
            ) {
                Text(
                    text = viewModel.pharmacy!!.name,
                    fontSize = TextUnit(28f, TextUnitType.Sp),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
                viewModel.pharmacy?.address?.run {
                    Text(
                        text = this,
                        fontSize = TextUnit(20f, TextUnitType.Sp)
                    )
                }
                viewModel.pharmacy?.distanceFormatted?.run {
                    Spacer(modifier = Modifier.size(16.dp))
                    Text(
                        text = "a $this da me",
                        fontSize = TextUnit(18f, TextUnitType.Sp),
                        color = MaterialTheme.colorScheme.outline
                    )
                    val status = viewModel.pharmacy!!.isNowOpen()
                    if (status.first) {
                        Spacer(modifier = Modifier.size(16.dp))
                        Row(verticalAlignment = Alignment.Bottom) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_local_pharmacy_24),
                                contentDescription = "Immagine farmacia",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            androidx.compose.material3.Text(
                                text = "Aperto",
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                fontSize = TextUnit(18f, TextUnitType.Sp)
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            androidx.compose.material3.Text(
                                text = "fino alle ${status.second}", color = Color.Black
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.size(16.dp))
                        Row(verticalAlignment = Alignment.Bottom) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_local_pharmacy_24),
                                contentDescription = "Immagine farmacia",
                                tint = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            androidx.compose.material3.Text(
                                text = "Chiuso",
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.size(16.dp))
                viewModel.pharmacy?.phoneNumber?.run {
                    Button(
                        onClick = {
                            if (permissionState.status.isGranted) activity.startActivity(
                                Intent(Intent.ACTION_CALL).setData(
                                    Uri.parse(
                                        "tel:${
                                            viewModel.pharmacy!!.phoneNumber.trim().replace(" ", "")
                                        }"
                                    )
                                )
                            )
                            else permissionState.launchPermissionRequest()
                        }, colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer
                        )
                    ) {
                        Row {
                            Icon(
                                imageVector = Icons.Rounded.Phone,
                                contentDescription = "Chiama il ${viewModel.pharmacy!!.phoneNumber}",
                                tint = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(
                                text = "Chiama ${viewModel.pharmacy!!.phoneNumber}",
                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                fontSize = TextUnit(20f, TextUnitType.Sp)
                            )
                        }
                    }
                } ?: run {
                    Text(
                        text = "La farmacia non ha fornito il numero di telefono",
                        fontSize = TextUnit(16f, TextUnitType.Sp),
                        color = MaterialTheme.colorScheme.error
                    )
                }

                /* MAP */
                val pharmaPosition = viewModel.pharmacy!!.getLocation()
                pharmaPosition?.run {
                    val cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(pharmaPosition, 16.8f)
                    }
                    Spacer(modifier = Modifier.size(16.dp))


                    Card(
                        shape = CardDefaults.outlinedShape,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                    ) {
                        GoogleMap(
                            modifier = Modifier
                                .fillMaxSize()
                                .height(380.dp),
                            cameraPositionState = cameraPositionState,
                            properties = MapProperties(
                                isMyLocationEnabled = true, isTrafficEnabled = true,
                                isBuildingEnabled = true,
                            )
                        ) {
                            //                        val bitmapDescriptor =
                            //                            BitmapDescriptorFactory.fromResource(R.drawable.baseline_pharmacy)
                            Marker(
                                state = MarkerState(position = this@run),
                                title = viewModel.pharmacy!!.name,
                                snippet = viewModel.pharmacy!!.address,
                                tag = viewModel.pharmacy,
                                //                            icon = bitmapDescriptor,
                                onClick = { marker ->
                                    val pharmacy = marker.tag as Pharmacy
                                    if (pharmacy.hasCoordinates()) {
                                        val gmmIntentUri: Uri =
                                            Uri.parse("geo:${pharmacy.latitude},${pharmacy.longitude}?z=16")
                                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                        mapIntent.setPackage("com.google.android.apps.maps").flags =
                                            Intent.FLAG_ACTIVITY_NEW_TASK
                                        startActivity(OIApplication.context, mapIntent, null)
                                    }
                                    false
                                }
                            )
                        }
                    }
                }
                if (viewModel.pharmacy!!.hasCoordinates()) {
                    Spacer(
                        modifier = Modifier
                            .size(16.dp)
                            .fillMaxWidth()
                    )
                    OutlinedButton(onClick = {
                        val gmmIntentUri: Uri =
                            Uri.parse("geo:0,0?q=${viewModel.pharmacy!!.name}@${viewModel.pharmacy!!.latitude},${viewModel.pharmacy!!.longitude}")
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        mapIntent.setPackage("com.google.android.apps.maps").flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(OIApplication.context, mapIntent, null)
                    }, modifier = Modifier.fillMaxWidth()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.ExitToApp,
                                contentDescription = "Portami li",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(text = "Portami li", fontWeight = FontWeight.Bold)

                        }
                    }
                }

                Spacer(modifier = Modifier.size(24.dp))
                Row {
                    Image(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = "check",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "Prenotazione telefonica", fontSize = TextUnit(16f, TextUnitType.Sp)
                    )
                }
                Spacer(modifier = Modifier.size(8.dp))
                Row {
                    Image(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "cross",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.error)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "Prenotazione On-Line", fontSize = TextUnit(16f, TextUnitType.Sp)
                    )
                }
                Spacer(modifier = Modifier.size(16.dp))
                Divider(modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text = "Orari di apertura dei prossimi 7 giorni",
                    fontWeight = FontWeight.Bold,
                    fontSize = TextUnit(
                        22f, TextUnitType.Sp,
                    ),
                    color = Color.Black
                )
                Spacer(modifier = Modifier.size(16.dp))

                val cal = Calendar.getInstance()

                val next7Days = viewModel.pharmacy!!.getNext7Days()
                next7Days.forEach { day ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(
                                text = "%tA %td %tB %tY".format(cal, cal, cal, cal),
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.size(8.dp))
                        Column {
                            if (day.allDay) Text("Aperta tutto il giorno")
                            else {
                                Text(
                                    "dalle ${
                                        "%s:%s".format(
                                            day.morningOpeningHour.substring(0, 2),
                                            day.morningOpeningHour.substring(2)
                                        )
                                    }" +
                                            " alle ${
                                                "%s:%s".format(
                                                    day.morningClosingHour.substring(0, 2),
                                                    day.morningClosingHour.substring(2)
                                                )
                                            }"
                                )
                                Text(
                                    "dalle ${
                                        "%s:%s".format(
                                            day.afternoonOpeningHour.substring(0, 2),
                                            day.afternoonOpeningHour.substring(2)
                                        )
                                    } alle ${
                                        "%s:%s".format(
                                            day.afternoonClosingHour.substring(0, 2),
                                            day.afternoonClosingHour.substring(2)
                                        )
                                    }"
                                )
                            }
                        }
                    }

                    cal.roll(Calendar.DAY_OF_MONTH, true)
                }

                /* Consulenze */

                Spacer(modifier = Modifier.size(16.dp))
                Divider(modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text = "Consulenze della farmacia",
                    fontWeight = FontWeight.Bold,
                    fontSize = TextUnit(
                        18f, TextUnitType.Sp,
                    ),
                    color = Color.Black
                )
                Spacer(modifier = Modifier.size(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = "check",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(text = "Consulenze dermocosmetica")
                }
                Row {
                    Image(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = "check",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(text = "Omeopatia")
                }
                Row {
                    Image(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = "check",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(text = "Fisioterapia")
                }

                Spacer(modifier = Modifier.size(16.dp))
                Divider(modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text = "Prodotti della farmacia",
                    fontWeight = FontWeight.Bold,
                    fontSize = TextUnit(
                        18f, TextUnitType.Sp,
                    ),
                    color = Color.Black
                )
                Spacer(modifier = Modifier.size(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = "check",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(text = "Per celiaci")
                }
                Row {
                    Image(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = "check",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(text = "Cosmetica")
                }
                Row {
                    Image(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = "check",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(text = "Veterinaria")
                }
                Row {
                    Image(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = "check",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(text = "Omeopatia")
                }

                Spacer(modifier = Modifier.size(16.dp))
                Divider(modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text = "Noleggi della farmacia",
                    fontWeight = FontWeight.Bold,
                    fontSize = TextUnit(
                        18f, TextUnitType.Sp,
                    ),
                    color = Color.Black
                )
                Spacer(modifier = Modifier.size(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = "check",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(text = "Nebulizzatori")
                }
                Row {
                    Image(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = "check",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(text = "Stampelle")
                }
                Row {
                    Image(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = "check",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(text = "Bilance per bambini")
                }

                Spacer(modifier = Modifier.size(16.dp))
                Divider(modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text = "Servizi della farmacia",
                    fontWeight = FontWeight.Bold,
                    fontSize = TextUnit(
                        18f, TextUnitType.Sp,
                    ),
                    color = Color.Black
                )
                Spacer(modifier = Modifier.size(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = "check",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(text = "Misurazione del peso")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = "check",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(text = "Misurazione della pressione")
                }
                Row {
                    Image(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = "check",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(text = "Covid: test antigenico")
                }
                Row {
                    Image(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = "check",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(text = "Covid: test sierologico")
                }
                Row {
                    Image(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = "check",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(text = "Consegna a casa")
                }
            }
        }
    }
}