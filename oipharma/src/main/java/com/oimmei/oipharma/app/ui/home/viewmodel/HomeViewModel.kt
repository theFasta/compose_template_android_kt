package com.oimmei.oipharma.app.ui.home.viewmodel

import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.oimmei.oipharma.OIApplication
import com.oimmei.oipharma.app.comms.model.Pharmacy
import com.oimmei.oipharma.app.ui.home.MainActivity
import com.oimmei.oipharma.app.utils.LocationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader


/**
 * Created by Andrea Fastame
 * @email andrea@oimmei.com
 * @since 29/09/2023 - 13:17
 * Copyright Oimmei Digital Consulting Srl 2015-2023 - www.oimmei.com
 */

class HomeViewModel(val aValue: Boolean) : ViewModel() {
    class HomeViewModelFactory(private val aValue: Boolean) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            HomeViewModel(aValue) as T
    }

    private var _currentLocation = MutableStateFlow<LatLng?>(null)
    val currentLocation = _currentLocation.asStateFlow()
    fun setCurrentLocation(newLoc: LatLng?) {
        _currentLocation.value = newLoc
    }

    var pharmacies = mutableStateListOf<Pharmacy>()
    var services = mutableStateListOf<String>()
    var phoneyServices = listOf<String>(
        "Servizi di Farmacia",
        "Servizi di Consulenza",
        "Noleggio Prodotti",
        "Vendita Prodotti"
    )

    fun getPharmacies() {
        val type = object : TypeToken<List<Pharmacy>>() {}.type
        val iStream: InputStream =
            OIApplication.context.resources.openRawResource(com.oimmei.oipharma.app.R.raw.farmacie_livorno)
        val reader = BufferedReader(InputStreamReader(iStream))
        val json = reader.readText()
        var list: MutableList<Pharmacy> = Gson().fromJson(json, type)

        currentLocation.value?.run {
            list.forEach {
                it.getDistanceInMetersFromCurrentLocation(this@run)
            }

        }
        val nullDistance =
            list.filter { it.latitude == null || it.longitude == null || it.latitude == 0.0 || it.longitude == 0.0 }
        val withDistance = list.filter { it.latitude > 0f && it.longitude > 0f }
        list.clear()
        list.addAll(withDistance.sortedBy { it.distance })
        list.addAll(nullDistance)
//        list = list.sortedBy { it.distance }.toMutableList()
        pharmacies.apply {
            clear()
            this.addAll(list)
        }
    }

    fun getServices() {
        services.apply {
            clear()
            addAll(phoneyServices)
        }
    }

    fun getUserLocation(activity: MainActivity) {
        LocationHelper.getUserLocation(activity) { location, provider, err ->
            err?.run {
                Toast.makeText(activity, err.toString(), Toast.LENGTH_LONG).show()
            } ?: run {
                location?.run {
                    setCurrentLocation(LatLng(location.latitude, location.longitude))
                    getPharmacies()
                } ?: {
                    setCurrentLocation(null)
                }
            }

        }
    }
}