package uk.co.josh9595.vroomswidget.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.LocalSize
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.text.Text
import uk.co.josh9595.vroomswidget.AppWidgetBox
import uk.co.josh9595.vroomswidget.AppWidgetColumn

class VroomsWidget: GlanceAppWidget() {
    companion object {
        private val thinMode = DpSize(120.dp, 120.dp)
        private val smallMode = DpSize(184.dp, 184.dp)
        private val mediumMode = DpSize(260.dp, 200.dp)
        private val largeMode = DpSize(260.dp, 280.dp)
    }

    override val stateDefinition = VroomsInfoStateDefinition

    override val sizeMode: SizeMode = SizeMode.Responsive(
        setOf(thinMode, smallMode, mediumMode, largeMode)
    )

    @Composable
    override fun Content() {
        val vroomsInfo = currentState<VroomsInfo>()
        val size = LocalSize.current

        when (vroomsInfo) {
            is VroomsInfo.Available -> {
                when (size) {
                    thinMode -> VroomsThin(vroomsInfo)
                    smallMode -> VroomsSmall(vroomsInfo)
                    mediumMode -> VroomsMedium(vroomsInfo)
                    largeMode -> VroomsLarge(vroomsInfo)
                }
            }
            VroomsInfo.Loading -> {
                AppWidgetBox(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is VroomsInfo.Unavailable -> {
                AppWidgetColumn(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Data not available")
                    Button("Refresh", actionRunCallback<UpdateVroomsAction>())
                }
            }
        }
    }

    @Composable
    fun VroomsThin(vroomsInfo: VroomsInfo.Available) {
        AppWidgetColumn {
            RaceInfo(
                round = vroomsInfo.round,
                name = vroomsInfo.name,
                location = vroomsInfo.location
            )
        }
    }

    @Composable
    fun VroomsSmall(vroomsInfo: VroomsInfo.Available) {
        AppWidgetColumn {
            RaceInfo(
                round = vroomsInfo.round,
                name = vroomsInfo.name,
                location = vroomsInfo.location
            )
        }
    }

    @Composable
    fun VroomsMedium(vroomsInfo: VroomsInfo.Available) {
        AppWidgetColumn {
            RaceInfo(
                round = vroomsInfo.round,
                name = vroomsInfo.name,
                location = vroomsInfo.location
            )
        }
    }

    @Composable
    fun VroomsLarge(vroomsInfo: VroomsInfo.Available) {
        AppWidgetColumn {
            RaceInfo(
                round = vroomsInfo.round,
                name = vroomsInfo.name,
                location = vroomsInfo.location
            )
        }
    }

    @Composable
    fun RaceInfo(
        round: Int,
        name: String,
        location: String
    ) {
        Column {
            Text(text = round.toString())
            Text(text = name)
            Text(text = location)
        }
    }

    class UpdateVroomsAction : ActionCallback {
        override suspend fun onAction(
            context: Context,
            glanceId: GlanceId,
            parameters: ActionParameters
        ) {
            VroomsWorker.enqueue(context)
        }

    }
}
