package com.example.notes.activities

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.example.notes.R
import com.example.notes.databinding.ActivityNotesEditBinding
import com.example.notes.models.Note
import com.example.notes.utils.AppUtils
import kotlinx.android.synthetic.main.activity_notes_edit.*
import java.util.*

class NotesEditActivity : BaseActivity() {

    private var note: Note? = null
    private lateinit var binding: ActivityNotesEditBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notes_edit)

        fetchEditModeValue()
        initToolbar()
        initNoteEditScreen()
        setDoneButtonListener()
    }

    private fun setDoneButtonListener() {
        binding.imgDone.setOnClickListener {
            if (binding.edtNoteContent.text.toString().isNotEmpty()) {
                //Update the note content
                checkIfContentIsUpdatedInEditMode(binding.edtNoteContent.text.toString())
            } else {
                Toast.makeText(this,"Note content cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkIfContentIsUpdatedInEditMode(noteContent: String) {
        if (isEditMode && noteContent == note?.noteContent) {
            finish()
        } else {
            note?.noteContent = noteContent
            note?.createdAt = System.currentTimeMillis()
            sendResultBackToListingScreen()
        }
    }

    private fun sendResultBackToListingScreen() {
        //Check if content is changed in edit mode, then update the current note
        val resultIntent = Intent()
        resultIntent.putExtra(NOTE_ITEM, note)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val title = if (isEditMode) R.string.action_edit else R.string.action_new
        binding.toolbarTitle.text = getString(title)

        try {
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        } catch (e: Exception) {
            Log.e(TAG, "Expecting in low end devices because of vectors")
        }

    }

    private fun initNoteEditScreen() {
        if (isEditMode) {
            note = intent.getParcelableExtra(NOTE_ITEM)
            binding.noteObj = note
        } else {
            val noteId = UUID.randomUUID().toString()
            val noteName = intent.getStringExtra(NEW_NOTE_NAME)
            note = Note(noteId, noteName, System.currentTimeMillis(), "")
            AppUtils.showSoftKeyboard(this, binding.edtNoteContent)
        }
    }

    private fun fetchEditModeValue() {
        isEditMode = intent.getBooleanExtra(IS_EDIT_MODE, false)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            if (it.itemId == android.R.id.home) {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private var TAG = NotesEditActivity::class.java.simpleName
        const val IS_EDIT_MODE = "edit mode"
        const val NOTE_ITEM = "note item"
        const val NEW_NOTE_NAME = "new note name"
    }
}
