package com.example.ardrillplacement

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.core.Anchor
import com.google.ar.core.Config
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.core.Session
import com.google.ar.core.TrackingState
import com.google.ar.core.exceptions.CameraNotAvailableException
import com.google.ar.core.exceptions.UnavailableApkTooOldException
import com.google.ar.core.exceptions.UnavailableArcoreNotInstalledException
import com.google.ar.core.exceptions.UnavailableDeviceNotCompatibleException
import com.google.ar.core.exceptions.UnavailableSdkTooOldException
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.MaterialFactory
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.ShapeFactory
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode

class ARActivity : AppCompatActivity() {

    private lateinit var arFragment: ArFragment
    private lateinit var instructionText: TextView
    private lateinit var exitButton: Button
    private lateinit var clearButton: Button

    private var drillId: Int = 0
    private var drillName: String = ""
    private var placedAnchorNode: AnchorNode? = null
    private var isTracking = false
    private var hasPlacedObject = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar)

        drillId = intent.getIntExtra("DRILL_ID", 0)
        drillName = intent.getStringExtra("DRILL_NAME") ?: "Unknown Drill"

        initializeViews()
        setupARScene()
        setupClickListeners()
    }

    private fun initializeViews() {
        instructionText = findViewById(R.id.instructionText)
        exitButton = findViewById(R.id.exitButton)
        clearButton = findViewById(R.id.clearButton)

        instructionText.text = "Move your phone to scan the ground"
        clearButton.visibility = View.GONE
    }

    private fun setupARScene() {
        arFragment = supportFragmentManager.findFragmentById(R.id.arFragment) as ArFragment

        // Configure AR session
        arFragment.planeDiscoveryController.setInstructionView(null)
        arFragment.arSceneView.scene.addOnUpdateListener { frameTime ->
            onUpdate()
        }

        // Set up tap listener
        arFragment.setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            if (!hasPlacedObject && plane.type == Plane.Type.HORIZONTAL_UPWARD_FACING) {
                placeObject(hitResult)
            }
        }
    }

    private fun onUpdate() {
        val frame = arFragment.arSceneView.arFrame
        if (frame == null || frame.camera.trackingState != TrackingState.TRACKING) {
            return
        }

        // Update tracking status
        if (!isTracking) {
            isTracking = true
            runOnUiThread {
                instructionText.text = "Tap on the ground to place $drillName marker"
            }
        }
    }

    private fun placeObject(hitResult: HitResult) {
        // Create anchor
        val anchor = hitResult.createAnchor()
        placedAnchorNode = AnchorNode(anchor)
        placedAnchorNode?.setParent(arFragment.arSceneView.scene)

        // Create 3D object based on drill type
        when (drillId) {
            1 -> createCone() // Cone Drill
            2 -> createCube() // Box Drill
            3 -> createPyramid() // Ladder Drill
            else -> createCone()
        }
    }

    private fun createCone() {
        MaterialFactory.makeOpaqueWithColor(this, com.google.ar.sceneform.rendering.Color(1.0f, 0.5f, 0.0f))
            .thenAccept { material ->
                val cone = ShapeFactory.makeCylinder(
                    0.15f, // radius
                    0.3f,  // height
                    Vector3(0f, 0.15f, 0f), // center
                    material
                )
                addNodeToScene(cone)
            }
    }

    private fun createCube() {
        MaterialFactory.makeOpaqueWithColor(this, com.google.ar.sceneform.rendering.Color(0.0f, 0.5f, 1.0f))
            .thenAccept { material ->
                val cube = ShapeFactory.makeCube(
                    Vector3(0.2f, 0.2f, 0.2f), // size
                    Vector3(0f, 0.1f, 0f), // center
                    material
                )
                addNodeToScene(cube)
            }
    }

    private fun createPyramid() {
        MaterialFactory.makeOpaqueWithColor(this, com.google.ar.sceneform.rendering.Color(0.0f, 1.0f, 0.0f))
            .thenAccept { material ->
                // Create a cone to represent pyramid
                val pyramid = ShapeFactory.makeCylinder(
                    0.0001f, // top radius (very small to create pyramid effect)
                    0.25f,   // height
                    Vector3(0f, 0.125f, 0f), // center
                    material
                )
                addNodeToScene(pyramid)
            }
    }

    private fun addNodeToScene(renderable: ModelRenderable) {
        val node = TransformableNode(arFragment.transformationSystem)
        node.renderable = renderable
        node.setParent(placedAnchorNode)
        node.select()

        hasPlacedObject = true

        runOnUiThread {
            instructionText.text = "$drillName marker placed!"
            clearButton.visibility = View.VISIBLE
            Toast.makeText(this, "$drillName marker placed successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearPlacedObject() {
        placedAnchorNode?.anchor?.detach()
        placedAnchorNode?.setParent(null)
        placedAnchorNode = null
        hasPlacedObject = false

        instructionText.text = "Tap on the ground to place $drillName marker"
        clearButton.visibility = View.GONE
    }

    private fun setupClickListeners() {
        exitButton.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            clearPlacedObject()
        }
    }

    override fun onPause() {
        super.onPause()
        arFragment.arSceneView.pause()
    }

    override fun onResume() {
        super.onResume()
        try {
            arFragment.arSceneView.resume()
        } catch (e: CameraNotAvailableException) {
            Toast.makeText(this, "Camera not available", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}