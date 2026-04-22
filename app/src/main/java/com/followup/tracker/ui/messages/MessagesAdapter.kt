package com.followup.tracker.ui.messages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.followup.tracker.data.entity.PrewrittenMessage
import com.followup.tracker.databinding.ItemMessageBinding

class MessagesAdapter(
    private val onEdit: (PrewrittenMessage) -> Unit,
    private val onDelete: (PrewrittenMessage) -> Unit
) : ListAdapter<PrewrittenMessage, MessagesAdapter.MessageViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MessageViewHolder(private val binding: ItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: PrewrittenMessage) {
            binding.tvTitle.text = message.title
            binding.tvBody.text = message.body
            binding.tvDay.text = "Day ${message.dayOffset}"
            binding.btnEdit.setOnClickListener { onEdit(message) }
            binding.btnDelete.setOnClickListener { onDelete(message) }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<PrewrittenMessage>() {
        override fun areItemsTheSame(oldItem: PrewrittenMessage, newItem: PrewrittenMessage) =
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: PrewrittenMessage, newItem: PrewrittenMessage) =
            oldItem == newItem
    }
}
