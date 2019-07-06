package com.example.notes.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.example.notes.models.Note

@Dao
interface NotesDao {

    @Query("SELECT * from note_table ORDER BY created_at DESC")
    fun getAllNotes(): LiveData<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: Note)

    @Update
    fun update(note: Note)
}