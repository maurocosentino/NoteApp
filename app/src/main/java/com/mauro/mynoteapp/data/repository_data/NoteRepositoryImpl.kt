package com.mauro.mynoteapp.data.repository_data

import com.mauro.mynoteapp.data.remote.NoteDto
import com.mauro.mynoteapp.data.remote.toNote
import com.mauro.mynoteapp.data.remote.toNoteDto
import com.mauro.mynoteapp.domain.AppResult
import com.mauro.mynoteapp.domain.model.Note
import com.mauro.mynoteapp.domain.repository.INoteRepository
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObjects
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class NoteRepositoryImpl @Inject constructor (private val firestore: FirebaseFirestore):
    INoteRepository {

    override fun getAllNotes(): Flow<AppResult<List<Note>>> {
        return firestore
            .collection(NoteConstants.collectionPath)
            .snapshots()
            .map{ querySnapshot ->
              val list =  querySnapshot.toObjects<NoteDto>().map{it.toNote() }
                AppResult.Success(list) as AppResult<List<Note>>
            }.catch { exception ->
                // Catch the exception and emit a fallback value (or values)
                println("Caught exception: ${exception.message}")
                emit(
                    AppResult.Error("Hubo un problema al cargar tus notas. Comprueba tu conexión a internet o intenta de nuevo")
                )
            }

    }
    override suspend fun getNoteById(noteId: String): AppResult<Note> {
        return try {
            val snapshot = firestore
                .collection(NoteConstants.collectionPath)
                .document(noteId)
                .get()
                .await()

            val noteDto = snapshot.toObject(NoteDto::class.java)

            if (noteDto != null) {
                AppResult.Success(noteDto.toNote())
            } else {
                AppResult.Error("Nota no encontrada")
            }

        } catch (_: Exception) {
            AppResult.Error("Error al obtener la nota")
        }
    }

    override suspend fun createNote(note: Note): AppResult<Unit> {
        return try {
           val noteDto = note.toNoteDto()
            firestore.collection(NoteConstants.collectionPath).document(note.id).set(noteDto).await()
            AppResult.Success(Unit)
        } catch (_: Exception) {
            AppResult.Error("Error al crear la nota")
        }
    }

    override suspend fun updateNote(note: Note): AppResult<Unit> {
        return try {
            val noteDto = note.toNoteDto()
            firestore.collection(NoteConstants.collectionPath).document(note.id).set(noteDto).await()
            AppResult.Success(Unit)
        } catch (_: Exception) {
            AppResult.Error("Error al actualizar la nota")
        }
    }

    override suspend fun deleteNote(noteId: String): AppResult<Unit> {
        return try {
            firestore.collection(NoteConstants.collectionPath).document(noteId).delete().await()
            AppResult.Success(Unit)
        } catch (_: Exception) {
            AppResult.Error("Error al eliminar la nota")
        }
    }
}
