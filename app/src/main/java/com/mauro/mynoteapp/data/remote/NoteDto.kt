package com.mauro.mynoteapp.data.remote

import com.google.firebase.Timestamp

data class NoteDto(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null,

    )
