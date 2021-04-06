package com.duke.orca.android.kotlin.travels.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.duke.orca.android.kotlin.travels.databinding.TouristSpotBinding
import com.duke.orca.android.kotlin.travels.home.data.Tour

class TourAdapter(
    private val items: ArrayList<Tour>,
    private val onItemClick: (Tour) -> Unit
): RecyclerView.Adapter<TourAdapter.ViewHolder>() {
    private var inflater: LayoutInflater? = null

    class ViewHolder(private val viewBinding: TouristSpotBinding): RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(item: Tour) {

        }
    }

    private fun from(parent: ViewGroup): ViewHolder {
        return ViewHolder(TouristSpotBinding.inflate(
            inflater ?: LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.count()
}