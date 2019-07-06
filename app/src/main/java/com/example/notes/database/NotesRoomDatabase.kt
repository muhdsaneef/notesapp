package com.example.notes.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.example.notes.dao.NotesDao
import com.example.notes.models.Note

@Database(entities = [Note::class], version = 1)
public abstract class NotesRoomDatabase : RoomDatabase() {
    abstract fun noteDao(): NotesDao

    companion object {
        @Volatile
        private var INSTANCE: NotesRoomDatabase? = null

        fun getDatabase(context: Context): NotesRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotesRoomDatabase::class.java,
                    "Notes_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}