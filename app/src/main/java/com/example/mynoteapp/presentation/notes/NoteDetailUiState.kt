package com.example.mynoteapp.presentation.notes

sealed interface NoteDetailUiState {
    data object Idle : NoteDetailUiState
    data object Loading : NoteDetailUiState
    data object Success : NoteDetailUiState
    data object Deleted : NoteDetailUiState
    data class Error(val error: String) : NoteDetailUiState
}
