package com.example.yourlearningassistant.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import com.example.yourlearningassistant.LectureViewModel
import com.example.yourlearningassistant.R
import com.example.yourlearningassistant.adapters.NoteAdapter
import com.example.yourlearningassistant.databinding.DialogAddNoteBinding
import com.example.yourlearningassistant.databinding.FragmentNotesBinding

class NotesFragment : Fragment(R.layout.fragment_notes) {
    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LectureViewModel by activityViewModels()
    private val args: NotesFragmentArgs by navArgs()
    private lateinit var notesAdapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupAddNoteButton()
        observeNotes()
    }

    private fun setupRecyclerView() {
        notesAdapter = NoteAdapter(
            onNoteClick = { note ->
                findNavController().navigate(
                    NotesFragmentDirections.actionNotesFragmentToNoteDetailsFragment(note.noteId)
                )
            },
            onDeleteClick = { note ->
                viewModel.deleteNote(note)
            }
        )

        binding.notesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = notesAdapter
        }
    }

    private fun setupAddNoteButton() {
        binding.addNoteButton.setOnClickListener {
            showAddNoteDialog()
        }
    }

    private fun showAddNoteDialog() {
        val dialogBinding = DialogAddNoteBinding.inflate(layoutInflater)

        AlertDialog.Builder(requireContext())
            .setTitle("Add New Note")
            .setView(dialogBinding.root)
            .setPositiveButton("Add") { _, _ ->
                val title = dialogBinding.noteTitleInput.text.toString()
                val content = dialogBinding.noteContentInput.text.toString()
                if (content.isNotEmpty()) {
                    viewModel.insertNote(args.lectureId, title, content)
                    binding.notesRecyclerView.scrollToPosition(0)
                } else {
                    Toast.makeText(context, "Note content is required", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun observeNotes() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getNotesForLecture(args.lectureId).collect { notes ->
                notesAdapter.submitList(notes.sortedByDescending { it.timestamp }) {
                    binding.notesRecyclerView.scrollToPosition(0)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
