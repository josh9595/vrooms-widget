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

class MainViewModel: ViewModel() {
    var scheduleResponse: List<VroomsInfo.Available> by mutableStateOf(emptyList())
        private set

    fun getSchedule() {
        viewModelScope.launch {
            val apiService = VroomsNetwork.retrofit
            try {
                val scheduleList = apiService.getCalendar()
                val response = scheduleList.races.map {
                    raceToVroomsInfo(it, showFriday = true, showSaturday = true)
                }
                scheduleResponse = response
            }
            catch (_: Exception) {}
        }
    }
}