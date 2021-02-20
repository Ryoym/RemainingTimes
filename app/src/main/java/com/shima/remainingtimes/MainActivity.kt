package com.shima.remainingtimes

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
        ScheduleViewModelFactory((application as SchedulesApplication).repository)
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
        binding.fab.setOnClickListener {
            val intent = Intent(this@MainActivity, SettingActivity::class.java)
            startActivity(intent)
        }
    }
}
