package com.shima.remainingtimes.remtimer

import com.shima.remainingtimes.RemTimeModel
import com.shima.remainingtimes.SettingKey
import com.shima.remainingtimes.UserSettings
import java.time.*
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.util.*

class RemTimer {
    companion object {
        const val ONE_DAY: Long = 86400000
    }

    fun settingMyRems(settings: UserSettings): RemTimeModel {
        val getUpTime = stringTimeToIntMap(settings.getUpTime!!)
        val bedTime = stringTimeToIntMap(settings.bedTime!!)
        val morningRoutineStart = stringTimeToIntMap(settings.morningRoutineStart!!)
        val morningRoutineEnd = stringTimeToIntMap(settings.morningRoutineEnd!!)
        val nightRoutineStart = stringTimeToIntMap(settings.nightRoutineStart!!)
        val nightRoutineEnd = stringTimeToIntMap(settings.nightRoutineEnd!!)
        val workStart = stringTimeToIntMap(settings.workStart!!)
        val workEnd = stringTimeToIntMap(settings.workEnd!!)

        val intGetUpTime = intMapToMillis(getUpTime)
        val intBedTime = intMapToMillis(bedTime)
        val intMorningRoutineStart = intMapToMillis(morningRoutineStart)
        val intMorningRoutineEnd = intMapToMillis(morningRoutineEnd)
        val intNightRoutineStart = intMapToMillis(nightRoutineStart)
        val intNightRoutineEnd = intMapToMillis(nightRoutineEnd)
        val intWorkStart = intMapToMillis(workStart)
        val intWorkEnd = intMapToMillis(workEnd)

        val daySleep = createDayRoutineTime(intBedTime, intGetUpTime)
        val dayMorningRoutine = createDayRoutineTime(intMorningRoutineStart, intMorningRoutineEnd)
        val dayNightRoutine = createDayRoutineTime(intNightRoutineStart, intNightRoutineEnd)
        val dayWorking = createDayRoutineTime(intWorkStart, intWorkEnd)

        val todayDaySleep = todayRoutineTime(intBedTime, intGetUpTime)
        val todayDayMorningRoutine =
                todayRoutineTime(intMorningRoutineStart, intMorningRoutineEnd)
        val todayDayNightRoutine =
                todayRoutineTime(intNightRoutineStart, intNightRoutineEnd)
        val todayDayWorking = todayRoutineTime(intWorkStart, intWorkEnd)
        val todayTotalRoutine = (
                todayDaySleep + todayDayMorningRoutine + todayDayNightRoutine + todayDayWorking
                )

        val totalRoutineTime = routines(
                daySleep, dayMorningRoutine, dayNightRoutine, dayWorking, todayTotalRoutine
        )

        return setMyRemainingTimes(totalRoutineTime)
    }

    private fun setMyRemainingTimes(allRems: MutableMap<ChronoUnit, Long>): RemTimeModel {
        val yearMilli = allRems[ChronoUnit.YEARS]!!
        val monthMilli = allRems[ChronoUnit.MONTHS]!!
        val weekMilli = allRems[ChronoUnit.WEEKS]!!
        val dayMilli = allRems[ChronoUnit.DAYS]!!

        val yearMinute = (monthMilli / 1000 / 60 % 60).toString()
        val yearHour = (yearMilli / 1000 / 60 / 60).toString()

        val monthMinute = (monthMilli / 1000 / 60 % 60).toString()
        val monthHour = (monthMilli / 1000 / 60 / 60).toString()

        val weekMinute = (weekMilli / 1000 / 60 % 60).toString()
        val weekHour = (weekMilli / 1000 / 60 / 60).toString()

        val dayMinute = (dayMilli / 1000 / 60 % 60).toString()
        val dayHour = (dayMilli / 1000 / 60 / 60).toString()

        return RemTimeModel(
            yearHour, yearMinute, monthHour, monthMinute, weekHour, weekMinute, dayHour, dayMinute
        )
    }

    private fun routines(sleep: Long, morning: Long, night: Long, work: Long, todayTotal: Long): MutableMap<ChronoUnit, Long> {
        val totalRems: MutableMap<ChronoUnit, Long> =
                EnumMap(java.time.temporal.ChronoUnit::class.java)
        val now = LocalDateTime.now()
        val target = getNextMap()
        val dayRoutine = (sleep + morning + night + work)

        for ((key, value) in target) {
            val duration = Duration.between(now, value)
            val day = duration.toDays()
            val durationMilli = duration.toMillis()
            val rems = if (day <= 1L) {
                durationMilli - todayTotal
            } else {
                (duration.toMillis() - (day * dayRoutine) - todayTotal)
            }
            totalRems[key] = rems
        }
        return totalRems
    }

    private fun createDayRoutineTime(startDate: Long, endDate: Long): Long {
        return if (startDate <= endDate) {
            endDate - startDate
        } else {
            (ONE_DAY - startDate + endDate)
        }
    }

    private fun todayRoutineTime(startDate: Long, endDate: Long): Long {
        val now = LocalDateTime.now()
        val nowHourMilli = (now.hour * 60 * 60 * 1000).toLong()
        val nowMinuteMilli = (now.minute * 60 * 1000).toLong()
        val nowDate: Long = nowHourMilli + nowMinuteMilli
        var result: Long = 0
        if (startDate < nowDate) {
            if (nowDate < endDate) {
                result = endDate - nowDate
            } else if (endDate < startDate) {
                result = ONE_DAY - nowDate
            }
        } else if (endDate < nowDate) {
            if (nowDate < startDate) {
                result = ONE_DAY - startDate
            }
        } else if (nowDate < startDate) {
            if (startDate < endDate) {
                result = endDate - startDate
            } else if (nowDate < endDate) {
                result = (endDate - nowDate) + (ONE_DAY - startDate)
            }
        }
        return result
    }

    private fun intMapToMillis(map: Map<ChronoUnit, Long>): Long {
        val hourMilli = (map[ChronoUnit.HOURS]!! * 60 * 60 * 1000)
        val minuteMilli = (map[ChronoUnit.MINUTES]!! * 60 * 1000)
        return (hourMilli + minuteMilli)
    }

    private fun stringTimeToIntMap(stringTime: String): Map<ChronoUnit, Long> {
        val list = stringTime.split(":")
        val hourStr = list[0]
        val minuteStr = list[1]
        val hour = hourStr.toLong()
        val minute = minuteStr.toLong()
        return mapOf(ChronoUnit.HOURS to hour, ChronoUnit.MINUTES to minute)
    }

    fun setRemTime(unitType: ChronoUnit, timeUnit: TimeUnit): String {
        val defaultRems = getRems()
        val minutes = defaultRems[unitType]?.toMinutes()?: 0
        val remTime: Long = when (timeUnit) {
            TimeUnit.HOUR -> (minutes / 60)

            TimeUnit.MINUTE -> (minutes % 60)
        }
        return remTime.toString()
    }

    private fun getRems(): Map<ChronoUnit, Duration> {
        val now: LocalDateTime = LocalDateTime.now()
        val next: Map<ChronoUnit, LocalDateTime> = getNextMap()
        val remOfNext: MutableMap<ChronoUnit, Duration> = EnumMap(java.time.temporal.ChronoUnit::class.java)

        for ((key, value) in next) {
            val duration = Duration.between(now, value)
            remOfNext[key] = duration
        }
        return remOfNext
    }

    private fun getNextMap(): Map<ChronoUnit, LocalDateTime> {
        val begNext: MutableMap<ChronoUnit, LocalDateTime> = EnumMap(java.time.temporal.ChronoUnit::class.java)
        val now: LocalDateTime = LocalDateTime.now()

        val begNextYear: LocalDateTime =
            now.plusYears(1)
                .withMonth(1)
                .withDayOfMonth(1)
                .truncatedTo(ChronoUnit.DAYS)

        val begNextMonth: LocalDateTime =
            now.plusMonths(1)
                .withDayOfMonth(1)
                .truncatedTo(ChronoUnit.DAYS)

        val begNextWeek: LocalDateTime =
            now.with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .truncatedTo(ChronoUnit.DAYS)


        val begNextDay: LocalDateTime =
            now.plusDays(1)
                .truncatedTo(ChronoUnit.DAYS)

        begNext[ChronoUnit.YEARS] = begNextYear
        begNext[ChronoUnit.MONTHS] = begNextMonth
        begNext[ChronoUnit.WEEKS] = begNextWeek
        begNext[ChronoUnit.DAYS] = begNextDay

        return begNext
    }
}

enum class TimeUnit{
    HOUR, MINUTE
}
