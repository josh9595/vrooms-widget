package uk.co.josh9595.vroomswidget.widget

import androidx.compose.ui.text.toLowerCase
import uk.co.josh9595.vroomswidget.R
import uk.co.josh9595.vroomswidget.data.Sessions
import uk.co.josh9595.vroomswidget.data.VroomsCalendar
import java.time.LocalDateTime
import java.util.*

object VroomsRepo {
    suspend fun getVroomsInfo(calendar: VroomsCalendar): VroomsInfo {

        val currentDate = LocalDateTime.now()
        calendar.races.map { race ->
            val raceStartDate = LocalDateTime.parse(race.sessions.fp1.dropLast(1))
            if (raceStartDate.isAfter(currentDate)) {
                return VroomsInfo.Available(
                    round = race.round,
                    dateValue = buildDate(race.sessions.fp1, race.sessions.gp),
                    name = race.name,
                    nameImage = getTrack(race.slug).nameImage,
                    trackImage = getTrack(race.slug).trackImage,
                    sessions = buildSessions(race.sessions)
                )
            }
        }

        return VroomsInfo.Unavailable("No next race")
    }
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
                    "${LocalDateTime.parse(sessions.fp1.dropLast(1)).hour}:${addDigit(LocalDateTime.parse(sessions.fp1.dropLast(1)).minute)}",
                    "${LocalDateTime.parse(sessions.fp1.dropLast(1)).plusHours(1).hour}:${addDigit(LocalDateTime.parse(sessions.fp1.dropLast(1)).minute)}"
                ),
                Session(
                    "qualifying",
                    R.drawable.q,
                    "${LocalDateTime.parse(sessions.qualifying.dropLast(1)).hour}:${addDigit(LocalDateTime.parse(sessions.qualifying.dropLast(1)).minute)}",
                    "${LocalDateTime.parse(sessions.qualifying.dropLast(1)).plusHours(1).hour}:${addDigit(LocalDateTime.parse(sessions.qualifying.dropLast(1)).minute)}"
                )
            )
        )
        sessionsList.add(
            SessionDate(
                "saturday",
                R.drawable.saturday,
                Session(
                    "free practice 2",
                    R.drawable.fp2,
                    "${LocalDateTime.parse(sessions.fp2.dropLast(1)).hour}:${addDigit(LocalDateTime.parse(sessions.fp2.dropLast(1)).minute)}",
                    "${LocalDateTime.parse(sessions.fp2.dropLast(1)).plusHours(1).hour}:${addDigit(LocalDateTime.parse(sessions.fp2.dropLast(1)).minute)}"
                ),
                Session(
                    "sprint",
                    R.drawable.s,
                    "${LocalDateTime.parse(it.dropLast(1)).hour}:${addDigit(LocalDateTime.parse(it.dropLast(1)).minute)}",
                    "${LocalDateTime.parse(it.dropLast(1)).plusHours(1).hour}:${addDigit(LocalDateTime.parse(it.dropLast(1)).minute)}"
                )
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
                    "${LocalDateTime.parse(sessions.fp1.dropLast(1)).hour}:${addDigit(LocalDateTime.parse(sessions.fp1.dropLast(1)).minute)}",
                    "${LocalDateTime.parse(sessions.fp1.dropLast(1)).plusHours(1).hour}:${addDigit(LocalDateTime.parse(sessions.fp1.dropLast(1)).minute)}"
                ),
                Session(
                    "free practice 2",
                    R.drawable.fp2,
                    "${LocalDateTime.parse(sessions.fp2.dropLast(1)).hour}:${addDigit(LocalDateTime.parse(sessions.fp2.dropLast(1)).minute)}",
                    "${LocalDateTime.parse(sessions.fp2.dropLast(1)).plusHours(1).hour}:${addDigit(LocalDateTime.parse(sessions.fp2.dropLast(1)).minute)}"
                )
            )
        )
        sessionsList.add(
            SessionDate(
                "saturday",
                R.drawable.saturday,
                Session(
                    "free practice 3",
                    R.drawable.fp3,
                    "${LocalDateTime.parse(sessions.fp3?.dropLast(1)).hour}:${addDigit(LocalDateTime.parse(sessions.fp3?.dropLast(1)).minute)}",
                    "${LocalDateTime.parse(sessions.fp3?.dropLast(1)).plusHours(1).hour}:${addDigit(LocalDateTime.parse(sessions.fp3?.dropLast(1)).minute)}"
                ),
                Session(
                    "qualifying",
                    R.drawable.q,
                    "${LocalDateTime.parse(sessions.qualifying.dropLast(1)).hour}:${addDigit(LocalDateTime.parse(sessions.qualifying.dropLast(1)).minute)}",
                    "${LocalDateTime.parse(sessions.qualifying.dropLast(1)).plusHours(1).hour}:${addDigit(LocalDateTime.parse(sessions.qualifying.dropLast(1)).minute)}"
                )
            )
        )
    }

    sessionsList.add(SessionDate(
        "sunday",
        R.drawable.sunday,
        Session(
            "race",
            R.drawable.flag,
            "${LocalDateTime.parse(sessions.gp.dropLast(1)).hour}:${addDigit(LocalDateTime.parse(sessions.gp.dropLast(1)).minute)}",
            null
        )
    ))

    return sessionsList
}

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