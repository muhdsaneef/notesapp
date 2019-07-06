package com.example.notes.activities

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.TypedValue
import android.view.MenuItem
import android.widget.Toast
import com.example.notes.R
import com.example.notes.databinding.ActivityNotesEditBinding
import com.example.notes.models.Note
import kotlinx.android.synthetic.main.activity_notes_edit.*
import java.util.*

class NotesEditActivity : AppCompatActivity() {

    private var isEditMode = false
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
                //Update the note object if it is edit mode
                if (isEditMode) {
                    note?.setUpdatedContent(System.currentTimeMillis(), binding.edtNoteContent.text.toString())
                }
                val resultIntent = Intent()
                resultIntent.putExtra(NOTE_ITEM, note)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this,"Note content cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
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
            note?.let {
                binding.edtNoteContent.post {
                    binding.edtNoteContent.setSelection(it.noteContent.length)
                }
            }
        } else {
            val noteId = UUID.randomUUID().toString()
            note = Note(noteId, "", System.currentTimeMillis(), "")
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
    }
}
