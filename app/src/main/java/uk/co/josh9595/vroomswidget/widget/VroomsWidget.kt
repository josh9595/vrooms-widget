package uk.co.josh9595.vroomswidget.widget

import android.content.Context
import android.graphics.drawable.Icon
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.glance.appwidget.lazy.GridCells
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.LazyVerticalGrid
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
        private val largeMode = DpSize(300.dp, 200.dp)
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
                    AppWidgetBox(modifier = GlanceModifier.background(GlanceTheme.colors.surface)) {
                        when (size) {
                            thinMode -> VroomsThin(vroomsInfo)
                            smallMode -> VroomsSmall(vroomsInfo)
                            mediumMode -> VroomsMedium(vroomsInfo)
                            largeMode -> VroomsLarge(vroomsInfo)
                        }
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
    // 2 titles max
    fun VroomsThin(info: VroomsInfo.Available) {
//        TrackImage(info)
        Column {
            RaceImage(info)
            RaceDetailsTiny(info)
        }
    }

    @Composable
    // 3 titles max
    fun VroomsSmall(info: VroomsInfo.Available) {
//        TrackImage(info)
        Column {
            RaceImage(info)
            RaceDetails(info)
            AllSessionsContainer(info.sessions)
        }
    }

    @Composable
    // 4 titles max
    fun VroomsMedium(info: VroomsInfo.Available) {
//        TrackImage(info)
        Column {
            RaceImage(info)
            RaceDetails(info)
            AllSessionsContainer(info.sessions)
        }
    }

    @Composable
    // 5 titles max
    fun VroomsLarge(info: VroomsInfo.Available) {
//        TrackImage(info)
        Column {
            RaceImage(info)
            RaceDetails(info)
            AllSessionsContainer(info.sessions)
        }
    }

    @Composable
    fun TrackImage(info: VroomsInfo.Available) {
        Image(
            provider = IconImageProvider(Icon.createWithResource(LocalContext.current, info.trackImage).setTint(
                ContextCompat.getColor(LocalContext.current, R.color.colorPrimary)
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
                text = "Round ${info.round} • ${info.dateValue}",
                style = TextStyle(
                    fontSize = 16.sp,
                    color = GlanceTheme.colors.onSurface
                )
            )
        }
    }

    @Composable
    fun RaceDetailsTiny(info: VroomsInfo.Available) {
        Row (modifier = GlanceModifier.padding(vertical = 4.dp)) {
            Text(
                text = info.dateValue,
                style = TextStyle(
                    fontSize = 16.sp,
                    color = GlanceTheme.colors.onSurface
                )
            )
        }
    }

    @Composable
    fun AllSessionsContainer(sessionDates: List<SessionDate>) {
        LazyVerticalGrid(
            gridCells = GridCells.Adaptive(minSize = 128.dp)
        ) {
            items(sessionDates) { sessionDate ->
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
            SessionDateImage(sessionDate.dayImage)
            Spacer(modifier = GlanceModifier.height(12.dp))
            Session(sessionDate.sessionOne)
            sessionDate.sessionTwo?.let {
                Spacer(modifier = GlanceModifier.height(8.dp))
                Session(it)
                Spacer(modifier = GlanceModifier.height(12.dp))
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
                modifier = GlanceModifier.height(44.dp).width(44.dp).cornerRadius(8.dp).background(GlanceTheme.colors.surfaceVariant),
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
                style = TextStyle(
                    fontSize = 16.sp,
                    color = GlanceTheme.colors.onSurfaceVariant
                )
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
