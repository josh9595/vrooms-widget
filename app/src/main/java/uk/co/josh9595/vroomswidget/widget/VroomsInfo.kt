package uk.co.josh9595.vroomswidget.widget

import kotlinx.serialization.Serializable
import uk.co.josh9595.vroomswidget.R

@Serializable
sealed interface VroomsInfo {

    @Serializable
    object Loading: VroomsInfo

    @Serializable
    data class Available(
        val round: Int,
        val dateValue: String,
        val name: String,
        val nameImage: Int,
        val trackImage: Int,
        val sessions: List<SessionDate>
    ): VroomsInfo

    @Serializable
    data class Unavailable(
        val message: String
    ): VroomsInfo
}

@Serializable
data class SessionDate(
    val date: String,
    val dayImage: Int,
    val sessionOne: Session,
    val sessionTwo: Session? = null
)

@Serializable
data class Session(
    val name: String,
    val nameImage: Int,
    val time: String,
    val endTime: String?
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
