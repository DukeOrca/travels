package com.duke.orca.android.kotlin.travels.schedule.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.duke.orca.android.kotlin.travels.databinding.DetailBinding
import com.duke.orca.android.kotlin.travels.util.hide
import com.duke.orca.android.kotlin.travels.util.show

class DetailAdapter: ListAdapter<DetailItem, DetailAdapter.ViewHolder>(DiffCallback()) {
    private var inflater: LayoutInflater? = null

    inner class ViewHolder(private val binding: DetailBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DetailItem) {
            item.iconId?.let {
                binding.imageViewIcon.show()
                binding.imageViewIcon.setImageResource(it)
            } ?: run {
                binding.imageViewIcon.hide()
            }

            binding.textViewContent.text = item.content
            binding.textViewTitle.text = item.title

            binding.root.setOnClickListener {
                item.onItemClick(item)
            }
        }
    }

    private fun from(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        val binding = DetailBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return from(inflater ?: LayoutInflater.from(parent.context).apply {
            inflater = this
        }, parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

data class DetailItem(
        val content: String,
        val iconId: Int?,
        val onItemClick: (item: DetailItem) -> Unit,
        val title: String
)

class DiffCallback: DiffUtil.ItemCallback<DetailItem>() {
    override fun areItemsTheSame(oldItem: DetailItem, newItem: DetailItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: DetailItem, newItem: DetailItem): Boolean {
        return oldItem == newItem
    }
}