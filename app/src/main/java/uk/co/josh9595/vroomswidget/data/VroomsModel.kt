package uk.co.josh9595.vroomswidget.data

import com.google.gson.annotations.SerializedName
import uk.co.josh9595.vroomswidget.R

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

enum class Tracks(val nameImage: Int, val trackImage: Int) {
    BAHRAIN(R.drawable.bahrain, R.drawable.bahrain_track),
    SAUDI_ARABIA(R.drawable.saudi_arabia, R.drawable.saudi_arabia_track),
    AUSTRALIAN(R.drawable.australian, R.drawable.australian_track),
    AZERBAIJAN(R.drawable.azerbaijan, R.drawable.azerbaijan_track),
    MIAMI(R.drawable.miami, R.drawable.miami_track),
    EMILIA_ROMAGNA(R.drawable.emilia_romagna, R.drawable.emilia_romagna_track),
    MONACO(R.drawable.monaco, R.drawable.monaco_track),
    SPANISH(R.drawable.spanish, R.drawable.spanish_track),
    CANADIAN(R.drawable.canadian, R.drawable.canadian_track),
    AUSTRIAN(R.drawable.austrian, R.drawable.austrian_track),
    BRITISH(R.drawable.british, R.drawable.british_track),
    HUNGARIAN(R.drawable.hungarian, R.drawable.hungarian_track),
    BELGIAN(R.drawable.belgian, R.drawable.belgian_track),
    DUTCH(R.drawable.dutch, R.drawable.dutch_track),
    ITALIAN(R.drawable.italian, R.drawable.italian_track),
    SINGAPORE(R.drawable.singapore, R.drawable.singapore_track),
    JAPANESE(R.drawable.japanese, R.drawable.japanese_track),
    QATAR(R.drawable.qatar, R.drawable.qatar_track),
    UNITED_STATES(R.drawable.united_states, R.drawable.united_states_track),
    MEXICAN(R.drawable.mexican, R.drawable.mexican_track),
    BRAZILIAN(R.drawable.brazilian, R.drawable.brazilian_track),
    LAS_VEGAS(R.drawable.las_vegas, R.drawable.las_vegas_track),
    ABU_DHABI(R.drawable.abu_dhabi, R.drawable.abu_dhabi_track),
}