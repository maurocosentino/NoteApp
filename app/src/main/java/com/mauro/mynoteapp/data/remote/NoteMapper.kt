package com.mauro.mynoteapp.data.remote

import com.mauro.mynoteapp.domain.model.Note
import com.google.firebase.Timestamp


fun NoteDto.toNote(): Note {
   return Note(
       id = this.id,
       title = this.title,
       description = this.description,
       createdAt = this.createdAt?.let { it.seconds * 1000 + it.nanoseconds / 1000000 } ?: 0L,
       updatedAt = this.updatedAt?.let { it.seconds * 1000 + it.nanoseconds / 1000000 } ?: 0L
   )
}

fun Note.toNoteDto(): NoteDto{
    return NoteDto(
        id = this.id,
        title = this.title,
        description = this.description,
        createdAt = Timestamp(this.createdAt / 1000, ((this.createdAt % 1000) * 1000000).toInt()),
        updatedAt = Timestamp(this.updatedAt / 1000, ((this.updatedAt % 1000) * 1000000).toInt())
    )
}
