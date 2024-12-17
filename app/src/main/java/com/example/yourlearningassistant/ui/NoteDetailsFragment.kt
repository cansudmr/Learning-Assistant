package com.example.yourlearningassistant.ui

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.yourlearningassistant.LectureViewModel
import com.example.yourlearningassistant.R
import com.example.yourlearningassistant.data.Note
import com.example.yourlearningassistant.databinding.FragmentNoteDetailsBinding
import kotlinx.coroutines.launch

class NoteDetailsFragment : Fragment(R.layout.fragment_note_details) {
    private var _binding: FragmentNoteDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LectureViewModel by activityViewModels()
    private val args: NoteDetailsFragmentArgs by navArgs()
    private var currentNote: Note? = null
    private var loadingDialog: ProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe the specific note
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getNoteById(args.noteId).collect { note ->
                currentNote = note
                binding.noteTitleEdit.setText(note.noteTitle)
                binding.noteContentEdit.setText(note.noteContent)
            }
        }

        // Save button click listener
        binding.saveNoteButton.setOnClickListener {
            currentNote?.let { note ->
                val updatedTitle = binding.noteTitleEdit.text.toString()
                val updatedContent = binding.noteContentEdit.text.toString()

                if (updatedContent.isNotEmpty()) {
                    viewModel.updateNote(
                        note.copy(
                            noteTitle = updatedTitle,
                            noteContent = updatedContent
                        )
                    )
                    findNavController().navigateUp()
                } else {
                    Toast.makeText(context, "Note content is required", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.summarizeButton.setOnClickListener {
            val content = binding.noteContentEdit.text.toString()
            if (content.isNotEmpty()) {
                showLoadingDialog()
                viewModel.summarizeNote(content) { summary ->
                    // Ensure this runs on the main thread
                    requireActivity().runOnUiThread {
                        dismissLoadingDialog()
                        showSummaryDialog(summary)
                    }
                }
            } else {
                Toast.makeText(context, "Note content is empty", Toast.LENGTH_SHORT).show()
            }
        }

        binding.generateStudyDetailsButton.setOnClickListener {
            val content = binding.noteContentEdit.text.toString()
            if (content.isNotEmpty()) {
                showLoadingDialog()
                viewModel.generateStudyDetails(content) { studyDetails ->
                    // Ensure this runs on the main thread
                    requireActivity().runOnUiThread {
                        dismissLoadingDialog()
                        showStudyDetailsDialog(studyDetails)
                    }
                }
            } else {
                Toast.makeText(context, "Note content is empty", Toast.LENGTH_SHORT).show()
            }
        }




    }

    private fun showStudyDetailsDialog(studyDetails: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Comprehensive Study Guide")
            .setMessage(studyDetails)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showLoadingDialog() {
        loadingDialog = ProgressDialog(requireContext()).apply {
            setMessage("Generating...")
            setCancelable(false)
            show()
        }
    }

    private fun dismissLoadingDialog() {
        loadingDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
            loadingDialog = null
        }
    }

    private fun showSummaryDialog(summary: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Note Summary")
            .setMessage(summary)
            .setPositiveButton("OK", null)
            .show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}