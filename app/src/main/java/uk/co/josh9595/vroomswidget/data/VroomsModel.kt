package uk.co.josh9595.vroomswidget.data

import com.google.gson.annotations.SerializedName
import uk.co.josh9595.vroomswidget.R
import uk.co.josh9595.vroomswidget.widget.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

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
    AUSTRALIAN(R.drawable.australia, R.drawable.australian_track),
    AZERBAIJAN(R.drawable.azerbaijan, R.drawable.azerbaijan_track),
    MIAMI(R.drawable.miami, R.drawable.miami_track),
    EMILIA_ROMAGNA(R.drawable.emilia_romagna, R.drawable.emilia_romagna_track),
    MONACO(R.drawable.monaco, R.drawable.monaco_track),
    SPANISH(R.drawable.spain, R.drawable.spanish_track),
    CANADIAN(R.drawable.canada, R.drawable.canadian_track),
    AUSTRIAN(R.drawable.austria, R.drawable.austrian_track),
    BRITISH(R.drawable.great_britain, R.drawable.british_track),
    HUNGARIAN(R.drawable.hungary, R.drawable.hungarian_track),
    BELGIAN(R.drawable.belgium, R.drawable.belgian_track),
    DUTCH(R.drawable.netherlands, R.drawable.dutch_track),
    ITALIAN(R.drawable.italy, R.drawable.italian_track),
    SINGAPORE(R.drawable.singapore, R.drawable.singapore_track),
    JAPANESE(R.drawable.japan, R.drawable.japanese_track),
    QATAR(R.drawable.qatar, R.drawable.qatar_track),
    UNITED_STATES(R.drawable.united_states, R.drawable.united_states_track),
    MEXICAN(R.drawable.mexico, R.drawable.mexican_track),
    BRAZILIAN(R.drawable.brazil, R.drawable.brazilian_track),
    LAS_VEGAS(R.drawable.las_vegas, R.drawable.las_vegas_track),
    ABU_DHABI(R.drawable.abu_dhabi, R.drawable.abu_dhabi_track),
}

fun raceToVroomsInfo(race: Races): VroomsInfo.Available {
    return VroomsInfo.Available(
        round = race.round,
        dateValue = buildDate(race.sessions.fp1, race.sessions.gp),
        name = race.name,
        nameImage = getTrack(race.slug).nameImage,
        trackImage = getTrack(race.slug).trackImage,
        sessions = buildSessions(race.sessions)
    )
}

fun buildDate(firstSession: String, lastSession: String): String {
    val firstSessionDate = LocalDateTime.parse(firstSession.dropLast(1))
    val lastSessionDate = LocalDateTime.parse(lastSession.dropLast(1))

    return if (firstSessionDate.month != lastSessionDate.month) {
        "${firstSessionDate.month.name.take(3).lowercase().replaceFirstChar(Char::uppercaseChar)} ${addDigit(firstSessionDate.dayOfMonth)} - ${lastSessionDate.month.name.take(3).lowercase().replaceFirstChar(Char::uppercaseChar)} ${addDigit(lastSessionDate.dayOfMonth)}"
    } else {
        "${firstSessionDate.month.name.take(3).lowercase().replaceFirstChar(Char::uppercaseChar)} ${addDigit(firstSessionDate.dayOfMonth)} - ${addDigit(lastSessionDate.dayOfMonth)}"
    }
}

fun buildSessions(sessions: Sessions): List<SessionDate> {
    val sessionsList = mutableListOf<SessionDate>()

    sessions.sprint?.let {

        sessionsList.add(
            SessionDate(
                "friday",
                R.drawable.friday,
                Session(
                    "free practice 1",
                    R.drawable.fp1,
                    "${getHour(sessions.fp1)}:${getMinute(sessions.fp1)}",
                    "${getHourPlusOne(sessions.fp1)}:${getMinute(sessions.fp1)}",
                    hasSessionPassed(sessions.fp1)
                ),
                Session(
                    "qualifying",
                    R.drawable.q,
                    "${getHour(sessions.qualifying)}:${getMinute(sessions.qualifying)}",
                    "${getHourPlusOne(sessions.qualifying)}:${getMinute(sessions.qualifying)}",
                    hasSessionPassed(sessions.qualifying)
                ),
                hasDayPassed(sessions.fp1)
            )
        )

        sessionsList.add(
            SessionDate(
                "saturday",
                R.drawable.saturday,
                Session(
                    "free practice 2",
                    R.drawable.fp2,
                    "${getHour(sessions.fp2)}:${getMinute(sessions.fp2)}",
                    "${getHourPlusOne(sessions.fp2)}:${getMinute(sessions.fp2)}",
                    hasSessionPassed(sessions.fp2)
                ),
                Session(
                    "sprint",
                    R.drawable.s,
                    "${getHour(it)}:${getMinute(it)}",
                    "${getHourPlusOne(it)}:${getMinute(it)}",
                    hasSessionPassed(it)
                ),
                hasDayPassed(sessions.fp2)
            )
        )

    } ?: run {

        sessionsList.add(
            SessionDate(
                "friday",
                R.drawable.friday,
                Session(
                    "free practice 1",
                    R.drawable.fp1,
                    "${getHour(sessions.fp1)}:${getMinute(sessions.fp1)}",
                    "${getHourPlusOne(sessions.fp1)}:${getMinute(sessions.fp1)}",
                    hasSessionPassed(sessions.fp1)
                ),
                Session(
                    "free practice 2",
                    R.drawable.fp2,
                    "${getHour(sessions.fp2)}:${getMinute(sessions.fp2)}",
                    "${getHourPlusOne(sessions.fp2)}:${getMinute(sessions.fp2)}",
                    hasSessionPassed(sessions.fp2)
                ),
                hasDayPassed(sessions.fp1)
            )
        )

        sessionsList.add(
            SessionDate(
                "saturday",
                R.drawable.saturday,
                Session(
                    "free practice 3",
                    R.drawable.fp3,
                    "${getHour(sessions.fp3)}:${getMinute(sessions.fp3)}",
                    "${getHourPlusOne(sessions.fp3)}:${getMinute(sessions.fp3)}",
                    hasSessionPassed(sessions.fp3)
                ),
                Session(
                    "qualifying",
                    R.drawable.q,
                    "${getHour(sessions.qualifying)}:${getMinute(sessions.qualifying)}",
                    "${getHourPlusOne(sessions.qualifying)}:${getMinute(sessions.qualifying)}",
                    hasSessionPassed(sessions.qualifying)
                ),
                hasDayPassed(sessions.qualifying)
            )
        )
    }

    sessionsList.add(
        SessionDate(
            "sunday",
            R.drawable.sunday,
            Session(
                "race",
                R.drawable.flag,
                "${getHour(sessions.gp)}:${getMinute(sessions.gp)}",
                null,
                hasSessionPassed(sessions.gp, true)
            )
        )
    )

    return sessionsList
}

fun getHour(time: String?): String = addDigit(ZonedDateTime
    .parse(time, DateTimeFormatter.ISO_DATE_TIME)
    .withZoneSameInstant(ZoneId.systemDefault())
    .toLocalDateTime()
    .hour)

fun getHourPlusOne(time: String?): String = addDigit(ZonedDateTime
    .parse(time, DateTimeFormatter.ISO_DATE_TIME)
    .withZoneSameInstant(ZoneId.systemDefault())
    .toLocalDateTime()
    .plusHours(1)
    .hour)

fun getMinute(time: String?): String = addDigit(ZonedDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME).minute)

fun addDigit(minutes: Int): String {
    return if (minutes.length() == 1) {
        "0${minutes}"
    } else {
        minutes.toString()
    }
}

fun Int.length() = when(this) {
    0 -> 1
    else -> kotlin.math.log10(kotlin.math.abs(toDouble())).toInt() + 1
}

fun getTrack(slug: String): Tracks {
    return when (slug) {
        "bahrain-grand-prix" -> Tracks.BAHRAIN
        "saudi-arabia-grand-prix" -> Tracks.SAUDI_ARABIA
        "australian-grand-prix" -> Tracks.AUSTRALIAN
        "azerbaijan-grand-prix" -> Tracks.AZERBAIJAN
        "miami-grand-prix" -> Tracks.MIAMI
        "emilia-romagna-grand-prix" -> Tracks.EMILIA_ROMAGNA
        "monaco-grand-prix" -> Tracks.MONACO
        "spanish-grand-prix" -> Tracks.SPANISH
        "canadian-grand-prix" -> Tracks.CANADIAN
        "austrian-grand-prix" -> Tracks.AUSTRIAN
        "british-grand-prix" -> Tracks.BRITISH
        "hungarian-grand-prix" -> Tracks.HUNGARIAN
        "belgian-grand-prix" -> Tracks.BELGIAN
        "dutch-grand-prix" -> Tracks.DUTCH
        "italian-grand-prix" -> Tracks.ITALIAN
        "singapore-grand-prix" -> Tracks.SINGAPORE
        "japanese-grand-prix" -> Tracks.JAPANESE
        "qatar-grand-prix" -> Tracks.QATAR
        "us-grand-prix" -> Tracks.UNITED_STATES
        "mexican-grand-prix" -> Tracks.MEXICAN
        "brazilian-grand-prix" -> Tracks.BRAZILIAN
        "las-vegas-grand-prix" -> Tracks.LAS_VEGAS
        "abu-dhabi-grand-prix" -> Tracks.ABU_DHABI
        else -> Tracks.BAHRAIN
    }
}

fun hasSessionPassed(sessionStartTime: String?, isGrandPrix: Boolean = false): Boolean {
    val endDateTime = ZonedDateTime
        .parse(sessionStartTime, DateTimeFormatter.ISO_DATE_TIME)
        .withZoneSameInstant(ZoneId.systemDefault())
        .toLocalDateTime().plusHours(if (isGrandPrix) 2 else 1)
    val current = LocalDateTime.now()

    return current.isAfter(endDateTime)
}

fun hasDayPassed(day: String): Boolean {
    val dayLocalDate = LocalDate.parse(day.split("T")[0])
    val current = LocalDate.now()

    return current.isAfter(dayLocalDate)
}