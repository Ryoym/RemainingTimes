package com.shima.remainingtimes

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class ScheduleDataRepository(private val database: ScheduleDatabase) {
    val allSchedules: Flow<List<Schedule>> = database.scheduleDao().getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(schedule: Schedule) {
        database.scheduleDao().insert(schedule)
    }
}
