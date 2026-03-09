package com.mauro.mynoteapp.domain.usecase

import com.mauro.mynoteapp.domain.AppResult
import com.mauro.mynoteapp.domain.repository.INoteRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class DeleteNoteTest {

    private lateinit var repository: INoteRepository
    private lateinit var deleteNote: DeleteNote

    @Before
    fun setup() {
        repository = mock()
        deleteNote = DeleteNote(repository)
    }

    @Test
    fun `when id is blank, returns error without calling repository`() = runTest {
        val result = deleteNote(id = "")

        assertTrue(result is AppResult.Error)
        verify(repository, never()).deleteNote(any())
    }

    @Test
    fun `when id is valid, calls repository and returns success`() = runTest {
        val validId = "abc-123"
        whenever(repository.deleteNote(validId)).thenReturn(AppResult.Success(Unit))

        val result = deleteNote(id = validId)

        assertTrue(result is AppResult.Success)
        verify(repository).deleteNote(validId)
    }
}