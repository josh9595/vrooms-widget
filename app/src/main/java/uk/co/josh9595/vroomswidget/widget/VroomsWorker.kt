package uk.co.josh9595.vroomswidget.widget

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.work.*
import uk.co.josh9595.vroomswidget.data.VroomsNetwork
import java.time.Duration

class VroomsWorker(
    private val context: Context,
    workerParameters: WorkerParameters
): CoroutineWorker(context, workerParameters) {

    companion object {
        private val uniqueWorkName = VroomsWorker::class.java.simpleName

        fun enqueue(context: Context, force: Boolean = false) {
            val manager = WorkManager.getInstance(context)
            val requestBuilder = PeriodicWorkRequestBuilder<VroomsWorker>(
                Duration.ofMinutes(30)
            )
            var workPolicy = ExistingPeriodicWorkPolicy.KEEP

            if (force) {
                workPolicy = ExistingPeriodicWorkPolicy.REPLACE
            }

            manager.enqueueUniquePeriodicWork(
                uniqueWorkName,
                workPolicy,
                requestBuilder.build()
            )
        }

        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(uniqueWorkName)
        }
    }

    override suspend fun doWork(): Result {
        val manager = GlanceAppWidgetManager(context)
        val glanceIds = manager.getGlanceIds(VroomsWidget::class.java)
        val calendar = VroomsNetwork.retrofit.getCalendar()

        return try {
            setWidgetState(glanceIds, VroomsInfo.Loading)
            setWidgetState(glanceIds, VroomsRepo.getVroomsInfo(calendar))

            Result.success()
        } catch (e: Exception) {
            setWidgetState(glanceIds, VroomsInfo.Unavailable(e.message.orEmpty()))
            if (runAttemptCount < 10) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }

    private suspend fun setWidgetState(glanceIds: List<GlanceId>, newState: VroomsInfo) {
        glanceIds.forEach { glanceId ->
            updateAppWidgetState(
                context = context,
                definition = VroomsInfoStateDefinition,
                glanceId = glanceId,
                updateState = { newState }
            )
        }
        VroomsWidget().updateAll(context)
    }

}