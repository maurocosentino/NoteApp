package com.mauro.mynoteapp.domain.usecase

import com.mauro.mynoteapp.domain.AppResult
import com.mauro.mynoteapp.domain.model.Note
import com.mauro.mynoteapp.domain.repository.INoteRepository
import javax.inject.Inject

class CreateNote @Inject constructor(private val noteRepository: INoteRepository)
{
    suspend operator fun invoke(title: String, description: String) : AppResult<Unit>
    {
        if (title.isBlank() || description.isBlank() ) {
            return AppResult.Error("Note title and content be empty")
        }

        val note = Note(title = title, description = description)
        return noteRepository.createNote(note)
    }
}