package uk.co.josh9595.vroomswidget.widget

import android.content.Context
import android.graphics.drawable.Icon
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.*
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.layout.*
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import uk.co.josh9595.vroomswidget.*
import uk.co.josh9595.vroomswidget.R

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

        GlanceTheme {
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
    }

    @Composable
    fun VroomsThin(vroomsInfo: VroomsInfo.Available) {
        AppWidgetBox {
            RaceInfo(
                round = vroomsInfo.round,
                name = vroomsInfo.name,
                nameImage = vroomsInfo.nameImage,
                trackImage = vroomsInfo.trackImage,
                location = vroomsInfo.location,
                vroomsInfo.sessions
            )
        }
    }

    @Composable
    fun VroomsSmall(vroomsInfo: VroomsInfo.Available) {
        AppWidgetBox {
            RaceInfo(
                round = vroomsInfo.round,
                name = vroomsInfo.name,
                nameImage = vroomsInfo.nameImage,
                trackImage = vroomsInfo.trackImage,
                location = vroomsInfo.location,
                vroomsInfo.sessions
            )
        }
    }

    @Composable
    fun VroomsMedium(vroomsInfo: VroomsInfo.Available) {
        AppWidgetBox {
            RaceInfo(
                round = vroomsInfo.round,
                name = vroomsInfo.name,
                nameImage = vroomsInfo.nameImage,
                trackImage = vroomsInfo.trackImage,
                location = vroomsInfo.location,
                vroomsInfo.sessions
            )
        }
    }

    @Composable
    fun VroomsLarge(vroomsInfo: VroomsInfo.Available) {
        AppWidgetBox {
            RaceInfo(
                round = vroomsInfo.round,
                name = vroomsInfo.name,
                nameImage = vroomsInfo.nameImage,
                trackImage = vroomsInfo.trackImage,
                location = vroomsInfo.location,
                vroomsInfo.sessions
            )
        }
    }

    @Composable
    fun RaceInfo(
        round: Int,
        name: String,
        nameImage: Int,
        trackImage: Int,
        location: String,
        sessions: List<SessionDate>
    ) {
        val roundStyle = TextStyle(
            fontSize = 16.sp
        )
        val nameStyle = TextStyle(
            fontSize = 24.sp
        )
        val locationStyle = TextStyle(
            fontSize = 16.sp
        )

        Image(
            provider = IconImageProvider(Icon.createWithResource(LocalContext.current, trackImage).setTint(
                LocalContext.current.resources.getColor(R.color.colorOnPrimary)
            )),
            contentDescription = name,
            modifier = GlanceModifier.fillMaxWidth().padding(8.dp)
        )

        Column {
            Text(
                text = "Round $round",
                style = roundStyle
            )
            Image(
                provider = IconImageProvider(Icon.createWithResource(LocalContext.current, nameImage).setTint(
                    LocalContext.current.resources.getColor(R.color.colorPrimary)
                )),
                contentDescription = name,
                modifier = GlanceModifier.height(40.dp)
            )
            Text(
                text = location,
                style = locationStyle
            )
            Row (
                modifier = GlanceModifier.fillMaxWidth()
            ) {
                Session(
                    session = sessions[0],
                    modifier = GlanceModifier.defaultWeight()
                )
                Session(
                    session = sessions[1],
                    modifier = GlanceModifier.defaultWeight()
                )
                Session(
                    session = sessions[2],
                    modifier = GlanceModifier.defaultWeight()
                )
            }
        }
    }

    @Composable
    fun Session(session: SessionDate, modifier: GlanceModifier) {
        Column (
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
        ){
            Image(
                provider = IconImageProvider(Icon.createWithResource(LocalContext.current, R.drawable.friday).setTint(
                    LocalContext.current.resources.getColor(R.color.colorPrimary)
                )),
                contentDescription = "friday",
                modifier = GlanceModifier.height(20.dp)
            )
            Column {
                Text(text = "${session.sessionOne.name} ${session.sessionOne.time}")
                session.sessionTwo?.let {
                    Text(text ="${it.name} ${it.time}")
                }
            }
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
