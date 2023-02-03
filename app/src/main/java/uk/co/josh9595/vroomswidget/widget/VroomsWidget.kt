package uk.co.josh9595.vroomswidget.widget

import android.content.Context
import android.graphics.drawable.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.glance.*
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
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

    val roundStyle = TextStyle(
        fontSize = 16.sp
    )

    val locationStyle = TextStyle(
        fontSize = 16.sp
    )

    val sessionStyle = TextStyle(
        fontSize = 18.sp
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
    fun VroomsThin(info: VroomsInfo.Available) {
        AppWidgetBox {
//            TrackImage(info)
            Column {
                RaceImage(info)
                RaceDetails(info)
                AllSessionsContainer(info)
            }
        }
    }

    @Composable
    fun VroomsSmall(info: VroomsInfo.Available) {
        AppWidgetBox {
//            TrackImage(info)
            Column {
                RaceImage(info)
                RaceDetails(info)
                AllSessionsContainer(info)
            }
        }
    }

    @Composable
    fun VroomsMedium(info: VroomsInfo.Available) {
        AppWidgetBox {
//            TrackImage(info)
            Column {
                RaceImage(info)
                RaceDetails(info)
                AllSessionsContainer(info)
            }
        }
    }

    @Composable
    fun VroomsLarge(info: VroomsInfo.Available) {
        AppWidgetBox {
//            TrackImage(info)
            Column {
                RaceImage(info)
                RaceDetails(info)
                AllSessionsContainer(info)
            }
        }
    }

    @Composable
    fun TrackImage(info: VroomsInfo.Available) {
        Image(
            provider = IconImageProvider(Icon.createWithResource(LocalContext.current, info.trackImage).setTint(
                ContextCompat.getColor(LocalContext.current, R.color.colorSurfaceVariant)
            )),
            contentDescription = info.name,
            modifier = GlanceModifier.fillMaxWidth().padding(8.dp),
            contentScale = ContentScale(1)
        )
    }

    @Composable
    fun RaceImage(info: VroomsInfo.Available) {
        Image(
            provider = IconImageProvider(Icon.createWithResource(LocalContext.current, info.nameImage).setTint(
                ContextCompat.getColor(LocalContext.current, R.color.colorPrimary)
            )),
            contentDescription = info.name
        )
    }

    @Composable
    fun RaceDetails(info: VroomsInfo.Available) {
        Row (modifier = GlanceModifier.padding(vertical = 4.dp)) {
            Text(
                text = "Round ${info.round}",
                style = roundStyle
            )
            Spacer(modifier = GlanceModifier.width(4.dp))
            Text(text = "•")
            Spacer(modifier = GlanceModifier.width(4.dp))
            Text(
                text = info.location,
                style = locationStyle
            )
        }
    }

    @Composable
    fun AllSessionsContainer(info: VroomsInfo.Available) {
        LazyColumn (
            modifier = GlanceModifier.fillMaxWidth()
        ) {
            items(info.sessions) { sessionDate ->
                SessionDate(
                    sessionDate = sessionDate,
                    modifier = GlanceModifier
                )
            }
        }
    }

    @Composable
    fun SessionDate(sessionDate: SessionDate, modifier: GlanceModifier) {
        Column (
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
        ){
            Spacer(modifier = GlanceModifier.height(12.dp))
            SessionDateImage(sessionDate.dayImage)
            Spacer(modifier = GlanceModifier.height(12.dp))
            Session(sessionDate.sessionOne)
            sessionDate.sessionTwo?.let {
                Spacer(modifier = GlanceModifier.height(8.dp))
                Session(it)
            }

        }
    }

    @Composable
    fun SessionDateImage(id: Int) {
        Image(
            provider = IconImageProvider(Icon.createWithResource(LocalContext.current, id).setTint(
                ContextCompat.getColor(LocalContext.current, R.color.colorPrimary)
            )),
            contentDescription = "friday"
        )
    }

    @Composable
    fun Session(session: Session) {
        Row (
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(
                modifier = GlanceModifier.height(48.dp).width(48.dp).cornerRadius(8.dp).background(GlanceTheme.colors.surfaceVariant),
                contentAlignment = Alignment(Alignment.CenterHorizontally,Alignment.CenterVertically)
            ) {
                Image(
                    provider = IconImageProvider(
                        Icon.createWithResource(LocalContext.current, session.nameImage)
                            .setTint(
                                ContextCompat.getColor(LocalContext.current, R.color.colorPrimary)
                            )
                    ),
                    contentDescription = "friday",
                    modifier = GlanceModifier.height(20.dp)
                )
            }
            Spacer(modifier = GlanceModifier.width(8.dp))
            Text(
                text = "${session.time}${if (session.endTime != null) " - " + session.endTime else ""}",
                style = sessionStyle
            )
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
