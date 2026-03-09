package com.mauro.mynoteapp.notes

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mauro.mynoteapp.domain.model.Note

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotesScreen(
    onNoteClick: (Note) -> Unit,
    onAddNote: () -> Unit,
    noteDeleted: Boolean = false,
    onNoteDeletedConsumed: () -> Unit = {},
    onRestoreNote: () -> Unit = {},
    deletedTitle: String = "",
    deletedDescription: String = ""
) {
    val viewModel: NotesViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(noteDeleted) {
        if (noteDeleted) {
            val result = snackbarHostState.showSnackbar(
                message = "Nota eliminada",
                actionLabel = "Deshacer",
                duration = SnackbarDuration.Short
            )
            onNoteDeletedConsumed()
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.restoreNote(deletedTitle, deletedDescription)
            }
        }
    }

    var showSnackbarForListDelete by remember { mutableStateOf(false) }

    LaunchedEffect(showSnackbarForListDelete) {
        if (showSnackbarForListDelete) {
            val result = snackbarHostState.showSnackbar(
                message = "Nota eliminada",
                actionLabel = "Deshacer",
                duration = SnackbarDuration.Short
            )
            showSnackbarForListDelete = false
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.restoreNote(
                    viewModel.lastDeletedTitle,
                    viewModel.lastDeletedDescription
                )
            }
        }
    }
    var noteToDelete by remember { mutableStateOf<Note?>(null) }
    Scaffold(

        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mis Notas",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddNote,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Text(
                    text = "+",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background

    )

    { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (uiState) {
                is NotesUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                is NotesUiState.Empty -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "📝",
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 48.sp)
                        )
                        Text(
                            text = "No tenés notas todavía",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Tocá + para crear una",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                is NotesUiState.Success -> {
                    val data = (uiState as NotesUiState.Success).data
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(2),
                        contentPadding = PaddingValues(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalItemSpacing = 8.dp,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(data) { note ->
                            NoteItem(
                                note = note,
                                onClick = { onNoteClick(note) },
                                onLongClick = { noteToDelete = note }
                            )
                        }
                    }
                }
                is NotesUiState.Error -> {
                    val message = (uiState as NotesUiState.Error).message
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = message,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
        if (noteToDelete != null) {
            AlertDialog(
                onDismissRequest = { noteToDelete = null },
                title = { Text("Eliminar nota") },
                text = { Text("¿Querés eliminar \"${noteToDelete!!.title}\"?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            noteToDelete?.let {
                                viewModel.deleteNoteFromList(it.id, it.title, it.description)
                                showSnackbarForListDelete = true
                            }
                            noteToDelete = null
                        }
                    ) {
                        Text("Eliminar", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { noteToDelete = null }) {
                        Text("Cancelar")
                    }
                }
            )
        }

    }
}