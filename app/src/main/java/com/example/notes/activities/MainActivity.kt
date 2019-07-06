package com.example.notes.activities

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import com.example.notes.R
import com.example.notes.adapters.NotesAdapter
import com.example.notes.databinding.ActivityMainBinding
import com.example.notes.models.Note
import com.example.notes.viewmodel.NoteViewModel

import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: NoteViewModel
    private lateinit var adapter: NotesAdapter
    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(NoteViewModel::class.java)
        viewModel.init(this)


        initNotesAdapter()

        setAddNoteListener()

        setObserverForDatabaseChanges()
    }

    private fun initNotesAdapter() {
        adapter = NotesAdapter()
        binding.rvNotes.adapter = adapter
        adapter.itemClickListener = {
            handleNoteClick(it)
        }
    }

    private fun handleNoteClick(note: Note) {
        isEditMode = true
        showNoteEditScreen(note)
    }

    private fun showNoteEditScreen(note: Note?) {
        val intent = Intent(this, NotesEditActivity::class.java)
        intent.putExtra(NotesEditActivity.IS_EDIT_MODE, isEditMode)
        if (isEditMode) {
            intent.putExtra(NotesEditActivity.NOTE_ITEM, note)
        }
        startActivityForResult(intent, REQUEST_EDIT_SCREEN)
    }

    private fun setObserverForDatabaseChanges() {
        viewModel.allWords.observe(this, Observer {
            listOfNotes -> updateNotes(listOfNotes)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_EDIT_SCREEN && resultCode == Activity.RESULT_OK) {
            handleEditAction(data)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleEditAction(data: Intent?) {
        data?.let {
            val note = it.getParcelableExtra<Note>(NotesEditActivity.NOTE_ITEM)
            viewModel.executeDatabaseOperation(note, isEditMode)
        }
    }

    private fun updateNotes(listOfNotes: List<Note>?) {
        listOfNotes?.let {
            notesList -> adapter.setNotes(notesList)
        }
    }

    private fun setAddNoteListener() {
        fab.setOnClickListener {
            isEditMode = false
            showNoteEditScreen(null)
        }
    }

    companion object {
        private const val REQUEST_EDIT_SCREEN = 9001
    }
}
