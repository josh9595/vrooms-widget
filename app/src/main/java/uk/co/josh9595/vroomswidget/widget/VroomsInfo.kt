package uk.co.josh9595.vroomswidget.widget

import kotlinx.serialization.Serializable

@Serializable
sealed interface VroomsInfo {

    @Serializable
    object Loading: VroomsInfo

    @Serializable
    data class Available(
        val round: Int,
        val location: String,
        val name: String,
        val sessions: List<Session>
    ): VroomsInfo

    @Serializable
    data class Unavailable(
        val message: String
    ): VroomsInfo
}

@Serializable
data class Session(
    val name: String,
    val date: String,
    val time: String
)