package com.example.notes.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.example.notes.database.NotesRoomDatabase
import com.example.notes.models.Note
import com.example.notes.repository.NotesRepository
import java.lang.ref.WeakReference

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

    private class DatabaseWorker(repository: NotesRepository, private var note: Note, isUpdate: Boolean) : AsyncTask<Void, Void, Boolean>() {
        private var weakActivityReference: WeakReference<NotesRepository> = WeakReference(repository)
        private var isUpdateOperation = isUpdate

        override fun doInBackground(vararg params: Void?): Boolean {
            if (weakActivityReference.get() != null) {
                if (isUpdateOperation) {
                    weakActivityReference.get()?.update(note)
                } else {
                    weakActivityReference.get()?.insert(note)
                }
                return true
            }
            return false
        }

        override fun onPostExecute(result: Boolean?) {
            result?.let {
                Log.d("DatabaseWorker status ", it.toString())
            }
        }
    }


}