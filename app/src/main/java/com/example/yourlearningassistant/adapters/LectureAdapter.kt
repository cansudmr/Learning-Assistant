package com.example.yourlearningassistant.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.yourlearningassistant.data.Lecture
import com.example.yourlearningassistant.databinding.ItemLectureBinding

class LectureAdapter(
    private val onLectureClick: (Lecture) -> Unit,
    private val onDeleteClick: (Lecture) -> Unit
) : ListAdapter<Lecture, LectureAdapter.LectureViewHolder>(LectureDiffCallback()) {

    class LectureViewHolder(private val binding: ItemLectureBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(lecture: Lecture, onLectureClick: (Lecture) -> Unit, onDeleteClick: (Lecture) -> Unit) {
            binding.lectureName.text = lecture.lectureName
            binding.lectureDetails.text = lecture.lectureDetails
            binding.root.setOnClickListener { onLectureClick(lecture) }
            binding.deleteButton.setOnClickListener { onDeleteClick(lecture) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LectureViewHolder {
        val binding = ItemLectureBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return LectureViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LectureViewHolder, position: Int) {
        val lecture = getItem(position)
        holder.bind(lecture, onLectureClick, onDeleteClick)
    }
}

class LectureDiffCallback : DiffUtil.ItemCallback<Lecture>() {
    override fun areItemsTheSame(oldItem: Lecture, newItem: Lecture) =
        oldItem.lectureId == newItem.lectureId
    override fun areContentsTheSame(oldItem: Lecture, newItem: Lecture) =
        oldItem == newItem
}