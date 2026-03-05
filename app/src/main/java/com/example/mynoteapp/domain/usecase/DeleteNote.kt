package com.example.mynoteapp.domain.usecase

import com.example.mynoteapp.domain.AppResult
import com.example.mynoteapp.domain.repository.INoteRepository
import jakarta.inject.Inject



class DeleteNote @Inject constructor(private val noteRepository: INoteRepository) {
    suspend operator fun invoke(id: String): AppResult<Unit> {
        if (id.isBlank()) return AppResult.Error("El id esta vacio o no existe")

        return noteRepository.deleteNote(id)

    }
}