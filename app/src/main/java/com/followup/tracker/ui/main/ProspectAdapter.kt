package com.followup.tracker.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.followup.tracker.R
import com.followup.tracker.data.entity.Prospect
import com.followup.tracker.databinding.ItemProspectBinding
import java.text.SimpleDateFormat
import java.util.*

class ProspectAdapter(
    private val onDelete: (Prospect) -> Unit
) : ListAdapter<Prospect, ProspectAdapter.ProspectViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProspectViewHolder {
        val binding = ItemProspectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProspectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProspectViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ProspectViewHolder(private val binding: ItemProspectBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(prospect: Prospect) {
            binding.tvName.text = prospect.name
            binding.tvContact.text = prospect.contact
            binding.tvStatus.text = prospect.status

            val (bgColor, textColor) = when (prospect.status) {
                "Hot" -> Pair(R.color.status_hot_bg, R.color.status_hot_text)
                "Warm" -> Pair(R.color.status_warm_bg, R.color.status_warm_text)
                else -> Pair(R.color.status_cold_bg, R.color.status_cold_text)
            }
            binding.tvStatus.backgroundTintList =
                ContextCompat.getColorStateList(binding.root.context, bgColor)
            binding.tvStatus.setTextColor(
                ContextCompat.getColor(binding.root.context, textColor)
            )

            val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            binding.tvDate.text = "Added: ${sdf.format(Date(prospect.createdAt))}"

            binding.btnDelete.setOnClickListener { onDelete(prospect) }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Prospect>() {
        override fun areItemsTheSame(oldItem: Prospect, newItem: Prospect) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Prospect, newItem: Prospect) = oldItem == newItem
    }
}
