package com.shima.remainingtimes

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class ScheduleDataRepository(private val scheduleDao: ScheduleDao) {
    val allSchedules: Flow<List<Schedule>> = scheduleDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(schedule: Schedule) {
        scheduleDao.insert(schedule)
    }
}
