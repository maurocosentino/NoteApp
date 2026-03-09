package com.mauro.mynoteapp
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mauro.mynoteapp.notes.NoteDetailScreen
import com.mauro.mynoteapp.notes.NotesScreen
import com.mauro.mynoteapp.theme.MyNoteAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            MyNoteAppTheme {

                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AppNavigation()
                }

            }

        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "notes"
    ) {

        composable("notes") { backStackEntry ->
            val noteDeleted = backStackEntry.savedStateHandle
                .getStateFlow("noteDeleted", false)
                .collectAsStateWithLifecycle()
            val deletedTitle = backStackEntry.savedStateHandle
                .getStateFlow("deletedTitle", "")
                .collectAsStateWithLifecycle()
            val deletedDescription = backStackEntry.savedStateHandle
                .getStateFlow("deletedDescription", "")
                .collectAsStateWithLifecycle()

            NotesScreen(
                onNoteClick = { note -> navController.navigate("detail/${note.id}") },
                onAddNote = { navController.navigate("detail") },
                noteDeleted = noteDeleted.value,
                onNoteDeletedConsumed = {
                    backStackEntry.savedStateHandle["noteDeleted"] = false
                },
                onRestoreNote = {
                    backStackEntry.savedStateHandle["noteDeleted"] = false
                },
                deletedTitle = deletedTitle.value,
                deletedDescription = deletedDescription.value
            )
        }

        composable("detail") {
            NoteDetailScreen(
                noteId = null,
                onBack = { navController.popBackStack() },
                onNoteDeleted = { _, _ -> navController.popBackStack() }
            )
        }

        composable("detail/{noteId}") { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")
            NoteDetailScreen(
                noteId = noteId,
                onBack = { navController.popBackStack() },
                onNoteDeleted = { title, description ->
                    navController.previousBackStackEntry?.savedStateHandle?.set("noteDeleted", true)
                    navController.previousBackStackEntry?.savedStateHandle?.set("deletedTitle", title)
                    navController.previousBackStackEntry?.savedStateHandle?.set("deletedDescription", description)
                    navController.popBackStack()
                }
            )
        }
    }
}

