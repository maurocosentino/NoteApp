package com.mauro.mynoteapp.notes

data class NoteDetailState(
    val title: String = "",
    val description: String = "",
    val isExistingNote: Boolean = false,
    val isSaved: Boolean = false
)