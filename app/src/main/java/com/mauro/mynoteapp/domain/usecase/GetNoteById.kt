package com.mauro.mynoteapp.domain.usecase

import com.mauro.mynoteapp.domain.AppResult
import com.mauro.mynoteapp.domain.model.Note
import com.mauro.mynoteapp.domain.repository.INoteRepository
import javax.inject.Inject

class GetNoteById @Inject constructor(
    private val repository: INoteRepository
) {

    suspend operator fun invoke(noteId: String): AppResult<Note> {

        if (noteId.isBlank()) {
            return AppResult.Error("El id de la nota no puede estar vacío")
        }

        return repository.getNoteById(noteId)
    }
}