package com.example.yourlearningassistant

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import com.example.yourlearningassistant.data.*
import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LectureViewModel(application: Application) : AndroidViewModel(application) {
    // Database and Repository Setup
    private val database: AppDatabase = AppDatabase.getDatabase(application)

    // Retrofit and Gemini Service Setup
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://generativelanguage.googleapis.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val geminiService = retrofit.create(GeminiService::class.java)

    // Repository with Gemini Service
    private val repository: LectureRepository = LectureRepository(
        database.lectureDao(),
        database.noteDao(),
        geminiService
    )

    // Expose lectures to UI
    val allLectures: Flow<List<Lecture>> = repository.allLectures

    // Lecture-related methods
    fun insertLecture(lectureName: String, lectureDetails: String) = viewModelScope.launch {
        repository.insertLecture(Lecture(lectureName = lectureName, lectureDetails = lectureDetails))
    }

    fun deleteLecture(lecture: Lecture) = viewModelScope.launch {
        repository.deleteLecture(lecture)
    }

    // Notes-related methods
    fun getNotesForLecture(lectureId: Long) = repository.getNotesForLecture(lectureId)

    fun insertNote(lectureId: Long, title: String, content: String) = viewModelScope.launch {
        repository.insertNote(Note(lectureId = lectureId, noteTitle = title, noteContent = content))
    }

    fun deleteNote(note: Note) = viewModelScope.launch {
        repository.deleteNote(note)
    }

    fun updateNote(note: Note) = viewModelScope.launch {
        repository.updateNote(note)
    }

    fun getNoteById(noteId: Long) = repository.getNoteById(noteId)

    // Gemini AI Summarization Method
    fun summarizeNote(noteContent: String, onSummaryReady: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val summary = repository.summarizeNote(noteContent)
                onSummaryReady(summary)
            } catch (e: Exception) {
                onSummaryReady("Error generating summary: ${e.message}")
            }
        }
    }

    fun generateStudyDetails(noteContent: String, onStudyDetailsReady: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val studyDetails = repository.generateStudyDetails(noteContent)
                onStudyDetailsReady(studyDetails)
            } catch (e: Exception) {
                onStudyDetailsReady("Error generating study details: ${e.message}")
            }
        }
    }
}
