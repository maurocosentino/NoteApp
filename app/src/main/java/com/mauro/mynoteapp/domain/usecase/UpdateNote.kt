package com.mauro.mynoteapp.domain.usecase

import com.mauro.mynoteapp.domain.AppResult
import com.mauro.mynoteapp.domain.model.Note
import com.mauro.mynoteapp.domain.repository.INoteRepository
import java.lang.System
import javax.inject.Inject


class UpdateNote @Inject constructor(
    private val noteRepository: INoteRepository
) {
    suspend operator fun invoke(note: Note): AppResult<Unit> {

        if (note.title.isBlank() || note.description.isBlank()) {
            return AppResult.Error("Note title and content can't be empty")
        }

        return noteRepository.updateNote(
            note.copy(updatedAt = System.currentTimeMillis())
        )
    }
}