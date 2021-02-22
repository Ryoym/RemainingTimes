package com.shima.remainingtimes

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.preference.PreferenceManager
import com.shima.remainingtimes.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels {
        Factory((application as SchedulesApplication).repository, application)
    }

    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_setting)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.setGetupTime.setOnClickListener { TimeDialog{ time ->
            binding.setGetupTime.text = time
            viewModel.save(SettingKey.GETUP_TIME, time)
        }.show(supportFragmentManager, "time_dialog") }

        binding.setGetupLabel.setOnClickListener { TimeDialog{ time ->
            binding.setGetupTime.text = time
            viewModel.save(SettingKey.GETUP_TIME, time)
        }.show(supportFragmentManager, "time_dialog") }

        binding.setBedtime.setOnClickListener { TimeDialog{ time ->
            binding.setBedtime.text = time
            viewModel.save(SettingKey.BED_TIME, time)
        }.show(supportFragmentManager, "time_dialog") }

        binding.setBedtimeLabel.setOnClickListener { TimeDialog{ time ->
            binding.setBedtime.text = time
            viewModel.save(SettingKey.BED_TIME, time)
        }.show(supportFragmentManager, "time_dialog") }

        binding.setMorningRoutine.setOnClickListener { TimeDialog{ time ->
            binding.setMorningRoutine.text = time
            viewModel.save(SettingKey.MORNING_ROUTINE_START, time)
        }.show(supportFragmentManager, "time_dialog") }

        binding.setMorningRoutineEnd.setOnClickListener { TimeDialog{ time ->
            binding.setMorningRoutineEnd.text = time
            viewModel.save(SettingKey.MORNING_ROUTINE_END, time)
        }.show(supportFragmentManager, "time_dialog") }

        binding.setNightRoutine.setOnClickListener { TimeDialog{ time ->
            binding.setNightRoutine.text = time
            viewModel.save(SettingKey.NIGHT_ROUTINE_START, time)
        }.show(supportFragmentManager, "time_dialog") }

        binding.setNightRoutineEnd.setOnClickListener { TimeDialog{ time ->
            binding.setNightRoutineEnd.text = time
            viewModel.save(SettingKey.NIGHT_ROUTINE_END, time)
        }.show(supportFragmentManager, "time_dialog") }

        binding.setWorkingStart.setOnClickListener { TimeDialog{ time ->
            binding.setWorkingStart.text = time
            viewModel.save(SettingKey.WORK_START, time)
        }.show(supportFragmentManager, "time_dialog") }

        binding.setWorkingEnd.setOnClickListener { TimeDialog{ time ->
            binding.setWorkingEnd.text = time
            viewModel.save(SettingKey.WORK_END, time)
        }.show(supportFragmentManager, "time_dialog") }

        //binding.saveButton.setOnClickListener { onSaveTapped() }
    }

    private fun onSaveTapped() {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        pref.edit {
            putString("GETUP", binding.setGetupTime.text.toString())
            putString("BED", binding.setBedtime.text.toString())
            putString("MORNING", binding.setMorningRoutine.text.toString())
            putString("MORNING_END", binding.setMorningRoutineEnd.text.toString())
            putString("NIGHT", binding.setNightRoutine.text.toString())
            putString("NIGHT_END", binding.setNightRoutineEnd.text.toString())
            putString("WORK", binding.setWorkingStart.text.toString())
            putString("WORK_END", binding.setWorkingEnd.text.toString())
        }
    }
}
