package com.mauro.mynoteapp.notes

import com.mauro.mynoteapp.domain.model.Note

sealed interface NotesUiState {

    object Loading : NotesUiState

    data class Success(val data: List<Note>) : NotesUiState

    data class Error(val message: String) : NotesUiState

    object Empty : NotesUiState
}
