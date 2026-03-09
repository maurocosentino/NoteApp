package com.mauro.mynoteapp.notes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mauro.mynoteapp.domain.AppResult
import com.mauro.mynoteapp.domain.model.Note
import com.mauro.mynoteapp.domain.usecase.CreateNote
import com.mauro.mynoteapp.domain.usecase.DeleteNote
import com.mauro.mynoteapp.domain.usecase.GetNoteById
import com.mauro.mynoteapp.domain.usecase.UpdateNote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val getNoteById: GetNoteById,
    private val createNote: CreateNote,
    private val updateNote: UpdateNote,
    private val deleteNote: DeleteNote

) : ViewModel() {

    var state by mutableStateOf(NoteDetailState())
        private set

    private var currentNoteId: String? = null

    fun loadNote(noteId: String) {

        viewModelScope.launch {

            when (val result = getNoteById(noteId)) {

                is AppResult.Success -> {

                    val note = result.data

                    currentNoteId = note.id

                    state = state.copy(
                        title = note.title,
                        description = note.description,
                        isExistingNote = true
                    )
                }

                else -> {}
            }
        }
    }

    fun onTitleChange(value: String) {
        state = state.copy(title = value)
    }

    fun onDescriptionChange(value: String) {
        state = state.copy(description = value)
    }

    fun onSaveNote() {

        viewModelScope.launch {

            val note = Note(
                id = currentNoteId ?: "",
                title = state.title,
                description = state.description
            )

            val result =
                if (state.isExistingNote) {
                    updateNote(note)
                } else {
                    createNote(state.title, state.description)
                }

            if (result is AppResult.Success) {
                state = state.copy(isSaved = true)
            }

        }

    }

    fun onDeleteNote() {

        currentNoteId?.let { id ->

            viewModelScope.launch {

                deleteNote(id)

                state = state.copy(isSaved = true)

            }

        }

    }

}
