package uk.co.josh9595.vroomswidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import uk.co.josh9595.vroomswidget.widget.Session
import uk.co.josh9595.vroomswidget.widget.SessionDate
import uk.co.josh9595.vroomswidget.widget.VroomsInfo

class MainActivity : ComponentActivity() {

    private val formulaFamily = FontFamily(
        Font(R.font.formula_regular, weight = FontWeight.Normal),
        Font(R.font.formula_bold, weight = FontWeight.Bold)
    )

    private val mainViewModel by viewModels<MainViewModel>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val widgetManager = AppWidgetManager.getInstance(this)
        val widgetProviders = widgetManager.getInstalledProvidersForPackage(packageName, null)

        mainViewModel.getSchedule()

        setContent {
            val colors = if (isSystemInDarkTheme()) {
                darkColors()
            } else {
                lightColors()
            }

            MaterialTheme(colors) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "Vrooms Widget",
                                    fontFamily = formulaFamily,
                                    fontWeight = FontWeight.Bold,
                                    style = TextStyle(
                                        color = colorResource(id = R.color.colorOnSurface),
                                        fontSize = 20.sp
                                    )
                                )
                            },
                            actions = {
                                IconButton(onClick = { widgetProviders[0].pin(applicationContext) }) {
                                    Icon(
                                        Icons.Default.Add,
                                        contentDescription = "Add widget",
                                        tint = colorResource(id = R.color.colorOnSurface)
                                    )
                                }
                            },
                            backgroundColor = colorResource(id = R.color.colorSurface),
                            elevation = 0.dp
                        )
                    }
                ) { contentPadding ->
                    LazyColumn(
                        modifier = Modifier
                            .padding(contentPadding)
                            .fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(mainViewModel.scheduleResponse) { info ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(24.dp))
                                    .background(colorResource(id = R.color.colorSurface))
                                    .padding(24.dp)
                            ) {
                                TrackImage(info)
                                Column {
                                    RaceImage(info)
                                    RaceDetails(info)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    AllSessionsContainer(info.sessions)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun TrackImage(info: VroomsInfo.Available) {
        Box(
            contentAlignment = Alignment.TopEnd,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(info.trackImage),
                contentDescription = null,
                modifier = Modifier.wrapContentHeight()
            )
        }
    }

    @Composable
    fun RaceImage(info: VroomsInfo.Available) {
        Image(
            painter = painterResource(info.nameImage),
            contentDescription = info.name
        )
    }

    @Composable
    fun RaceDetails(info: VroomsInfo.Available) {
        Text(
            text = "Round ${info.round} • ${info.dateValue}",
            style = TextStyle(
                fontSize = 16.sp,
                color = MaterialTheme.colors.onSurface
            )
        )
    }

    @Composable
    fun AllSessionsContainer(sessionDates: List<SessionDate>) {
        Row(Modifier.fillMaxWidth()) {
            SessionDate(
                sessionDate = sessionDates[0],
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(16.dp))
            SessionDate(
                sessionDate = sessionDates[1],
                modifier = Modifier.weight(1f)
            )
        }
        Row {
            SessionDate(
                sessionDate = sessionDates[2],
                modifier = Modifier
            )
        }
    }

    @Composable
    fun SessionDate(sessionDate: SessionDate, modifier: Modifier) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = modifier
        ) {
            SessionDateImage(sessionDate.dayImage)
            Spacer(modifier = Modifier.height(12.dp))
            Session(sessionDate.sessionOne)
            sessionDate.sessionTwo?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Session(it)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }

    @Composable
    fun SessionDateImage(id: Int) {
        Image(
            painter = painterResource(id),
            contentDescription = "friday"
        )
    }

    @Composable
    fun Session(session: Session) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .height(44.dp)
                    .width(44.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(colorResource(id = R.color.colorPrimaryContainer)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(session.nameImage),
                    contentDescription = session.name,
                    modifier = Modifier.height(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${session.time}${if (session.endTime != null) " - " + session.endTime else ""}",
                style = TextStyle(
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.onSurface
                )
            )
        }
    }

    private fun AppWidgetProviderInfo.pin(context: Context) {
        val successCallback = PendingIntent.getBroadcast(
            context,
            0,
            Intent(context, AppWidgetPinnedReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        AppWidgetManager.getInstance(context).requestPinAppWidget(provider, null, successCallback)
    }
}
