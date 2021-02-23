package com.shima.remainingtimes

data class UserSettings(
    val getUpTime: String?,
    val bedTime: String?,
    val morningRoutineStart: String?,
    val morningRoutineEnd: String?,
    val nightRoutineStart: String?,
    val nightRoutineEnd: String?,
    val workStart: String?,
    val workEnd: String?
)
