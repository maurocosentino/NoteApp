package com.mauro.mynoteapp.domain.repository

import com.mauro.mynoteapp.domain.AppResult
import com.mauro.mynoteapp.domain.model.Note
import kotlinx.coroutines.flow.Flow
interface INoteRepository {
    fun getAllNotes(): Flow<AppResult<List<Note>>>

    suspend fun getNoteById(noteId: String): AppResult<Note>
    suspend fun createNote(note: Note): AppResult<Unit>
    suspend fun updateNote(note: Note): AppResult<Unit>
    suspend fun deleteNote(noteId: String): AppResult<Unit>
}
