package com.shima.remainingtimes

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class SchedulesApplication() : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { ScheduleDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { ScheduleDataRepository(database) }
}
