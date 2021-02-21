package com.shima.remainingtimes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.shima.remainingtimes.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivitySettingBinding>(
            this, R.layout.activity_setting)

        binding.setGetupTime.setOnClickListener { TimeDialog{ time ->
            binding.setGetupTime.text = time
        }.show(supportFragmentManager, "time_dialog") }

        binding.setGetupLabel.setOnClickListener { TimeDialog{ time ->
            binding.setGetupTime.text = time
        }.show(supportFragmentManager, "time_dialog") }

        binding.setBedtime.setOnClickListener { TimeDialog{ time ->
            binding.setBedtime.text = time
        }.show(supportFragmentManager, "time_dialog") }

        binding.setBedtimeLabel.setOnClickListener { TimeDialog{ time ->
            binding.setBedtime.text = time
        }.show(supportFragmentManager, "time_dialog") }

        binding.setMorningRoutine.setOnClickListener { TimeDialog{ time ->
            binding.setMorningRoutine.text = time
        }.show(supportFragmentManager, "time_dialog") }

        binding.setMorningRoutineEnd.setOnClickListener { TimeDialog{ time ->
            binding.setMorningRoutineEnd.text = time
        }.show(supportFragmentManager, "time_dialog") }

        binding.setNightRoutine.setOnClickListener { TimeDialog{ time ->
            binding.setNightRoutine.text = time
        }.show(supportFragmentManager, "time_dialog") }

        binding.setNightRoutineEnd.setOnClickListener { TimeDialog{ time ->
            binding.setNightRoutineEnd.text = time
        }.show(supportFragmentManager, "time_dialog") }

        binding.setWorkingStart.setOnClickListener { TimeDialog{ time ->
            binding.setWorkingStart.text = time
        }.show(supportFragmentManager, "time_dialog") }

        binding.setWorkingEnd.setOnClickListener { TimeDialog{ time ->
            binding.setWorkingEnd.text = time
        }.show(supportFragmentManager, "time_dialog") }

    }
}
