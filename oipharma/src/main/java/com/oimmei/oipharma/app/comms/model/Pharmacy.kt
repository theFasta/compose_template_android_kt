package com.oimmei.oipharma.app.comms.model

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.oimmei.oipharma.app.ui.home.nf
import java.util.Calendar
import kotlin.random.Random


/**
 * Created by Andrea Fastame
 * @email andrea@oimmei.com
 * @since 09/11/2023 - 08:55
 * Copyright Oimmei Digital Consulting Srl 2015-2023 - www.oimmei.com
 */

data class OpeningDay(
    val weekday: Int,
    val morningOpeningHour: String,
    val morningClosingHour: String,
    val afternoonOpeningHour: String,
    val afternoonClosingHour: String,
    val allDay: Boolean = false
)

data class Pharmacy(
    val address: String,
    val name: String,
    val zip: Int,
    val city: String,
    val locality: String,
    val latitude: Double,
    val longitude: Double,
    var distance: Float? = null,
    val phoneNumber: String,
    val openingDays: List<OpeningDay>
) {
    val distanceFormatted: String?
        get() {
            return distance?.let { distance ->
                when (distance) {
                    in 0f..999f -> {
                        "%d m".format(distance.toInt())
                    }

                    else -> {
                        "%s Km".format(nf.format(distance.div(1000f)))
                    }
                }
            }
        }

    private var _id: Long? = null
        get
    val id: Long
        get() {
            if (_id == null)
                _id = Random.nextLong(Long.MAX_VALUE)
            return _id!!
        }
//    val uuid = UUID.randomUUID().toString()

    //    init {
//        id = Random.nextLong(Long.MAX_VALUE)
//    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Pharmacy

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    companion object {
        fun fooInstance(): Pharmacy {
            return Pharmacy(
                address = "Via dell'Oriolino",
                name = "Farmacia del Dr Ciccio Pasticcio",
                zip = 57125,
                city = "Livorno",
                locality = "LI",
                latitude = 43.21,
                longitude = 10.58,
                distance = 552f,
                phoneNumber = "0586 123 4567",
                openingDays = listOf()

            )
        }
    }

    fun isNowOpen(): Pair<Boolean, String?> {
        val today = Calendar.getInstance()
        val day: Int = today.get(Calendar.DAY_OF_WEEK)
        val hour: Int = today.get(Calendar.HOUR_OF_DAY)
        val minute: Int = today.get(Calendar.MINUTE)
        val actualHour = "%s%s".format(hour, minute).toInt()

        val openingDay = openingDays.find { it.weekday == day }
        return openingDay?.let { od ->
            when (actualHour) {
                in (od.morningOpeningHour.toInt()..od.morningClosingHour.toInt()) -> {
                    (true to od.morningClosingHour)
                }

                in (od.afternoonOpeningHour.toInt()..od.afternoonClosingHour.toInt()) -> {
                    (true to od.afternoonClosingHour)
                }

                else -> (false to null)
            }
        } ?: return (false to null)
    }

    fun getDistanceInMetersFromCurrentLocation(position: LatLng?): Float? {
        position?.run away@{
            val results = FloatArray(3)
            return when {
                this@Pharmacy.latitude != 0.0 && this@Pharmacy.longitude != 0.0 -> {
                    Location.distanceBetween(
                        this@Pharmacy.latitude,
                        this@Pharmacy.longitude,
                        position.latitude,
                        position.longitude,
                        results
                    )
                    distance = results[0]
                    distance
                }

                else -> {
                    distance = null
                    null
                }
            }
        } ?: return null

    }

    override fun toString(): String {
        return "Pharmacy(name='$name', distance=$distance)"
    }

    fun getLocation(): LatLng? {
        return latitude?.let { lat ->
            longitude?.let { lon ->
                LatLng(lat, lon)
            }
        }
    }

    fun getNext7Days(): List<OpeningDay> {
        val today = Calendar.getInstance()
        val day: Int = today.get(Calendar.DAY_OF_WEEK)
        val starter = openingDays.find { it.weekday == day }
        val starteIdx = openingDays.indexOf(starter)

        val starters = openingDays.subList(starteIdx, openingDays.lastIndex.plus(1))
//        val starters = openingDays.takeLast(openingDays.lastIndex.plus(1).minus(starteIdx))
        val enders = openingDays.subList(0, starteIdx)
        val results: MutableList<OpeningDay> =
            mutableListOf<OpeningDay>().apply { addAll(starters); addAll(enders) }
        return results
    }
}