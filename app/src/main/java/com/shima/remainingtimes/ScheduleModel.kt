package com.shima.remainingtimes

data class ScheduleModel(
    val id: Long,
    val title: String,
    val dateTime: Long,
    val detail: String?
)
