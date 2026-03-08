package com.example.mynoteapp.presentation.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynoteapp.domain.AppResult
import com.example.mynoteapp.domain.usecase.GetAllNotes
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


@HiltViewModel
class NotesViewModel @Inject constructor (
    private val getAllNotes : GetAllNotes
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

}