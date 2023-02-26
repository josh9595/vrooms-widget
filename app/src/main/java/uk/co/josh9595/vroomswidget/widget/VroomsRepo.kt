package uk.co.josh9595.vroomswidget.widget

import uk.co.josh9595.vroomswidget.R
import uk.co.josh9595.vroomswidget.data.Sessions
import uk.co.josh9595.vroomswidget.data.Tracks
import uk.co.josh9595.vroomswidget.data.VroomsCalendar
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object VroomsRepo {
    suspend fun getVroomsInfo(calendar: VroomsCalendar): VroomsInfo {

        val currentDate = LocalDate.now()
        calendar.races.map { race ->
            val sundayDate = LocalDate.parse(race.sessions.gp.split("T")[0])

            if (sundayDate.plusDays(1).isAfter(currentDate)) {
                var showFriday = true
                var showSaturday = true

                if (currentDate.isEqual(sundayDate)) {
                    showFriday = false
                    showSaturday = false
                } else if (currentDate.isEqual(sundayDate.minusDays(1))) {
                    showFriday = false
                }

                return VroomsInfo.Available(
                    round = race.round,
                    dateValue = buildDate(race.sessions.fp1, race.sessions.gp),
                    name = race.name,
                    nameImage = getTrack(race.slug).nameImage,
                    trackImage = getTrack(race.slug).trackImage,
                    sessions = buildSessions(race.sessions, showFriday, showSaturday)
                )
            }
        }

        return VroomsInfo.Unavailable("No next race")
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

fun buildSessions(sessions: Sessions, showFriday: Boolean, showSaturday: Boolean): List<SessionDate> {
    val sessionsList = mutableListOf<SessionDate>()

    sessions.sprint?.let {
        if (showFriday) {
            sessionsList.add(
                SessionDate(
                    "friday",
                    R.drawable.friday,
                    Session(
                        "free practice 1",
                        R.drawable.fp1,
                        "${getHour(sessions.fp1)}:${getMinute(sessions.fp1)}",
                        "${getHourPlusOne(sessions.fp1)}:${getMinute(sessions.fp1)}"
                    ),
                    Session(
                        "qualifying",
                        R.drawable.q,
                        "${getHour(sessions.qualifying)}:${getMinute(sessions.qualifying)}",
                        "${getHourPlusOne(sessions.qualifying)}:${getMinute(sessions.qualifying)}"
                    )
                )
            )
        }
        if (showSaturday) {
            sessionsList.add(
                SessionDate(
                    "saturday",
                    R.drawable.saturday,
                    Session(
                        "free practice 2",
                        R.drawable.fp2,
                        "${getHour(sessions.fp2)}:${getMinute(sessions.fp2)}",
                        "${getHourPlusOne(sessions.fp2)}:${getMinute(sessions.fp2)}"
                    ),
                    Session(
                        "sprint",
                        R.drawable.s,
                        "${getHour(it)}:${getMinute(it)}",
                        "${getHourPlusOne(it)}:${getMinute(it)}"
                    )
                )
            )
        }
    } ?: run {
        if (showFriday) {
            sessionsList.add(
                SessionDate(
                    "friday",
                    R.drawable.friday,
                    Session(
                        "free practice 1",
                        R.drawable.fp1,
                        "${getHour(sessions.fp1)}:${getMinute(sessions.fp1)}",
                        "${getHourPlusOne(sessions.fp1)}:${getMinute(sessions.fp1)}"
                    ),
                    Session(
                        "free practice 2",
                        R.drawable.fp2,
                        "${getHour(sessions.fp2)}:${getMinute(sessions.fp2)}",
                        "${getHourPlusOne(sessions.fp2)}:${getMinute(sessions.fp2)}"
                    )
                )
            )
        }
        if (showSaturday) {
            sessionsList.add(
                SessionDate(
                    "saturday",
                    R.drawable.saturday,
                    Session(
                        "free practice 3",
                        R.drawable.fp3,
                        "${getHour(sessions.fp3)}:${getMinute(sessions.fp3)}",
                        "${getHourPlusOne(sessions.fp3)}:${getMinute(sessions.fp3)}"
                    ),
                    Session(
                        "qualifying",
                        R.drawable.q,
                        "${getHour(sessions.qualifying)}:${getMinute(sessions.qualifying)}",
                        "${getHourPlusOne(sessions.qualifying)}:${getMinute(sessions.qualifying)}"
                    )
                )
            )
        }
    }

    sessionsList.add(SessionDate(
        "sunday",
        R.drawable.sunday,
        Session(
            "race",
            R.drawable.flag,
            "${getHour(sessions.gp)}:${getMinute(sessions.gp)}",
            null
        )
    ))

    return sessionsList
}

fun getHour(time: String?): String = ZonedDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime().hour.toString()

fun getHourPlusOne(time: String?): String = ZonedDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime().plusHours(1).hour.toString()

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
