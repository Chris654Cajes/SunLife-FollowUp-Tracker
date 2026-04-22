package com.followup.tracker.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.followup.tracker.R
import com.followup.tracker.databinding.ActivityMainBinding
import com.followup.tracker.ui.addprospect.AddProspectActivity
import com.followup.tracker.ui.messages.PrewrittenMessagesActivity
import com.followup.tracker.viewmodel.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: ProspectAdapter

    private val notifPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* handle gracefully */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        requestNotificationPermission()
        setupRecyclerView()
        observeProspects()

        binding.fabAddProspect.setOnClickListener {
            startActivity(Intent(this, AddProspectActivity::class.java))
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                notifPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = ProspectAdapter(
            onDelete = { prospect ->
                MaterialAlertDialogBuilder(this)
                    .setTitle("Delete Prospect")
                    .setMessage("Remove ${prospect.name}? All follow-ups will be cancelled.")
                    .setPositiveButton("Delete") { _, _ ->
                        viewModel.deleteProspect(prospect, this)
                        Snackbar.make(binding.root, "${prospect.name} deleted", Snackbar.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        )
        binding.recyclerProspects.layoutManager = LinearLayoutManager(this)
        binding.recyclerProspects.adapter = adapter
    }

    private fun observeProspects() {
        viewModel.allProspects.observe(this) { prospects ->
            adapter.submitList(prospects)
            binding.tvEmpty.visibility =
                if (prospects.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_messages -> {
                startActivity(Intent(this, PrewrittenMessagesActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
