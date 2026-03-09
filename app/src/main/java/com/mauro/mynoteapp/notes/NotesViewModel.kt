package com.mauro.mynoteapp.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mauro.mynoteapp.domain.AppResult
import com.mauro.mynoteapp.domain.usecase.CreateNote
import com.mauro.mynoteapp.domain.usecase.GetAllNotes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NotesViewModel @Inject constructor (
    private val getAllNotes : GetAllNotes,
    private val createNote: CreateNote
): ViewModel(){
    private val _uiState = MutableStateFlow<NotesUiState>(NotesUiState.Loading)
    val uiState: StateFlow<NotesUiState> = _uiState.asStateFlow()
    init {
        loadNotes()
    }
    private fun loadNotes() {
        viewModelScope.launch {
            getAllNotes().collect {
                result -> when(result){
                is AppResult.Success -> if (result.data.isEmpty()){
                    _uiState.value = NotesUiState.Empty
                }else{
                    _uiState.value = NotesUiState.Success(result.data)
                }
                is AppResult.Error -> _uiState.value = NotesUiState.Error(result.message)
                }
            }
        }
    }
    fun restoreNote(title: String, description: String) {
        viewModelScope.launch {
            createNote(title, description)
        }
    }

}