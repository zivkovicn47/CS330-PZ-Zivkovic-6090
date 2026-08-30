package com.zivkovic.project250.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

import com.google.firebase.database.PropertyName
import com.google.firebase.database.Exclude

import kotlinx.parcelize.RawValue

@Parcelize
data class CarModel(
    @get:Exclude
    var id: String = "",
    val userId: String = "",
    val categoryId: @RawValue Any? = null,
    val title: String = "",
    var price: @RawValue Any? = null,
    val description: String = "",
    val picUrl: String = "",
    val productionYear: @RawValue Any? = null,
    val mileage: @RawValue Any? = null,
    val fuelType: String = "",
    val transmission: String = "",
    val engineVolume: @RawValue Any? = null,
    val enginePower: @RawValue Any? = null,
    val highestSpeed: @RawValue Any? = null,
    val seats: @RawValue Any? = null,
    // Opciono kontakt polje koje uvodi web strana (ugovor o sinhronizaciji, tacka 6).
    val phone: String? = null
) : Parcelable {
    @get:Exclude
    val priceInt: Int
        get() = when (val p = price) {
            is Number -> p.toInt()
            is String -> p.toIntOrNull() ?: 0
            else -> 0
        }

    @get:Exclude
    val productionYearInt: Int
        get() = when (val p = productionYear) {
            is Number -> p.toInt()
            is String -> p.toIntOrNull() ?: 0
            else -> 0
        }

    @get:Exclude
    val mileageInt: Int
        get() = when (val p = mileage) {
            is Number -> p.toInt()
            is String -> p.toIntOrNull() ?: 0
            else -> 0
        }

    @get:Exclude
    val engineVolumeInt: Int
        get() = when (val p = engineVolume) {
            is Number -> p.toInt()
            is String -> p.toIntOrNull() ?: 0
            else -> 0
        }

    @get:Exclude
    val enginePowerInt: Int
        get() = when (val p = enginePower) {
            is Number -> p.toInt()
            is String -> p.toIntOrNull() ?: 0
            else -> 0
        }

    @get:Exclude
    val highestSpeedInt: Int
        get() = when (val p = highestSpeed) {
            is Number -> p.toInt()
            is String -> p.toIntOrNull() ?: 0
            else -> 0
        }

    @get:Exclude
    val seatsInt: Int
        get() = when (val p = seats) {
            is Number -> p.toInt()
            is String -> p.toIntOrNull() ?: 0
            else -> 0
        }
    
    @get:Exclude
    val categoryIdInt: Int
        get() = when (val p = categoryId) {
            is Number -> p.toInt()
            is String -> p.toIntOrNull() ?: 0
            else -> 0
        }
}