package uk.co.josh9595.vroomswidget.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class VroomsWidgetReceiver: GlanceAppWidgetReceiver() {
    override val glanceAppWidget = VroomsWidget()

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        VroomsWorker.enqueue(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        VroomsWorker.cancel(context)
    }
}