package com.example.mynoteapp.domain.repository

import com.example.mynoteapp.domain.AppResult
import com.example.mynoteapp.domain.model.Note
import kotlinx.coroutines.flow.Flow
interface INoteRepository {
    fun getAllNotes(): Flow<AppResult<List<Note>>>
    suspend fun createNote(note: Note): AppResult<Unit>
    suspend fun updateNote(note: Note): AppResult<Unit>
    suspend fun deleteNote(noteId: String): AppResult<Unit>
}
