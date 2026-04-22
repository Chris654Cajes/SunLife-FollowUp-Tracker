package com.followup.tracker.ui.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.followup.tracker.data.entity.PrewrittenMessage
import com.followup.tracker.databinding.ActivityPrewrittenMessagesBinding
import com.followup.tracker.databinding.DialogAddMessageBinding
import com.followup.tracker.viewmodel.MessagesViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PrewrittenMessagesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrewrittenMessagesBinding
    private val viewModel: MessagesViewModel by viewModels()
    private lateinit var adapter: MessagesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrewrittenMessagesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Pre-Written Messages"
        binding.toolbar.setNavigationOnClickListener { finish() }

        setupRecyclerView()
        observeMessages()

        binding.fabAddMessage.setOnClickListener { showAddMessageDialog(null) }
    }

    private fun setupRecyclerView() {
        adapter = MessagesAdapter(
            onEdit = { showAddMessageDialog(it) },
            onDelete = { viewModel.deleteMessage(it) }
        )
        binding.recyclerMessages.layoutManager = LinearLayoutManager(this)
        binding.recyclerMessages.adapter = adapter
    }

    private fun observeMessages() {
        viewModel.allMessages.observe(this) { messages ->
            adapter.submitList(messages)
            binding.tvEmpty.visibility =
                if (messages.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
        }
    }

    private fun showAddMessageDialog(existing: PrewrittenMessage?) {
        val dialogBinding = DialogAddMessageBinding.inflate(LayoutInflater.from(this))

        val dayOptions = listOf("Day 1", "Day 3", "Day 7")
        val dayValues = listOf(1, 3, 7)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, dayOptions)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dialogBinding.spinnerDay.adapter = spinnerAdapter

        existing?.let {
            dialogBinding.etTitle.setText(it.title)
            dialogBinding.etBody.setText(it.body)
            val idx = dayValues.indexOf(it.dayOffset)
            if (idx >= 0) dialogBinding.spinnerDay.setSelection(idx)
        }

        MaterialAlertDialogBuilder(this)
            .setTitle(if (existing == null) "Add Message" else "Edit Message")
            .setView(dialogBinding.root)
            .setPositiveButton("Save") { _, _ ->
                val title = dialogBinding.etTitle.text.toString().trim()
                val body = dialogBinding.etBody.text.toString().trim()
                val dayOffset = dayValues[dialogBinding.spinnerDay.selectedItemPosition]
                if (title.isNotEmpty() && body.isNotEmpty()) {
                    val msg = PrewrittenMessage(
                        id = existing?.id ?: 0,
                        title = title,
                        body = body,
                        dayOffset = dayOffset
                    )
                    if (existing == null) viewModel.insertMessage(msg) else viewModel.updateMessage(msg)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
