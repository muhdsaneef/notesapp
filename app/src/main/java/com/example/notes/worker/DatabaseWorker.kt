package com.example.notes.worker

import android.os.AsyncTask
import android.util.Log
import com.example.notes.models.Note
import com.example.notes.repository.NotesRepository
import java.lang.ref.WeakReference

class DatabaseWorker(repository: NotesRepository, private var note: Note, isUpdate: Boolean) : AsyncTask<Void, Void, Boolean>() {
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