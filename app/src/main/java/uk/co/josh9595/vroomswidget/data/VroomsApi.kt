package uk.co.josh9595.vroomswidget.data

import retrofit2.http.GET

interface VroomsApi {
    @GET("2023.json")
    suspend fun getCalendar(): VroomsCalendar
}
