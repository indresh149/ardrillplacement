package com.example.ardrillplacement.data

object DrillRepository {

    private val drills = listOf(
        Drill(
            id = 1,
            name = "Cone Drill",
            description = "The Cone Drill is designed to improve agility, speed, and directional changes. Players navigate through a series of cones placed in various patterns, focusing on quick feet and body control.",
            tips = listOf(
                "Keep your center of gravity low for better balance",
                "Focus on quick, small steps rather than long strides",
                "Keep your head up and eyes forward",
                "Use your arms for balance and momentum",
                "Start slow and gradually increase speed as you improve"
            )
        ),
        Drill(
            id = 2,
            name = "Box Drill",
            description = "The Box Drill enhances footwork, coordination, and lateral movement. Players move in a square pattern around cones, practicing different movement techniques including forward, backward, and lateral shuffles.",
            tips = listOf(
                "Maintain proper athletic stance throughout the drill",
                "Don't cross your feet during lateral movements",
                "Push off with the outside foot when changing direction",
                "Keep your core engaged for stability",
                "Practice both clockwise and counterclockwise directions"
            )
        ),
        Drill(
            id = 3,
            name = "Ladder Drill",
            description = "The Ladder Drill improves foot speed, coordination, and agility. Players perform various footwork patterns through a ground ladder, developing neuromuscular coordination and quick feet.",
            tips = listOf(
                "Start with basic patterns before advancing to complex ones",
                "Land on the balls of your feet, not your heels",
                "Keep your arms bent at 90 degrees and pump them",
                "Look ahead, not down at your feet",
                "Focus on precision before speed",
                "Practice different patterns: in-in-out, lateral shuffle, ickey shuffle"
            )
        )
    )

    fun getDrillById(id: Int): Drill {
        return drills.find { it.id == id } ?: drills[0]
    }

    fun getAllDrills(): List<Drill> {
        return drills
    }
}