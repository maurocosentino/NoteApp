package com.mauro.mynoteapp.domain.usecase

import com.mauro.mynoteapp.domain.AppResult
import com.mauro.mynoteapp.domain.model.Note
import com.mauro.mynoteapp.domain.repository.INoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllNotes @Inject constructor(private val noteRepository: INoteRepository) {
    operator fun invoke(): Flow<AppResult<List<Note>>> = noteRepository.getAllNotes()
}