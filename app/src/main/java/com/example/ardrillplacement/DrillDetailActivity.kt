package com.example.ardrillplacement

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ardrillplacement.data.Drill
import com.example.ardrillplacement.data.DrillRepository

class DrillDetailActivity : AppCompatActivity() {

    private lateinit var drillImageView: ImageView
    private lateinit var drillNameTextView: TextView
    private lateinit var drillDescriptionTextView: TextView
    private lateinit var drillTipsTextView: TextView
    private lateinit var startARButton: Button
    private lateinit var backButton: Button

    private var drillId: Int = 0
    private lateinit var currentDrill: Drill

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drill_detail)

        drillId = intent.getIntExtra("DRILL_ID", 0)

        initializeViews()
        loadDrillData()
        setupClickListeners()
    }

    private fun initializeViews() {
        drillImageView = findViewById(R.id.drillImageView)
        drillNameTextView = findViewById(R.id.drillNameTextView)
        drillDescriptionTextView = findViewById(R.id.drillDescriptionTextView)
        drillTipsTextView = findViewById(R.id.drillTipsTextView)
        startARButton = findViewById(R.id.startARSceneButton)
        backButton = findViewById(R.id.backButton)
    }

    private fun loadDrillData() {
        currentDrill = DrillRepository.getDrillById(drillId)

        drillNameTextView.text = currentDrill.name
        drillDescriptionTextView.text = currentDrill.description
        drillTipsTextView.text = buildString {
            append("Tips:\n")
            currentDrill.tips.forEachIndexed { index, tip ->
                append("${index + 1}. $tip\n")
            }
        }

        // Set drill image based on drill ID
        val imageResource = when (drillId) {
            1 -> R.drawable.cone_drill
            2 -> R.drawable.box_drill
            3 -> R.drawable.ladder_drill
            else -> R.drawable.default_drill
        }
        drillImageView.setImageResource(imageResource)
    }

    private fun setupClickListeners() {
        startARButton.setOnClickListener {
            val intent = Intent(this, ARActivity::class.java)
            intent.putExtra("DRILL_ID", drillId)
            intent.putExtra("DRILL_NAME", currentDrill.name)
            startActivity(intent)
        }

        backButton.setOnClickListener {
            finish()
        }
    }
}