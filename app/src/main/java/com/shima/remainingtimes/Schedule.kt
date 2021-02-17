package com.shima.remainingtimes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedules")
data class Schedule(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val date: Long,
    val time: Long,
    val detail: String?
)
