package uk.co.josh9595.vroomswidget.widget

object VroomsRepo {
    suspend fun getVroomsInfo(): VroomsInfo {
        return VroomsInfo.Available(
            round = 1,
            location = "Bahrain",
            name = "Bahrain Grand Prix",
            sessions = listOf(
                Session(
                    name = "FP1",
                    date = "03 Mar",
                    time = "11:30 - 12:30"
                ),
                Session(
                    name = "FP2",
                    date = "03 Mar",
                    time = "15:00 - 16:00"
                ),
                Session(
                    name = "FP3",
                    date = "04 Mar",
                    time = "11:30 - 12:30"
                ),
                Session(
                    name = "Q",
                    date = "04 Mar",
                    time = "15:00 - 16:00"
                ),
                Session(
                    name = "R",
                    date = "05 Mar",
                    time = "15:00"
                )
            )
        )
    }

    // races https://raw.githubusercontent.com/sportstimes/f1/main/_db/f1/2023.json
}