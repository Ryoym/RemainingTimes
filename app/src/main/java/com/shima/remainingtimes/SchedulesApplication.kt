package com.shima.remainingtimes

import android.app.Application

class SchedulesApplication : Application() {
    val database by lazy { ScheduleDatabase.getDatabase(this) }
    val repository by lazy { ScheduleDataRepository(database.scheduleDao()) }
}
