package com.shima.remainingtimes

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.time.LocalDateTime
import java.util.*

class Dialog(
    private val onSelected: (String) -> Unit) :DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val now = LocalDateTime.now()
        val year = now.year
        val month = now.monthValue - 1
        val day = now.dayOfMonth
        return DatePickerDialog(requireActivity(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        onSelected("$year/$(month + 1/$dayOfMonth")
    }
}

class TimeDialog(private val onSelected: (String) -> Unit)
    : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val now = LocalDateTime.now()
        val hour = now.hour
        val minute = now.minute
        return TimePickerDialog(context, this, hour, minute, true)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        onSelected("%1$02d:%2$02d".format(hourOfDay, minute))
    }
}
