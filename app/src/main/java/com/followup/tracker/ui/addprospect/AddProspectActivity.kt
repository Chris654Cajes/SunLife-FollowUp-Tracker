package com.followup.tracker.ui.addprospect

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.followup.tracker.data.entity.Prospect
import com.followup.tracker.databinding.ActivityAddProspectBinding
import com.followup.tracker.notification.NotificationHelper
import com.followup.tracker.viewmodel.AddProspectViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AddProspectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddProspectBinding
    private val viewModel: AddProspectViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProspectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Add Prospect"

        binding.btnSave.setOnClickListener { saveProspect() }
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun saveProspect() {
        val name = binding.etName.text.toString().trim()
        val contact = binding.etContact.text.toString().trim()
        val status = when (binding.rgStatus.checkedRadioButtonId) {
            binding.rbHot.id -> "Hot"
            binding.rbWarm.id -> "Warm"
            binding.rbCold.id -> "Cold"
            else -> "Warm"
        }

        if (name.isEmpty()) {
            binding.tilName.error = "Name is required"
            return
        }
        if (contact.isEmpty()) {
            binding.tilContact.error = "Contact is required"
            return
        }

        binding.tilName.error = null
        binding.tilContact.error = null

        lifecycleScope.launch {
            val prospect = Prospect(name = name, contact = contact, status = status)
            val prospectId = viewModel.insertProspect(prospect)

            // Schedule follow-ups: Day 1, Day 3, Day 7
            val now = System.currentTimeMillis()
            listOf(1, 3, 7).forEach { days ->
                val triggerAt = now + TimeUnit.DAYS.toMillis(days.toLong())
                val followUp = com.followup.tracker.data.entity.FollowUp(
                    prospectId = prospectId,
                    scheduledAt = triggerAt,
                    dayOffset = days
                )
                val app = application as com.followup.tracker.FollowUpApp
                val fuId = app.database.followUpDao().insert(followUp)
                NotificationHelper.scheduleFollowUpAlarm(
                    this@AddProspectActivity,
                    fuId,
                    name,
                    days,
                    triggerAt
                )
            }

            Toast.makeText(
                this@AddProspectActivity,
                "✅ $name added! Follow-ups scheduled for Day 1, 3 & 7.",
                Toast.LENGTH_LONG
            ).show()
            finish()
        }
    }
}
