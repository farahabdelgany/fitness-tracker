package com.example.fitnesstracker
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardPage(
    navController: NavHostController,
    exerciseViewModel: ExerciseViewModel = viewModel()
) {
    val exercises by exerciseViewModel.exercises.collectAsState()
    val isLoading by exerciseViewModel.isLoading.collectAsState()
    val error by exerciseViewModel.error.collectAsState()

    var expandedCardIndex by remember { mutableStateOf<Int?>(null) }
    var selectedMuscleName by remember { mutableStateOf("Select Muscle") }
    val muscleNames = listOf("Chest", "Biceps", "Glutes", "Neck", "Shoulders")
    var dropdownExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Exercises",
            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 32.sp),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            textAlign = TextAlign.Center
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { dropdownExpanded = !dropdownExpanded }
                    .border(1.dp, MaterialTheme.colorScheme.onBackground)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedMuscleName,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = if (dropdownExpanded) "▲" else "▼",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            DropdownMenu(
                expanded = dropdownExpanded,
                onDismissRequest = { dropdownExpanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                muscleNames.forEach { muscleName ->
                    DropdownMenuItem(
                        text = { Text(text = muscleName) },
                        onClick = {
                            selectedMuscleName = muscleName
                            dropdownExpanded = false
                            exerciseViewModel.fetchExercises(muscleName)
                        }
                    )
                }
            }
        }

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(64.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        } else if (exercises.isEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp)
                    .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.Top
            ) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Welcome to Fitness Tracker!",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Our app helps you find exercises based on the muscle groups you want to target. Simply select a muscle group from the dropdown menu and click on the muscle name to see a list of exercises tailored to your goals.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Explore exercises, track your progress, and stay motivated on your fitness journey!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp)
                    .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.Top
            ) {
                itemsIndexed(exercises) { index, exercise ->
                    StaggeredCard(
                        exercise = exercise,
                        isExpanded = index == expandedCardIndex,
                        onExpandClick = {
                            expandedCardIndex = if (expandedCardIndex == index) {
                                null
                            } else {
                                index
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun StaggeredCard(
    exercise: ExerciseResponse,
    isExpanded: Boolean,
    onExpandClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onExpandClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = if (isExpanded) MaterialTheme.shapes.large else MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                            MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                )
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = exercise.name,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Type: ${exercise.type}", color = MaterialTheme.colorScheme.onSurface)
            Text(text = "Muscle: ${exercise.muscle}", color = MaterialTheme.colorScheme.onSurface)
            Text(text = "Equipment: ${exercise.equipment}", color = MaterialTheme.colorScheme.onSurface)

            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = exercise.instructions,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            } else {
                Spacer(modifier = Modifier.height(8.dp))
                val displayInstructions = exercise.instructions.take(100) + if (exercise.instructions.length > 100) "..." else ""

                Text(
                    text = displayInstructions,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Read More",
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.clickable { onExpandClick() }
                )
            }
        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewDashboardPage() {
    // Mock data for preview
    val mockExercises = listOf(
        ExerciseResponse(
            name = "Incline Hammer Curls",
            type = "strength",
            muscle = "biceps",
            equipment = "dumbbell",
            difficulty = "beginner",
            instructions = "Seat yourself on an incline bench with a dumbbell in each hand. You should be pressed firmly against the back with your feet together..."
        ),
        ExerciseResponse(
            name = "Wide-grip barbell curl",
            type = "strength",
            muscle = "biceps",
            equipment = "barbell",
            difficulty = "beginner",
            instructions = "Stand up with your torso upright while holding a barbell at the wide outer handle. The palm of your hands should be facing forward..."
        )
    )

    // Mock ViewModel
    val exerciseViewModel = object : ExerciseViewModel() {
        override val exercises = MutableStateFlow(mockExercises).asStateFlow()
        override val isLoading = MutableStateFlow(false).asStateFlow()
        override val error = MutableStateFlow<String?>(null).asStateFlow()
    }

    DashboardPage(
        navController = rememberNavController(),
        exerciseViewModel = exerciseViewModel
    )
}