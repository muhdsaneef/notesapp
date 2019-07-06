package com.example.notes.adapters

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.notes.databinding.ItemNoteBinding
import com.example.notes.models.Note
import com.example.notes.utils.AppUtils

class NotesAdapter: RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    private var notesList = emptyList<Note>()
    var itemClickListener: ((note: Note) -> Unit)? = null

    override fun getItemCount(): Int {
        return notesList.size
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val itemNoteBinding = ItemNoteBinding.inflate(
            LayoutInflater.from(viewGroup.context),  viewGroup, false)
        return ViewHolder(itemNoteBinding)
    }

    override fun onBindViewHolder(viweHolder: ViewHolder, position: Int) {
        viweHolder.bind(notesList[position])
    }

    fun setNotes(notes: List<Note>) {
        notesList = notes
        notifyDataSetChanged()
    }


    inner class ViewHolder(itemNoteBinding: ItemNoteBinding) : RecyclerView.ViewHolder(itemNoteBinding.root) {

        private val binding = itemNoteBinding

        init {
            setItemClick(binding.root)
        }

        private fun setItemClick(view: View) {
            view.setOnClickListener {
                itemClickListener?.invoke(notesList[layoutPosition])
            }
        }

        fun bind(note: Note) {
            binding.noteObj = note
            binding.executePendingBindings()
        }

    }

    companion object {
        @BindingAdapter("createdAt")
        @JvmStatic
        fun setFormattedDate(textView: TextView, timeStamp: Long) {
            textView.text = AppUtils.getNotesCreatedFormattedDate(timeStamp)
        }
    }
}