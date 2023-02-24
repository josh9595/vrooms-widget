package uk.co.josh9595.vroomswidget.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object VroomsNetwork {
    val retrofit: VroomsApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/sportstimes/f1/main/_db/f1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VroomsApi::class.java)
    }
}