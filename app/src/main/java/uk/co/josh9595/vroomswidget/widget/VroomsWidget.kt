package uk.co.josh9595.vroomswidget.widget

import android.content.Context
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
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.GridCells
import androidx.glance.appwidget.lazy.LazyVerticalGrid
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.*
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import uk.co.josh9595.vroomswidget.*

class VroomsWidget: GlanceAppWidget() {
    companion object {
        private val smallestMode = DpSize(120.dp, 60.dp)
        private val thinMode = DpSize(120.dp, 120.dp)
        private val smallMode = DpSize(184.dp, 184.dp)
        private val mediumMode = DpSize(220.dp, 200.dp)
        private val largeMode = DpSize(320.dp, 200.dp)
    }

    override val stateDefinition = VroomsInfoStateDefinition

    override val sizeMode: SizeMode = SizeMode.Responsive(
        setOf(smallestMode, thinMode, smallMode, mediumMode, largeMode)
    )

    override suspend fun provideGlance(context: Context, id: GlanceId) = provideContent {
        val vroomsInfo = currentState<VroomsInfo>()
        val size = LocalSize.current

        VroomsGlanceTheme {
            when (vroomsInfo) {
                is VroomsInfo.Available -> {
                    AppWidgetBox(
                        modifier = GlanceModifier.background(VroomsGlanceTheme.colors.surface)
                    ) {
                        when (size) {
                            smallestMode -> VroomsSmallest(vroomsInfo)
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
    fun VroomsSmallest(info: VroomsInfo.Available) {
        Column {
            RaceImage(info)
            RaceDetailsTiny(info)
        }
    }

    @Composable
    fun VroomsThin(info: VroomsInfo.Available) {
        TrackImage(info)
        Column {
            RaceImage(info)
            RaceDetailsTiny(info)
            AllSessionsContainer(info.sessions, false)
        }
    }

    @Composable
    fun VroomsSmall(info: VroomsInfo.Available) {
        TrackImage(info)
        Column {
            RaceImage(info)
            RaceDetails(info)
            AllSessionsContainer(info.sessions)
        }
    }

    @Composable
    fun VroomsMedium(info: VroomsInfo.Available) {
        TrackImage(info)
        Column {
            RaceImage(info)
            RaceDetails(info)
            AllSessionsContainer(info.sessions)
        }
    }

    @Composable
    fun VroomsLarge(info: VroomsInfo.Available) {
        TrackImage(info)
        Column {
            RaceImage(info)
            RaceDetails(info)
            AllSessionsContainer(info.sessions)
        }
    }

    @Composable
    fun TrackImage(
        info: VroomsInfo.Available
    ) {
        Box(
            contentAlignment = Alignment.TopEnd,
            modifier = GlanceModifier.fillMaxSize()
        ) {
            Image(
                provider = ImageProvider(info.trackImage),
                contentDescription = null,
                modifier = GlanceModifier.wrapContentHeight()
            )
        }
    }

    @Composable
    fun RaceImage(
        info: VroomsInfo.Available
    ) {
        Image(
            provider = ImageProvider(info.nameImage),
            contentDescription = info.name
        )
    }

    @Composable
    fun RaceDetails(info: VroomsInfo.Available) {
        Row (modifier = GlanceModifier.padding(bottom = 4.dp)) {
            Text(
                text = "Round ${info.round} • ${info.dateValue}",
                style = TextStyle(
                    fontSize = 16.sp,
                    color = VroomsGlanceTheme.colors.onSurface
                )
            )
        }
    }

    @Composable
    fun RaceDetailsTiny(info: VroomsInfo.Available) {
        Row (modifier = GlanceModifier.padding(bottom = 4.dp)) {
            Text(
                text = info.dateValue,
                style = TextStyle(
                    fontSize = 16.sp,
                    color = VroomsGlanceTheme.colors.onSurface
                )
            )
        }
    }

    @Composable
    fun AllSessionsContainer(sessionDates: List<SessionDate>, includeEndTime: Boolean = true) {
        LazyVerticalGrid(
            gridCells = GridCells.Adaptive(minSize = 156.dp),
            modifier = GlanceModifier.padding(top = 8.dp)
        ) {
            items(sessionDates.filter { !it.hasPassed }) { sessionDate ->
                SessionDate(
                    sessionDate = sessionDate,
                    modifier = GlanceModifier,
                    includeEndTime = includeEndTime
                )
            }
        }
    }

    @Composable
    fun SessionDate(sessionDate: SessionDate, modifier: GlanceModifier, includeEndTime: Boolean) {
        Column (
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
        ){
            SessionDateImage(sessionDate.dayImage)
            Spacer(modifier = GlanceModifier.height(8.dp))
            Session(sessionDate.sessionOne, includeEndTime)
            sessionDate.sessionTwo?.let {
                Spacer(modifier = GlanceModifier.height(8.dp))
                Session(it, includeEndTime)
                Spacer(modifier = GlanceModifier.height(8.dp))
            }
        }
    }

    @Composable
    fun SessionDateImage(
        id: Int
    ) {
        Image(
            provider = ImageProvider(id),
            contentDescription = "friday"
        )
    }

    @Composable
    fun Session(
        session: Session,
        includeEndTime: Boolean
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(
                modifier = GlanceModifier.height(40.dp).width(40.dp).cornerRadius(8.dp).background(VroomsGlanceTheme.colors.primaryContainer),
                contentAlignment = Alignment(Alignment.CenterHorizontally,Alignment.CenterVertically)
            ) {
                Image(
                    provider = ImageProvider(session.nameImage),
                    contentDescription = session.name,
                    modifier = GlanceModifier.height(20.dp)
                )
            }
            Spacer(modifier = GlanceModifier.width(8.dp))
            Text(
                text = "${session.time}${if (session.endTime != null && includeEndTime) " - " + session.endTime else ""}",
                style = TextStyle(
                    fontSize = 16.sp,
                    color = VroomsGlanceTheme.colors.onPrimaryContainer
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
