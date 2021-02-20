package com.shima.remainingtimes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedules")
data class Schedule(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val title: String,
    val dateTime: Long,
    val detail: String?
)
