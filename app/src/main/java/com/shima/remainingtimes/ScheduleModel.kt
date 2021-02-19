package com.shima.remainingtimes

data class ScheduleModel(
    val id: Long,
    val title: String,
    val date: Long,
    val time: Long,
    val detail: String?
)
