package com.mauro.mynoteapp.domain.usecase

import com.mauro.mynoteapp.domain.AppResult
import com.mauro.mynoteapp.domain.repository.INoteRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class CreateNoteTest {

    private lateinit var repository: INoteRepository
    private lateinit var createNote: CreateNote

    @Before
    fun setup() {
        repository = mock()
        createNote = CreateNote(repository)
    }

    @Test
    fun `when title is blank, returns error without calling repository`() = runTest {
        val result = createNote(title = "", description = "Descripción")

        assertTrue(result is AppResult.Error)
        verify(repository, never()).createNote(any())
    }

    @Test
    fun `when description is blank, returns error without calling repository`() = runTest {
        val result = createNote(title = "Título", description = "")

        assertTrue(result is AppResult.Error)
        verify(repository, never()).createNote(any())
    }

    @Test
    fun `when title and description are valid, calls repository and returns success`() = runTest {
        whenever(repository.createNote(any())).thenReturn(AppResult.Success(Unit))

        val result = createNote(title = "Título", description = "Descripción")

        assertTrue(result is AppResult.Success)
        verify(repository).createNote(any())
    }
}