package com.shima.remainingtimes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.shima.remainingtimes.databinding.ActivityMainBinding
import com.shima.remainingtimes.MainViewModel as MainViewModel1

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel1 by viewModels {
        ScheduleViewModelFactory((application as SchedulesApplication).repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.lifecycleOwner = this
//        val scheduleViewModel = ViewModelProvider(this).get(MainViewModel1::class.java)
        binding.viewModel = viewModel
    }
}
