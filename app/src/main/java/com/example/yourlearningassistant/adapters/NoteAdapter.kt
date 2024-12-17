package com.example.yourlearningassistant.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.yourlearningassistant.data.Note
import com.example.yourlearningassistant.databinding.ItemNoteBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NoteAdapter(
    private val onNoteClick: (Note) -> Unit,
    private val onDeleteClick: (Note) -> Unit
) : ListAdapter<Note, NoteAdapter.NoteViewHolder>(NoteDiffCallback()) {

    class NoteViewHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            note: Note,
            onNoteClick: (Note) -> Unit,
            onDeleteClick: (Note) -> Unit
        ) {
            binding.noteTitle.text = note.noteTitle
            binding.noteContent.text = note.noteContent
            binding.noteTimestamp.text = formatDate(note.timestamp)
            binding.root.setOnClickListener { onNoteClick(note) }
            binding.deleteButton.setOnClickListener { onDeleteClick(note) }
        }

        private fun formatDate(timestamp: Long): String {
            return SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
                .format(Date(timestamp))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(getItem(position), onNoteClick, onDeleteClick)
    }
}

class NoteDiffCallback : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note) =
        oldItem.noteId == newItem.noteId

    override fun areContentsTheSame(oldItem: Note, newItem: Note) =
        oldItem == newItem
}
