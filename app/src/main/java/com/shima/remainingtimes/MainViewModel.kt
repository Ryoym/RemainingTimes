package com.shima.remainingtimes

import androidx.lifecycle.*
import com.shima.remainingtimes.remtimer.RemTimer
import com.shima.remainingtimes.remtimer.TimeUnit
import kotlinx.coroutines.launch
import java.time.temporal.ChronoUnit

class MainViewModel(private val repository: ScheduleDataRepository) : ViewModel() {
    val allSchedules: LiveData<List<Schedule>> = repository.allSchedules.asLiveData()
    private val _title = MutableLiveData<String>()
    private val _date = MutableLiveData<Long>()
    private val _time = MutableLiveData<Long>()
    private val _detail = MutableLiveData<String?>()

    val title: LiveData<String> = _title
    val date: LiveData<Long> = _date
    val time: LiveData<Long> = _time
    val detail: LiveData<String?> = _detail


    val defaultRemYearHour: String = RemTimer().setRemTime(ChronoUnit.YEARS, TimeUnit.HOUR)
    val defaultRemYearMinute: String = RemTimer().setRemTime(ChronoUnit.YEARS, TimeUnit.MINUTE)
    val defaultRemMonthHour: String = RemTimer().setRemTime(ChronoUnit.MONTHS, TimeUnit.HOUR)
    val defaultRemMonthMinute: String = RemTimer().setRemTime(ChronoUnit.MONTHS, TimeUnit.MINUTE)
    val defaultRemWeekHour: String = RemTimer().setRemTime(ChronoUnit.WEEKS, TimeUnit.HOUR)
    val defaultRemWeekMinute: String = RemTimer().setRemTime(ChronoUnit.WEEKS, TimeUnit.MINUTE)
    val defaultRemDayHour: String = RemTimer().setRemTime(ChronoUnit.DAYS, TimeUnit.HOUR)
    val defaultRemDayMinute: String = RemTimer().setRemTime(ChronoUnit.DAYS, TimeUnit.MINUTE)

    fun insert(schedule: Schedule) = viewModelScope.launch {
        repository.insert(schedule)
    }
}


class ScheduleViewModelFactory(private val repository: ScheduleDataRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
