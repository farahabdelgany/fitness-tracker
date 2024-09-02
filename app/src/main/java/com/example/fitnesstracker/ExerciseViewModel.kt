// ExerciseViewModel.kt
package com.example.fitnesstracker

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class ExerciseViewModel : ViewModel() {
    @RequiresApi(Build.VERSION_CODES.O)
    private val exerciseService = RetrofitClient.instance

    private val _exercises = MutableStateFlow<List<ExerciseResponse>>(emptyList())
    open val exercises: StateFlow<List<ExerciseResponse>> = _exercises.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    open val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    open val error: StateFlow<String?> = _error.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchExercises(muscle: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = exerciseService.getExercises(muscle)
                _exercises.value = response
                Log.d("ExerciseViewModel", "Fetched exercises: $response")
            } catch (e: Exception) {
                _error.value = "Failed to fetch exercises"
                Log.e("ExerciseViewModel", "Error fetching exercises", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
