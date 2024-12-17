package com.example.yourlearningassistant.ui

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.yourlearningassistant.LectureViewModel
import com.example.yourlearningassistant.databinding.DialogCreateLectureBinding

class CreateLectureDialog : DialogFragment() {
    private val viewModel: LectureViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { activity ->
            val binding = DialogCreateLectureBinding.inflate(layoutInflater)

            AlertDialog.Builder(activity)
                .setTitle("Create New Lecture")
                .setView(binding.root)
                .setPositiveButton("Done") { _, _ ->
                    val name = binding.lectureNameInput.text.toString()
                    val details = binding.lectureDetailsInput.text.toString()
                    if (name.isNotEmpty()) {
                        viewModel.insertLecture(name, details)
                    } else {
                        Toast.makeText(context, "Lecture name is required", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}