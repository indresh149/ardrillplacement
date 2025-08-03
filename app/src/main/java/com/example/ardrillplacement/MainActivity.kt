package com.example.ardrillplacement

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var drillSpinner: Spinner
    private lateinit var startARButton: Button
    private var selectedDrillId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        setupDrillSpinner()
        setupClickListeners()
    }

    private fun initializeViews() {
        drillSpinner = findViewById(R.id.drillSpinner)
        startARButton = findViewById(R.id.startARButton)
    }

    private fun setupDrillSpinner() {
        val drills = listOf("Select a Drill", "Cone Drill", "Box Drill", "Ladder Drill")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, drills)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        drillSpinner.adapter = adapter

        drillSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedDrillId = position
                startARButton.isEnabled = position != 0
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                selectedDrillId = 0
                startARButton.isEnabled = false
            }
        }
    }

    private fun setupClickListeners() {
        startARButton.setOnClickListener {
            if (selectedDrillId > 0) {
                val intent = Intent(this, DrillDetailActivity::class.java)
                intent.putExtra("DRILL_ID", selectedDrillId)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please select a drill first", Toast.LENGTH_SHORT).show()
            }
        }
    }
}