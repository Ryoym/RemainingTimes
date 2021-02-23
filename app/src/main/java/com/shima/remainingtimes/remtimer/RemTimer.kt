package com.shima.remainingtimes.remtimer

import com.shima.remainingtimes.RemTimeModel
import com.shima.remainingtimes.SettingKey
import com.shima.remainingtimes.UserSettings
import java.time.*
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.util.*

class RemTimer {

    fun settingMyRems(settings: UserSettings): RemTimeModel {
        val getUpTime = stringTimeToIntMap(settings.getUpTime!!)
        val bedTime = stringTimeToIntMap(settings.bedTime!!)
        val morningRoutineStart = stringTimeToIntMap(settings.morningRoutineStart!!)
        val morningRoutineEnd = stringTimeToIntMap(settings.morningRoutineEnd!!)
        val nightRoutineStart = stringTimeToIntMap(settings.nightRoutineStart!!)
        val nightRoutineEnd = stringTimeToIntMap(settings.nightRoutineEnd!!)
        val workStart = stringTimeToIntMap(settings.workStart!!)
        val workEnd = stringTimeToIntMap(settings.workEnd!!)

        val intMapUserSettings
        : Map<SettingKey, Pair<Map<ChronoUnit, Int>, Map<ChronoUnit, Int>>> = mapOf(
            SettingKey.SLEEP to Pair(getUpTime, bedTime),
            SettingKey.MORNING_ROUTINE to Pair(morningRoutineStart, morningRoutineEnd),
            SettingKey.NIGHT_ROUTINE to Pair(nightRoutineStart, nightRoutineEnd),
            SettingKey.WORK to Pair(workStart, workEnd)
        )

        val intGetUpTime = intMapToMillis(getUpTime)
        val intBedTime = intMapToMillis(bedTime)
        val intMorningRoutineStart = intMapToMillis(morningRoutineStart)
        val intMorningRoutineEnd = intMapToMillis(morningRoutineEnd)
        val intNightRoutineStart = intMapToMillis(nightRoutineStart)
        val intNightRoutineEnd = intMapToMillis(nightRoutineEnd)
        val intWorkStart = intMapToMillis(workStart)
        val intWorkEnd = intMapToMillis(workEnd)

        val sleep = routineTime(intBedTime, intGetUpTime)
        val morningRoutine = routineTime(intMorningRoutineStart, intMorningRoutineEnd)
        val nightRoutine = routineTime(intNightRoutineStart, intNightRoutineEnd)
        val working = routineTime(intWorkStart, intWorkEnd)

        val totalRoutineTime = (sleep + morningRoutine + nightRoutine + working)

        val myMinusRems = defaultMinusSettingTime(totalRoutineTime)
        val allRemMap: MutableMap<ChronoUnit, Long> =
            settingRemTimes(intMapUserSettings, myMinusRems)

        return setMyRemainingTimes(allRemMap)
    }

    private fun setMyRemainingTimes(allRems: MutableMap<ChronoUnit, Long>): RemTimeModel {
        val yearMilli = allRems[ChronoUnit.YEARS]!!
        val monthMilli = allRems[ChronoUnit.MONTHS]!!
        val weekMilli = allRems[ChronoUnit.WEEKS]!!
        val dayMilli = allRems[ChronoUnit.DAYS]!!

        val yMinute = (yearMilli / 1000 / 60)
        val yearMinute = (yMinute / 1000 % 60).toString()
        val yearHour = (yearMilli / 1000 / 60 / 60).toString()

        val mMinute = (monthMilli / 1000 / 60)
        val monthMinute = (mMinute / 1000 % 60).toString()
        val monthHour = (monthMilli / 1000 / 60 / 60).toString()

        val wMinute = (weekMilli / 1000 / 60)
        val weekMinute = (wMinute / 1000 % 60).toString()
        val weekHour = (weekMilli / 1000 / 60 / 60).toString()

        val dMinute = (dayMilli / 1000/ 60)
        val dayMinute = (dMinute / 1000 % 60).toString()
        val dayHour = (dayMilli / 1000 / 60 / 60).toString()

        return RemTimeModel(
            yearHour, yearMinute, monthHour, monthMinute, weekHour, weekMinute, dayHour, dayMinute
        )
    }

    private fun defaultMinusSettingTime(routineTime: Long): Map<ChronoUnit, Long> {
        val defaultRems = getRems()
        val rems: MutableMap<ChronoUnit, Long> = EnumMap(java.time.temporal.ChronoUnit::class.java)
        for ((key, value) in defaultRems) {
            val day = value.toDays()
            val days = if (day > 0) day - 1 else day
            val totalRoutineTime = (routineTime * (days))
            val remMillis = (value.toMillis() - totalRoutineTime)
            rems[key] = remMillis
        }
        return rems
    }

    private fun routineTime(start: Long, end: Long): Long {
        return if (start <= end) {
            end - start
        } else { end + (86400000 - start) }
    }

    private fun settingRemTimes(
        routineType: Map<SettingKey,Pair<Map<ChronoUnit, Int>, Map<ChronoUnit, Int>>>,
        myRems: Map<ChronoUnit, Long>
    ): MutableMap<ChronoUnit, Long> {

        val allRemMap
                : MutableMap<ChronoUnit, Long> = EnumMap(java.time.temporal.ChronoUnit::class.java)
        val now = ZonedDateTime.now(ZoneId.systemDefault())
        val nowMilli = now.toInstant().toEpochMilli()

        for ((remKey, remValue) in myRems) {
            var rems = remValue

            for ((_, value) in routineType) {

                val startMilli = toTodayEpoch(value.first)
                val endMilli = toTodayEpoch(value.second)

                val todayMinusSetMilli = if (startMilli > endMilli) {
                    startMilli - 86400000
                } else { startMilli }

                val result = when (nowMilli.compareTo(endMilli)) {
                    -1 -> (endMilli.minus(nowMilli))
                    else -> 0
                }
                val rem = when (nowMilli.compareTo(todayMinusSetMilli)) {
                    -1 -> remValue - result
                    else -> remValue
                }
                rems += rem - rems
            }
            allRemMap[remKey] = rems
        }
        return allRemMap
    }

    private fun intMapToMillis(map: Map<ChronoUnit, Int>): Long {
        val hourMilli = (map[ChronoUnit.HOURS]!! * 60 * 60 * 1000)
        val minuteMilli = (map[ChronoUnit.MINUTES]!! * 60 * 1000)
        return (hourMilli + minuteMilli).toLong()
    }

    private fun toTodayEpoch(target: Map<ChronoUnit, Int>): Long {
        val now = ZonedDateTime.now(ZoneId.systemDefault())
        val begNow = now.truncatedTo(ChronoUnit.DAYS)
        val settingToday = begNow.withHour(
            target[ChronoUnit.HOURS]?: 0).withMinute(target[ChronoUnit.MINUTES] ?: 0
        )
        val targetEpoch =  settingToday.toInstant().toEpochMilli()
        return targetEpoch
    }

    private fun stringTimeToIntMap(stringTime: String): Map<ChronoUnit, Int> {
        val list = stringTime.split(":")
        val hourStr = list[0]
        val minuteStr = list[1]
        val hour = hourStr.toInt()
        val minute = minuteStr.toInt()
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
