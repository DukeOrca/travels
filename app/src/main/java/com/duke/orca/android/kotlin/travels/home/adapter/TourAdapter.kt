package com.duke.orca.android.kotlin.travels.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.duke.orca.android.kotlin.travels.databinding.TourBinding
import com.duke.orca.android.kotlin.travels.home.data.Tour
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class TourAdapter(private val onItemClick: (Tour) -> Unit): ListAdapter<Tour, TourAdapter.ViewHolder>(DiffCallback()) {
    private val simpleDateFormat = SimpleDateFormat("M/d", Locale.getDefault())
    private var inflater: LayoutInflater? = null

    inner class ViewHolder(private val binding: TourBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Tour) {
            binding.textViewSpotName.text = item.tourspotname
            binding.textViewBeginDate.text = item.tourbegindate.format()
            binding.textViewBeginTime.text = item.tourbegintime

            Timber.d("tourimg: ${item.tourimg}")

            Glide.with(binding.root.context)
                .load(item.tourimg)
                .transform(CenterCrop(), RoundedCorners(16))
                .into(binding.imageViewImg)

            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    private fun from(parent: ViewGroup): ViewHolder {
        return ViewHolder(TourBinding.inflate(
            inflater ?: LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private fun Date.format() = simpleDateFormat.format(this)

}

class DiffCallback: DiffUtil.ItemCallback<Tour>() {
    override fun areItemsTheSame(oldItem: Tour, newItem: Tour): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Tour, newItem: Tour): Boolean {
        return oldItem == newItem
    }
}