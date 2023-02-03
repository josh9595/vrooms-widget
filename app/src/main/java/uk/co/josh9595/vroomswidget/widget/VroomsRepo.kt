package uk.co.josh9595.vroomswidget.widget

import uk.co.josh9595.vroomswidget.R

object VroomsRepo {
    suspend fun getVroomsInfo(): VroomsInfo {
        return VroomsInfo.Available(
            round = 1,
            location = "Bahrain",
            name = "Bahrain Grand Prix",
            nameImage = R.drawable.bahrain,
            trackImage = R.drawable.bahrain_track,
            sessions = listOf(
                SessionDate(
                    "03 MAR",
                    R.drawable.friday,
                    Session(
                        name = "FP1",
                        nameImage = R.drawable.fp1,
                        time = "11:30",
                        endTime = "12:30"
                    ),
                    Session(
                        name = "FP2",
                        nameImage = R.drawable.fp2,
                        time = "15:00",
                        endTime = "16:00"
                    )
                ),
                SessionDate(
                    date = "04 MAR",
                    R.drawable.saturday,
                    Session(
                        name = "FP3",
                        nameImage = R.drawable.fp3,
                        time = "11:30",
                        endTime = "12:30"
                    ),
                    Session(
                        name = "Q",
                        nameImage = R.drawable.q,
                        time = "15:00",
                        endTime = "16:00"
                    )
                ),
                SessionDate(
                    "05 Mar",
                    R.drawable.sunday,
                    Session(
                        name = "R",
                        nameImage = R.drawable.flag,
                        time = "14:00",
                        endTime = null
                    )
                )
            )
        )
    }

    // races https://raw.githubusercontent.com/sportstimes/f1/main/_db/f1/2023.json
}