package com.shima.remainingtimes.remtimer

import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.util.*
import kotlin.collections.HashMap

class RemTimer {
    fun setRemTime(unitType: ChronoUnit, timeUnit: TimeUnit): String {
        val defaultRems = getRems()
        val remTime: Long? = when (timeUnit) {
            TimeUnit.HOUR -> defaultRems[unitType]?.toHours()

            TimeUnit.MINUTE -> defaultRems[unitType]?.toMinutes()
        }

        return remTime.toString()
    }

    private fun getRems(): Map<ChronoUnit, Duration> {
        val now: LocalDateTime = LocalDateTime.now()
        val next: Map<ChronoUnit, LocalDateTime> = getNextMap()
        val remOfNext: MutableMap<ChronoUnit, Duration> = HashMap()

        for ((key, value) in next) {
            val duration = Duration.between(now, value)
            remOfNext[key] = duration
        }
        return remOfNext
    }

    private fun getNextMap(): Map<ChronoUnit, LocalDateTime> {
        val begNext: MutableMap<ChronoUnit, LocalDateTime> = HashMap()
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
