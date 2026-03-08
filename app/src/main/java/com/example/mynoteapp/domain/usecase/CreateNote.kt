package com.example.mynoteapp.domain.usecase

import com.example.mynoteapp.domain.AppResult
import com.example.mynoteapp.domain.model.Note
import com.example.mynoteapp.domain.repository.INoteRepository
import jakarta.inject.Inject

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