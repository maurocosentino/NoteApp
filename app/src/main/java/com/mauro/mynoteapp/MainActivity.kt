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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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

        composable("notes") {

            NotesScreen(
                onNoteClick = { note ->
                    navController.navigate("detail/${note.id}")
                },
                onAddNote = {
                    navController.navigate("detail")
                }
            )

        }

        composable("detail") {
            NoteDetailScreen(
                noteId = null,
                onBack = { navController.popBackStack() }
            )
        }

        composable("detail/{noteId}") { backStackEntry ->

            val noteId = backStackEntry.arguments?.getString("noteId")

            NoteDetailScreen(
                noteId = noteId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

