package uk.co.josh9595.vroomswidget.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.LocalSize
import androidx.glance.appwidget.GlanceAppWidget
import uk.co.josh9595.vroomswidget.AppWidgetColumn

class VroomsWidget: GlanceAppWidget() {
    companion object {
        private val thinMode = DpSize(120.dp, 120.dp)
        private val smallMode = DpSize(184.dp, 184.dp)
        private val mediumMode = DpSize(260.dp, 200.dp)
        private val largeMode = DpSize(260.dp, 280.dp)
    }

    @Composable
    override fun Content() {
        val size = LocalSize.current

        when (size) {
            thinMode -> VroomsThin()
            smallMode -> VroomsSmall()
            mediumMode -> VroomsMedium()
            largeMode -> VroomsLarge()
        }
    }

    @Composable
    fun VroomsThin() {
        AppWidgetColumn {

        }
    }

    @Composable
    fun VroomsSmall() {
        AppWidgetColumn {

        }
    }

    @Composable
    fun VroomsMedium() {
        AppWidgetColumn {

        }
    }

    @Composable
    fun VroomsLarge() {
        AppWidgetColumn {

        }
    }

    @Composable
    @Preview
    fun ThinPreview() {
        VroomsThin()
    }

    @Composable
    @Preview
    fun SmallPreview() {
        VroomsSmall()
    }

    @Composable
    @Preview
    fun MediumPreview() {
        VroomsMedium()
    }

    @Composable
    @Preview
    fun LargePreview() {
        VroomsLarge()
    }

}
