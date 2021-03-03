package com.shima.remainingtimes

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.*
import com.shima.remainingtimes.remtimer.RemTimer
import com.shima.remainingtimes.remtimer.TimeUnit
import kotlinx.coroutines.launch
import java.time.temporal.ChronoUnit

class MainViewModel(private val repository: ScheduleDataRepository, application: Application) : AndroidViewModel(application) {
    val allSchedules: LiveData<List<Schedule>> = repository.allSchedules.asLiveData()

    private val _title = MutableLiveData<String>()
    private val _dateTime = MutableLiveData<Long>()
    private val _detail = MutableLiveData<String>()

    val modeSwitch = MutableLiveData<Boolean>()

    val title: LiveData<String> = _title
    val dateTime: LiveData<Long> = _dateTime
    val detail: LiveData<String?> = _detail

    var defaultRemYearHour: String = RemTimer().setRemTime(ChronoUnit.YEARS, TimeUnit.HOUR)
    var defaultRemYearMinute: String = RemTimer().setRemTime(ChronoUnit.YEARS, TimeUnit.MINUTE)
    var defaultRemMonthHour: String = RemTimer().setRemTime(ChronoUnit.MONTHS, TimeUnit.HOUR)
    var defaultRemMonthMinute: String = RemTimer().setRemTime(ChronoUnit.MONTHS, TimeUnit.MINUTE)
    var defaultRemWeekHour: String = RemTimer().setRemTime(ChronoUnit.WEEKS, TimeUnit.HOUR)
    var defaultRemWeekMinute: String = RemTimer().setRemTime(ChronoUnit.WEEKS, TimeUnit.MINUTE)
    var defaultRemDayHour: String = RemTimer().setRemTime(ChronoUnit.DAYS, TimeUnit.HOUR)
    var defaultRemDayMinute: String = RemTimer().setRemTime(ChronoUnit.DAYS, TimeUnit.MINUTE)

    companion object{
        const val PREF = "main"
        const val defValue = "00:00"
    }
    private val pref: SharedPreferences = application.getSharedPreferences(PREF, Context.MODE_PRIVATE)
    val getUpTime = pref.stringLiveData(SettingKey.GETUP_TIME.name, defValue)
    val bedTime = pref.stringLiveData(SettingKey.BED_TIME.name, defValue)
    val morningRoutineStart = pref.stringLiveData(SettingKey.MORNING_ROUTINE_START.name, defValue)
    val morningRoutineEnd = pref.stringLiveData(SettingKey.MORNING_ROUTINE_END.name, defValue)
    val nightRoutineStart = pref.stringLiveData(SettingKey.NIGHT_ROUTINE_START.name, defValue)
    val nightRoutineEnd = pref.stringLiveData(SettingKey.NIGHT_ROUTINE_END.name, defValue)
    val workStart = pref.stringLiveData(SettingKey.WORK_START.name, defValue)
    val workEnd = pref.stringLiveData(SettingKey.WORK_END.name, defValue)

    var settings = RemTimer().settingMyRems(UserSettings(
        getUpTime.value,
        bedTime.value,
        morningRoutineStart.value,
        morningRoutineEnd.value,
        nightRoutineStart.value,
        nightRoutineEnd.value,
        workStart.value,
        workEnd.value))

    fun setTimes() {
        defaultRemYearHour = RemTimer().setRemTime(ChronoUnit.YEARS, TimeUnit.HOUR)
        defaultRemYearMinute = RemTimer().setRemTime(ChronoUnit.YEARS, TimeUnit.MINUTE)
        defaultRemMonthHour = RemTimer().setRemTime(ChronoUnit.MONTHS, TimeUnit.HOUR)
        defaultRemMonthMinute = RemTimer().setRemTime(ChronoUnit.MONTHS, TimeUnit.MINUTE)
        defaultRemWeekHour = RemTimer().setRemTime(ChronoUnit.WEEKS, TimeUnit.HOUR)
        defaultRemWeekMinute = RemTimer().setRemTime(ChronoUnit.WEEKS, TimeUnit.MINUTE)
        defaultRemDayHour = RemTimer().setRemTime(ChronoUnit.DAYS, TimeUnit.HOUR)
        defaultRemDayMinute = RemTimer().setRemTime(ChronoUnit.DAYS, TimeUnit.MINUTE)
        settings = RemTimer().settingMyRems((UserSettings(
                getUpTime.value,
                bedTime.value,
                morningRoutineStart.value,
                morningRoutineEnd.value,
                nightRoutineStart.value,
                nightRoutineEnd.value,
                workStart.value,
                workEnd.value)))
    }

    fun save(settingKey: SettingKey, text: String) {
        pref.edit().putString(settingKey.name, text).apply()
    }

    fun insert(schedule: Schedule) = viewModelScope.launch {
        repository.insert(schedule)
    }
}
class Factory(private val repository: ScheduleDataRepository, private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository, application) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }
}

enum class SettingKey {
    GETUP_TIME,
    BED_TIME,
    MORNING_ROUTINE_START,
    MORNING_ROUTINE_END,
    NIGHT_ROUTINE_START,
    NIGHT_ROUTINE_END,
    WORK_START,
    WORK_END,
    SLEEP,
    MORNING_ROUTINE,
    NIGHT_ROUTINE,
    WORK
}
