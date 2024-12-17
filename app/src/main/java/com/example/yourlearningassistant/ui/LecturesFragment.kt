package com.example.yourlearningassistant.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yourlearningassistant.LectureViewModel
import com.example.yourlearningassistant.R
import kotlinx.coroutines.launch
import com.example.yourlearningassistant.adapters.LectureAdapter
import com.example.yourlearningassistant.data.Lecture
import com.example.yourlearningassistant.databinding.FragmentLecturesBinding

class LecturesFragment : Fragment(R.layout.fragment_lectures) {
    private var _binding: FragmentLecturesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LectureViewModel by activityViewModels()
    private lateinit var adapter: LectureAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLecturesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = LectureAdapter(
            onLectureClick = { lecture ->
                findNavController().navigate(
                    LecturesFragmentDirections.actionLecturesFragmentToNotesFragment(lecture.lectureId)
                )
            },
            onDeleteClick = { lecture ->
                showDeleteConfirmationDialog(lecture)
            }
        )

        binding.lecturesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@LecturesFragment.adapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allLectures.collect { lectures ->
                adapter.submitList(lectures.sortedByDescending { it.timestamp }) {
                    // Scroll to the top immediately after the list is updated
                    binding.lecturesRecyclerView.scrollToPosition(0)
                }
            }
        }
    }

    private fun showDeleteConfirmationDialog(lecture: Lecture) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Lecture")
            .setMessage("Are you sure you want to delete this lecture? All associated notes will be deleted.")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteLecture(lecture)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}