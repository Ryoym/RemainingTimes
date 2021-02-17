package com.shima.remainingtimes

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(schedule: Schedule)

    @Update
    fun update(schedule: Schedule)

    @Delete
    fun delete(schedule: Schedule)

    @Query("select * from schedules")
    fun getAll(): Flow<List<Schedule>>

    @Query("select * from schedules where id = :id")
    suspend fun getSchedule(id: Int): Schedule
}
