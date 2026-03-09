package com.mauro.mynoteapp.notes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    noteId: String?,
    onBack: () -> Unit
) {

    val viewModel: NoteDetailViewModel = hiltViewModel()

    val state = viewModel.state

    LaunchedEffect(noteId) {

        noteId?.let {
            viewModel.loadNote(it)
        }

    }

    LaunchedEffect(state.isSaved) {

        if (state.isSaved) {
            onBack()
        }

    }

    Scaffold(

        topBar = {

            TopAppBar(

                title = { Text("Nota") },

                navigationIcon = {

                    IconButton(onClick = onBack) {

                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )

                    }

                },

                actions = {

                    if (state.isExistingNote) {

                        IconButton(
                            onClick = {
                                viewModel.onDeleteNote()
                            }
                        ) {

                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar"
                            )

                        }

                    }

                    IconButton(
                        onClick = {
                            viewModel.onSaveNote()
                        }
                    ) {

                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Guardar"
                        )

                    }

                }

            )

        }

    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.title,

                onValueChange = {
                    viewModel.onTitleChange(it)
                },

                label = {
                    Text("Título")
                }

            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.description,

                onValueChange = {
                    viewModel.onDescriptionChange(it)
                },

                label = {
                    Text("Descripción")
                }

            )

        }

    }

}