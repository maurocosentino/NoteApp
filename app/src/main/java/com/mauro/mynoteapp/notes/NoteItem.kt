package com.mauro.mynoteapp.notes

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mauro.mynoteapp.domain.model.Note
import com.mauro.mynoteapp.utils.toFormattedDateStringModern

@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun NoteItem(note: Note, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Column() {
            Text(text = note.title, style = MaterialTheme.typography.titleMedium)
            Text(text = note.description, style = MaterialTheme.typography.bodyMedium)
            Text(text = note.createdAt.toFormattedDateStringModern("dd MMM yyyy"), style = MaterialTheme.typography.bodyMedium)
        }


    }
}