package com.example.notes.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable

@Entity(tableName = "note_table")
class Note(@PrimaryKey @ColumnInfo(name = "id") val id: String,
           @ColumnInfo(name = "note_name") val noteName: String,
           @ColumnInfo(name = "created_at") var createdAt: Long,
           @ColumnInfo(name = "note_content") var noteContent: String) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readString()
    )

    fun setUpdatedContent(createdAt: Long, noteContent: String) {
        this.createdAt = createdAt
        this.noteContent = noteContent
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(noteName)
        parcel.writeLong(createdAt)
        parcel.writeString(noteContent)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Note> {
        override fun createFromParcel(parcel: Parcel): Note {
            return Note(parcel)
        }

        override fun newArray(size: Int): Array<Note?> {
            return arrayOfNulls(size)
        }
    }
}