package com.mauro.mynoteapp.domain.usecase

import com.mauro.mynoteapp.domain.AppResult
import com.mauro.mynoteapp.domain.model.Note
import com.mauro.mynoteapp.notes.NotesUiState
import com.mauro.mynoteapp.notes.NotesViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class NotesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var getAllNotes: GetAllNotes
    private lateinit var viewModel: NotesViewModel

    @Before
    fun setup() {
        getAllNotes = mock()
    }

    @Test
    fun `when notes exist, uiState is Success`() = runTest {
        val notes = listOf(Note(title = "Título", description = "Desc"))
        whenever(getAllNotes()).thenReturn(flowOf(AppResult.Success(notes)))

        viewModel = NotesViewModel(getAllNotes)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is NotesUiState.Success)
        assertEquals(notes, (viewModel.uiState.value as NotesUiState.Success).data)
    }

    @Test
    fun `when notes list is empty, uiState is Empty`() = runTest {
        whenever(getAllNotes()).thenReturn(flowOf(AppResult.Success(emptyList())))

        viewModel = NotesViewModel(getAllNotes)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is NotesUiState.Empty)
    }

    @Test
    fun `when repository returns error, uiState is Error`() = runTest {
        whenever(getAllNotes()).thenReturn(flowOf(AppResult.Error("Error de red")))

        viewModel = NotesViewModel(getAllNotes)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is NotesUiState.Error)
        assertEquals("Error de red", (viewModel.uiState.value as NotesUiState.Error).message)
    }
}