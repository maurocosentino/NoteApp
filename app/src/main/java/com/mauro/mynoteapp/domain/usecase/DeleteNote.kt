package com.mauro.mynoteapp.domain.usecase

import com.mauro.mynoteapp.domain.AppResult
import com.mauro.mynoteapp.domain.repository.INoteRepository
import javax.inject.Inject


class DeleteNote @Inject constructor(private val noteRepository: INoteRepository) {
    suspend operator fun invoke(id: String): AppResult<Unit> {
        if (id.isBlank()) return AppResult.Error("El id esta vacio o no existe")

        return noteRepository.deleteNote(id)

    }
}