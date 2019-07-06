package com.example.notes.repository

import com.example.notes.dao.NotesDao
import com.example.notes.models.Note

class NotesRepository(private val notesDao: NotesDao) {
    val allNotes = notesDao.getAllNotes()

    fun insert(note: Note) {
        notesDao.insert(note)
    }

    fun update(note: Note) {
        notesDao.update(note)
    }
}