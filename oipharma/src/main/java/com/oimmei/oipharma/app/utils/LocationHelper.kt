package com.oimmei.oipharma.app.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Criteria
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.SphericalUtil
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.sqrt

/**
 * @author Andrea Fastame <a.fastame@gmail.com>
 * @since 11 Apr 2018
 * @lastupdate Jul 13 2023
 * Copyright Oimmei Srls 2015-2022 - www.oimmei.com
 * Import - gradle : implementation "com.google.android.gms:play-services-location:19.0.1"
 */
class LocationHelper {
    companion object {

        val TAG: String = LocationHelper::class.java.simpleName
        val criteria: Criteria by lazy {
            Criteria().apply {
                isSpeedRequired = false
                isCostAllowed = false
                isBearingRequired = false
                isAltitudeRequired = false
                accuracy = Criteria.ACCURACY_COARSE
            }
        }

        @SuppressLint("MissingPermission")
        fun getUserLocation(
            activity: ComponentActivity, callback: (Location?, String?, Exception?) -> Unit
        ) {
            try {
                //                if (activity.checkPermissions()) {
                val lm: LocationManager =
                    activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager

                var theLocation: Location? = null

                getFusedLocation(activity) { loc, err ->
                    err?.let {
                        callback.invoke(null, null, err)
                        it
                    } ?: run {
                        loc?.run {
                            callback.invoke(loc, "fused", null)
                        } ?: run {
                            lm.allProviders.forEach all@{ aProvider: String ->
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                    lm.getCurrentLocation(
                                        aProvider, null, ContextCompat.getMainExecutor(activity)
                                    ) {
                                        if (theLocation == null) {
                                            /**
                                             * Se theLocation == null qualche provider ha gia' ritornato
                                             * un risultato valido, quindi non ritorno il risultato attuale
                                             */
                                            /**
                                             * Se theLocation == null qualche provider ha gia' ritornato
                                             * un risultato valido, quindi non ritorno il risultato attuale
                                             */
                                            /**
                                             * Se theLocation == null qualche provider ha gia' ritornato
                                             * un risultato valido, quindi non ritorno il risultato attuale
                                             */
                                            /**
                                             * Se theLocation == null qualche provider ha gia' ritornato
                                             * un risultato valido, quindi non ritorno il risultato attuale
                                             */
                                            it.run {
                                                theLocation = it
                                                callback.invoke(it, aProvider, null)
                                            }
                                        }
                                    }
                                } else {

                                    if (aProvider !in arrayOf("passive", "gps")) {
                                        /**
                                         * Il provider "passive" || "gps" non puo' essere utilizzato in quanto ha bisogno
                                         * del permesso ACCESS_FINE_LOCATION, che l'app non ha
                                         */
                                        /**
                                         * Il provider "passive" || "gps" non puo' essere utilizzato in quanto ha bisogno
                                         * del permesso ACCESS_FINE_LOCATION, che l'app non ha
                                         */
                                        /**
                                         * Il provider "passive" || "gps" non puo' essere utilizzato in quanto ha bisogno
                                         * del permesso ACCESS_FINE_LOCATION, che l'app non ha
                                         */
                                        /**
                                         * Il provider "passive" || "gps" non puo' essere utilizzato in quanto ha bisogno
                                         * del permesso ACCESS_FINE_LOCATION, che l'app non ha
                                         */
                                        lm.requestSingleUpdate(
                                            aProvider, {
                                                if (theLocation == null) {
                                                    /**
                                                     * Se theLocation == null qualche provider ha gia' ritornato
                                                     * un risultato valido, quindi non ritorno il risultato attuale
                                                     */
                                                    /**
                                                     * Se theLocation == null qualche provider ha gia' ritornato
                                                     * un risultato valido, quindi non ritorno il risultato attuale
                                                     */
                                                    /**
                                                     * Se theLocation == null qualche provider ha gia' ritornato
                                                     * un risultato valido, quindi non ritorno il risultato attuale
                                                     */
                                                    /**
                                                     * Se theLocation == null qualche provider ha gia' ritornato
                                                     * un risultato valido, quindi non ritorno il risultato attuale
                                                     */
                                                    it.run {
                                                        theLocation = this
                                                        callback.invoke(it, aProvider, null)
                                                    }
                                                }
                                            }, Looper.getMainLooper()
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                callback.invoke(null, null, e)
            }
        }

        val executor: ExecutorService = Executors.newSingleThreadExecutor()

        @SuppressLint("MissingPermission")
        fun getCurrentLocation(
            activity: ComponentActivity, callback: (Location?, String?, Exception?) -> Unit
        ) {
            val lm: LocationManager =
                activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val request = android.location.LocationRequest.Builder(1000)
                    .setMaxUpdates(1)
                    .setDurationMillis(5000)
                    .setQuality(android.location.LocationRequest.QUALITY_HIGH_ACCURACY).build()

                val allEnabledProviders = lm.getProviders(true)
                val provider =
                    allEnabledProviders.firstOrNull { it.equals(LocationManager.GPS_PROVIDER) }
                        ?: allEnabledProviders.firstOrNull { it.equals(LocationManager.FUSED_PROVIDER) }
                        ?: allEnabledProviders.firstOrNull { it.equals(LocationManager.NETWORK_PROVIDER) }

                provider?.run {
                    lm.getCurrentLocation(
                        provider,
                        request,
                        null,
                        { runnable: Runnable ->
                            Log.d(TAG, "here")
                        },
                        { location: Location ->
                            callback.invoke(location, provider, null)
                        }
                    )
                }


//                lm.getBestProvider(criteria, true)?.let { provider ->
//                    lm.getCurrentLocation(provider, request, null, { runnable ->
//                        Log.d(TAG, "here")
//                    }, { location ->
//                        callback.invoke(location, null)
//                    })
//                }
            } else {
                lm.getBestProvider(criteria, true)?.let { provider ->
                    LocationManagerCompat.getCurrentLocation(
                        lm,
                        provider,
                        null,
                        { runnable: Runnable ->
                        },
                        { location ->
                            callback.invoke(location, provider, null)
                        })
                }

            }

//                val loc = lm.getCurrentLocation(provider,
//                    LocationRequest.
//                    null,
//                    Executor {  },
//
//                can)
        }

        @SuppressLint("MissingPermission")
        private fun getFusedLocation(
            activity: ComponentActivity, callback: (Location?, Exception?) -> Unit
        ) {
            val flm = LocationServices.getFusedLocationProviderClient(activity)
            flm.lastLocation.addOnCompleteListener {
                when {
                    it.isSuccessful -> callback.invoke(it.result, null)
                    else -> callback.invoke(
                        null, Exception("Posizione non disponibile")
                    )
                }
            }
        }

        fun toLatLngBoundsWithRadius(
            location: Location,
            radiusInMeters: Double
        ): CoordinatesContainer {
            return toLatLngBoundsWithRadius(
                LatLng(location.latitude, location.longitude),
                radiusInMeters
            )
        }

        enum class DIRECTIONS_DEGREES(val degrees: Int) {
            NORTH(0),
            NORTH_EAST(45),
            EAST(90),
            SOUTH_EAST(135),
            SOUTH(180),
            SOUTH_WEST(225),
            WEST(270),
            NORTH_WEST(315)
        }

        data class CoordinatesContainer(val northWest: LatLng, val southEast: LatLng)

        fun toLatLngBoundsWithRadius(
            center: LatLng,
            radiusInMeters: Double
        ): CoordinatesContainer {
            val distanceFromCenterToCorner: Double = radiusInMeters * sqrt(2.0);

            /**
             * I numeri sotto rappresentano i gradi. Quindi:
             * N = 0
             * NE = 45
             * E = 90
             * SE = 135
             * S = 180
             * SW = 225
             * W = 270
             * NW = 315
             */

            val corner1 = 45.0
            val corner2 = 225.0

            val northEast: LatLng =
                SphericalUtil.computeOffset(
                    center,
                    distanceFromCenterToCorner,
                    corner1
                )
            val southWest: LatLng =
                SphericalUtil.computeOffset(
                    center,
                    distanceFromCenterToCorner,
                    corner2
                )
            val bounds = LatLngBounds(southWest, northEast)

            val northWest = LatLng(bounds.southwest.latitude, bounds.northeast.longitude)
            val southEast = LatLng(bounds.northeast.latitude, bounds.southwest.longitude)
            return CoordinatesContainer(
                northWest = northWest, southEast = southEast
            )
        }

//        fun hasPermissions(context: Context): Boolean {
//            return PermissionChecker.
//        }

        fun geoCode(context: Context, location: Location, function: (String?) -> Unit) {
            if (Geocoder.isPresent()) {
                Geocoder(context).run {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        this.getFromLocation(location.latitude, location.longitude, 1) {
                            function.invoke(it[0]?.getAddressLine(0))
                        }
                    } else {
                        val results: MutableList<Address>? =
                            this.getFromLocation(location.latitude, location.longitude, 1)
                        function.invoke(results?.get(0)?.getAddressLine(0))
                    }
                }
            }
        }

        fun getGeocodedAddress(location: Location, context: Context, callback: (Address?) -> Unit) {
            if (Geocoder.isPresent()) {
                val geocoder = Geocoder(context, Locale.getDefault())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    geocoder.getFromLocation(
                        location.latitude,
                        location.longitude,
                        1
                    ) {
                        it.firstOrNull()?.run {
                            callback.invoke(this)
                        }
                    }
                } else {
                    geocoder.getFromLocation(
                        location.latitude,
                        location.longitude,
                        1
                    )?.run list@{
                        this.firstOrNull()?.run {
                            callback.invoke(this)
                        }
                    }
                }
            } else callback.invoke(null)
        }
    }
}

