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
    val sessionOne: Session,
    val sessionTwo: Session? = null
)

@Serializable
data class Session(
    val name: String,
    val time: String
)