package com.example.notes.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import com.example.notes.database.NotesRoomDatabase
import com.example.notes.models.Note
import com.example.notes.repository.NotesRepository
import com.example.notes.worker.DatabaseWorker

class NoteViewModel : ViewModel() {

    private lateinit var repository: NotesRepository
    lateinit var allWords: LiveData<List<Note>>

    fun init(context: Context) {
        val wordsDao = NotesRoomDatabase.getDatabase(context).noteDao()
        repository = NotesRepository(wordsDao)
        allWords = repository.allNotes
    }

    fun executeDatabaseOperation(note: Note, isUpdate: Boolean) {
        val databaseOperation = DatabaseWorker(repository, note, isUpdate)
        databaseOperation.execute()
    }


}