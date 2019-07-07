package com.example.notes.activities

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.example.notes.R
import com.example.notes.adapters.NotesAdapter
import com.example.notes.databinding.ActivityMainBinding
import com.example.notes.models.Note
import com.example.notes.viewmodel.NoteViewModel

import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Toast
import android.content.DialogInterface
import android.app.AlertDialog
import android.view.LayoutInflater
import android.widget.EditText
import com.example.notes.utils.AppUtils

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: NoteViewModel
    private lateinit var adapter: NotesAdapter

    private var newNoteName :String? = null

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
        } else {
            intent.putExtra(NotesEditActivity.NEW_NOTE_NAME, newNoteName)
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
        binding.isNotesAvailable = listOfNotes != null && listOfNotes.isNotEmpty()
        listOfNotes?.let {
            notesList -> adapter.setNotes(notesList)
        }
    }

    private fun setAddNoteListener() {
        fab.setOnClickListener {
            isEditMode = false
            showEnterNoteNameDialog()
        }
    }

    private fun showEnterNoteNameDialog() {
        val alert = AlertDialog.Builder(this)
        val alertLayout = LayoutInflater.from(this).inflate(R.layout.enter_note_name_popup, null)
        val edtNoteName = alertLayout.findViewById<EditText>(R.id.edt_note_name)

        alert.setTitle(null)
        alert.setView(alertLayout)
        alert.setCancelable(false)
        alert.setNegativeButton(getString(R.string.label_cancel)) { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
        }


        alert.setPositiveButton(getString(R.string.label_ok), null)
        val dialog = alert.create()

        dialog.setOnShowListener { dialogInterface ->
            AppUtils.showSoftKeyboard(this, edtNoteName)
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                if (edtNoteName.text.toString().isNotEmpty()) {
                    newNoteName = edtNoteName.text.toString()
                    dialogInterface.dismiss()
                    showNoteEditScreen(null)
                } else {
                    Toast.makeText(this, R.string.message_please_enter_valid_name, Toast.LENGTH_SHORT).show()
                }
            }
        }
        dialog.show()
    }

    companion object {
        private const val REQUEST_EDIT_SCREEN = 9001
    }
}
