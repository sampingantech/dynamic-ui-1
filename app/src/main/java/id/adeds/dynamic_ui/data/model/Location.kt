package id.adeds.dynamic_ui.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Location(
    var addressName: String,
    var lat: Double,
    var long: Double,
    var key: String = ""
) : Parcelable {
    companion object {
        const val LOCATION_DATA = "locationData"
        const val GET_LOCATION = 1
    }
}