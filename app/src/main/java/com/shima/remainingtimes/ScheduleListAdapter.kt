package com.shima.remainingtimes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shima.remainingtimes.ScheduleListAdapter.ScheduleViewHolder

class ScheduleListAdapter : ListAdapter<Schedule, ScheduleViewHolder>(SCHEDULES_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        return ScheduleViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.id, current.title, current.dateTime)
    }

    class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val scheduleItemView: TextView = itemView.findViewById(R.id.recyclerLineId)
        private val scheduleTitleView: TextView = itemView.findViewById(R.id.recyclerLineTitle)
        private val scheduleDateTimeView: TextView = itemView.findViewById(R.id.recyclerDateTime)

        fun bind(id: Long, title: String, datetime: Long) {
            scheduleItemView.text = id.toString()
            scheduleTitleView.text = title
            scheduleDateTimeView.text = datetime.toString()
        }

        companion object {
            fun create(parent: ViewGroup): ScheduleViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)
                return ScheduleViewHolder(view)
            }
        }
    }

    companion object {
        private val SCHEDULES_COMPARATOR = object : DiffUtil.ItemCallback<Schedule>() {
            override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
