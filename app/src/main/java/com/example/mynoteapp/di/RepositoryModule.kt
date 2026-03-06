package com.example.mynoteapp.di

import com.example.mynoteapp.data.repository_data.NoteRepositoryImpl
import com.example.mynoteapp.domain.repository.INoteRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindNoteRepository(
        noteServiceImpl: NoteRepositoryImpl
    ): INoteRepository

    companion object {
        @Provides
        @Singleton
        fun provideFirebase(): FirebaseFirestore {
            return FirebaseFirestore.getInstance() //
        }
    }
}

