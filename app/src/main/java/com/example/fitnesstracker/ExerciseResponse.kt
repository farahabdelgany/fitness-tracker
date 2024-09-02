// ExerciseResponse.kt
package com.example.fitnesstracker

data class ExerciseResponse(
    val name: String,
    val type: String,
    val muscle: String,
    val equipment: String,
    val difficulty: String,
    val instructions: String
)
