package com.duke.orca.android.kotlin.travels.schedule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.duke.orca.android.kotlin.travels.databinding.ScheduleBinding
import com.duke.orca.android.kotlin.travels.schedule.data.Schedule
import java.text.SimpleDateFormat
import java.util.*

class ScheduleAdapter(private val onItemClick: (item: Schedule) -> Unit): ListAdapter<Schedule, ScheduleAdapter.ViewHolder>(DiffCallback()) {
    private val simpleDateFormat = SimpleDateFormat("M/d - ", Locale.getDefault())
    private var inflater: LayoutInflater? = null

    inner class ViewHolder(private val binding: ScheduleBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Schedule) {
            binding.textViewSpotName.text = item.tourspotname
            binding.textViewBeginDate.text = item.tourbegindate.format()
            binding.textViewBeginTime.text = item.tourbegintime

            Glide.with(binding.root.context)
                    .load(item.tourimg)
                    .transform(CenterCrop(), RoundedCorners(16))
                    .into(binding.imageViewImg)

            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    private fun from(binding: ScheduleBinding) = ViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ScheduleBinding.inflate(inflater ?: run {
            inflater = LayoutInflater.from(parent.context)

            requireNotNull(inflater)
        }, parent, false)

        return from(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private fun Date.format() = simpleDateFormat.format(this)
}

class DiffCallback: DiffUtil.ItemCallback<Schedule>() {
    override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
        return oldItem == newItem
    }
}