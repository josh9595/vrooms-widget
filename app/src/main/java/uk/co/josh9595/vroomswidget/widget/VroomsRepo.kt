package uk.co.josh9595.vroomswidget.widget

import uk.co.josh9595.vroomswidget.R
import uk.co.josh9595.vroomswidget.data.Sessions
import uk.co.josh9595.vroomswidget.data.Tracks
import uk.co.josh9595.vroomswidget.data.VroomsCalendar
import uk.co.josh9595.vroomswidget.data.raceToVroomsInfo
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

                return raceToVroomsInfo(race, showFriday, showSaturday)
            }
        }

        return VroomsInfo.Unavailable("No next race")
    }
}
