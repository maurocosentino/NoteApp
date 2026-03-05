package com.example.mynoteapp.domain.usecase

import com.example.mynoteapp.domain.AppResult
import com.example.mynoteapp.domain.model.Note
import com.example.mynoteapp.domain.repository.INoteRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetAllNotes @Inject constructor(private val noteRepository: INoteRepository) {
    operator fun invoke(): Flow<AppResult<List<Note>>> = noteRepository.getAllNotes()
}