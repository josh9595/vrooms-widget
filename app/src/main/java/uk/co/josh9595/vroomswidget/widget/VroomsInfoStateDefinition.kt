package uk.co.josh9595.vroomswidget.widget

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import androidx.datastore.dataStoreFile
import androidx.glance.state.GlanceStateDefinition
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.File
import java.io.InputStream
import java.io.OutputStream

object VroomsInfoStateDefinition: GlanceStateDefinition<VroomsInfo> {

    private const val DATA_STORE_FILENAME = "vroomsInfo"

    private val Context.datastore by dataStore(DATA_STORE_FILENAME, VroomsInfoSerializer)

    override suspend fun getDataStore(context: Context, fileKey: String): DataStore<VroomsInfo> {
        return context.datastore
    }

    override fun getLocation(context: Context, fileKey: String): File {
        return context.dataStoreFile(DATA_STORE_FILENAME)
    }

    object VroomsInfoSerializer: Serializer<VroomsInfo> {
        override val defaultValue = VroomsInfo.Unavailable("")

        override suspend fun readFrom(input: InputStream): VroomsInfo = try {
            Json.decodeFromString(
                VroomsInfo.serializer(),
                input.readBytes().decodeToString()
            )
        } catch (exception: SerializationException) {
            throw CorruptionException("Could not read vrooms data: ${exception.message}")
        }

        override suspend fun writeTo(t: VroomsInfo, output: OutputStream) {
            output.use {
                it.write(
                    Json.encodeToString(VroomsInfo.serializer(), t).encodeToByteArray()
                )
            }
        }
    }
}