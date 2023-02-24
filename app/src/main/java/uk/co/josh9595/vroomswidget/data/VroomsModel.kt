package uk.co.josh9595.vroomswidget.data

import com.google.gson.annotations.SerializedName

data class VroomsCalendar(
    @SerializedName("races") var races : List<Races>
)

data class Races (
    @SerializedName("name") var name: String,
    @SerializedName("location") var location: String,
    @SerializedName("latitude") var latitude: Double,
    @SerializedName("longitude") var longitude: Double,
    @SerializedName("round") var round: Int,
    @SerializedName("slug") var slug: String,
    @SerializedName("localeKey") var localeKey: String,
    @SerializedName("sessions") var sessions: Sessions

)

data class Sessions (
    @SerializedName("fp1") var fp1: String,
    @SerializedName("fp2") var fp2: String,
    @SerializedName("fp3") var fp3: String?,
    @SerializedName("qualifying") var qualifying: String,
    @SerializedName("sprint") var sprint: String?,
    @SerializedName("gp") var gp: String

)