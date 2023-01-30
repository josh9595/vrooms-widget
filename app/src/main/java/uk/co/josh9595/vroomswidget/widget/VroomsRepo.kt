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
                    Session(
                        name = "FP1",
                        time = "11:30"
                    ),
                    Session(
                        name = "FP2",
                        time = "15:00"
                    )
                ),
                SessionDate(
                    date = "04 MAR",
                    Session(
                        name = "FP3",
                        time = "11:30"
                    ),
                    Session(
                        name = "Q",
                        time = "15:00"
                    )
                ),
                SessionDate(
                    "05 Mar",
                    Session(
                        name = "R",
                        time = "15:00"
                    )
                )
            )
        )
    }

    // races https://raw.githubusercontent.com/sportstimes/f1/main/_db/f1/2023.json
}