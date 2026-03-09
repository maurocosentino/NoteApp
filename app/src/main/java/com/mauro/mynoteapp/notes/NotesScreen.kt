package com.mauro.mynoteapp.notes

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mauro.mynoteapp.domain.model.Note
import androidx.hilt.navigation.compose.hiltViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotesScreen(
    onNoteClick: (Note) -> Unit,
    onAddNote: () -> Unit,

    ) {

    val viewModel: NotesViewModel = hiltViewModel()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(

        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddNote,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar nota"
                )
            }
        }

    ) { padding ->
    Box(modifier = Modifier.fillMaxSize().padding(padding)){
        when (uiState) {

            is NotesUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is NotesUiState.Empty -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No tienes notas")
                }
            }

            is NotesUiState.Success -> {

                val data = (uiState as NotesUiState.Success).data

                LazyColumn(modifier = Modifier.fillMaxSize()) {

                    items(data) { note ->

                        NoteItem(
                            note = note,
                            onClick = { onNoteClick(note) }
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
                    Text(message)
                }

            }
        }
    }

    }
}