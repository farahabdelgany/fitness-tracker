// ExerciseService.kt
package com.example.fitnesstracker
import retrofit2.http.GET
import retrofit2.http.Query

interface ExerciseService {
    @GET("v1/exercises")
    suspend fun getExercises(@Query("muscle") muscle: String): List<ExerciseResponse>



}
