package uk.co.josh9595.vroomswidget

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uk.co.josh9595.vroomswidget.data.VroomsNetwork
import uk.co.josh9595.vroomswidget.data.raceToVroomsInfo
import uk.co.josh9595.vroomswidget.widget.VroomsInfo
import java.time.LocalDate

class MainViewModel: ViewModel() {
    var scheduleResponse: List<VroomsInfo.Available> by mutableStateOf(emptyList())
        private set

    var nextRaceIndex: Int by mutableStateOf(0)
        private set

    fun getSchedule() {
        viewModelScope.launch {
            val apiService = VroomsNetwork.retrofit
            try {
                val scheduleList = apiService.getCalendar()
                val response = scheduleList.races.map {
                    raceToVroomsInfo(it)
                }
                scheduleResponse = response

                val currentDate = LocalDate.now()
                scheduleList.races.mapIndexed { index, it ->
                    val sundayDate = LocalDate.parse(it.sessions.gp.split("T")[0])
                    if (sundayDate.plusDays(1).isAfter(currentDate)) {
                        nextRaceIndex = index
                    }
                }
            }
            catch (_: Exception) {}
        }
    }
}