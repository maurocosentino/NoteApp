package com.example.mynoteapp.presentation.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynoteapp.domain.AppResult
import com.example.mynoteapp.domain.model.Note
import com.example.mynoteapp.domain.usecase.CreateNote
import com.example.mynoteapp.domain.usecase.DeleteNote
import com.example.mynoteapp.domain.usecase.UpdateNote
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val saveNote : CreateNote,
    private val updateNote : UpdateNote,
    private val deleteNote : DeleteNote

): ViewModel() {
    private val _noteDetailUiState = MutableStateFlow<NoteDetailUiState>(NoteDetailUiState.Idle)
    val detailState: StateFlow<NoteDetailUiState> = _noteDetailUiState.asStateFlow()
    private var recentlyDeletedNote: Note? = null

     fun onSaveNote(title: String, description: String) {
        viewModelScope.launch {
            _noteDetailUiState.value = NoteDetailUiState.Loading
            val result = saveNote(title, description)
            when(result){
                is AppResult.Success -> {
                    _noteDetailUiState.value = NoteDetailUiState.Success
                }
                is AppResult.Error -> {
                    _noteDetailUiState.value = NoteDetailUiState.Error(result.message)
                }
            }
        }

    }
    fun onUpdateNote(note: Note) {
        viewModelScope.launch {
            _noteDetailUiState.value = NoteDetailUiState.Loading

            val result = updateNote(note)

            when (result) {
                is AppResult.Success -> {
                    _noteDetailUiState.value = NoteDetailUiState.Success
                }
                is AppResult.Error -> {
                    _noteDetailUiState.value =
                        NoteDetailUiState.Error(result.message)
                }
            }
        }
    }

    fun onDeleteNote(note: Note) {
        viewModelScope.launch {
            _noteDetailUiState.value = NoteDetailUiState.Loading

            recentlyDeletedNote = note

            val result = deleteNote(note.id)

            when (result) {
                is AppResult.Success -> {
                    _noteDetailUiState.value = NoteDetailUiState.Success
                }
                is AppResult.Error -> {
                    _noteDetailUiState.value =
                        NoteDetailUiState.Error(result.message)
                }
            }
        }
    }
    fun restoreDeletedNote() {
        val note = recentlyDeletedNote ?: return
        onSaveNote(note.title, note.description)
        recentlyDeletedNote = null
    }
}