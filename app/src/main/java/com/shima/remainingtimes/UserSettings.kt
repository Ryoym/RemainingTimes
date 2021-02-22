package com.shima.remainingtimes

data class UserSettings(
    val getUpTime: Long,
    val bedTime: Long,
    val morningRoutineStart: Long,
    val morningRoutineEnd: Long,
    val nightRoutineStart: Long,
    val nightRoutineEnd: Long,
    val workStart: Long,
    val workEnd: Long
)
