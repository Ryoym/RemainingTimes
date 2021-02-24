package com.shima.remainingtimes

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shima.remainingtimes.databinding.ActivityMainBinding
import com.shima.remainingtimes.MainViewModel as MainViewModel1

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel1 by viewModels {
        Factory((application as SchedulesApplication).repository, application)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(
            this, R.layout.activity_main)


        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = ScheduleListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        viewModel.allSchedules.observe(this) { schedules ->
            schedules.let { adapter.submitList(it) }
        }

        binding.imageButton.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
    }
}
