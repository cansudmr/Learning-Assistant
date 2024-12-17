package com.example.yourlearningassistant.data

import android.util.Log
import com.example.yourlearningassistant.BuildConfig
import com.example.yourlearningassistant.Content
import com.example.yourlearningassistant.GeminiService
import com.example.yourlearningassistant.GenerateContentRequest
import com.example.yourlearningassistant.Part
import kotlinx.coroutines.flow.Flow

class LectureRepository(private val lectureDao: LectureDao, private val noteDao: NoteDao, private val geminiService: GeminiService) {
    val allLectures: Flow<List<Lecture>> = lectureDao.getAllLectures()

    suspend fun insertLecture(lecture: Lecture) = lectureDao.insertLecture(lecture)

    suspend fun deleteLecture(lecture: Lecture) = lectureDao.deleteLecture(lecture)

    fun getNotesForLecture(lectureId: Long) = noteDao.getNotesForLecture(lectureId)

    suspend fun insertNote(note: Note) = noteDao.insertNote(note)

    suspend fun deleteNote(note: Note) = noteDao.deleteNote(note)

    suspend fun updateNote(note: Note) = noteDao.updateNote(note)

    fun getNoteById(noteId: Long) = noteDao.getNoteById(noteId)

    suspend fun summarizeNote(noteContent: String): String {
        return try {
            val request = GenerateContentRequest(
                contents = listOf(
                    Content(
                        parts = listOf(
                            Part("Determine if the following text is summarizable. If it is, provide a concise summary. If not, explain why. Text: $noteContent")
                        )
                    )
                )
            )

            val response = geminiService.generateContent(
                apiKey = BuildConfig.GEMINI_API_KEY,
                request = request
            )

            response.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: "Unable to generate summary"
        } catch (e: Exception) {
            // Log the full error for debugging
            Log.e("GeminiAPI", "Error generating summary", e)

            // Provide a user-friendly error message
            when (e) {
                is retrofit2.HttpException -> {
                    when (e.code()) {
                        403 -> "API access denied. Check your API key."
                        429 -> "Too many requests. Please try again later."
                        else -> "Network error occurred: ${e.message()}"
                    }
                }

                is java.net.UnknownHostException -> "No internet connection"
                else -> "Unexpected error: ${e.localizedMessage}"
            }
        }
    }

    suspend fun generateStudyDetails(noteContent: String): String {
        return try {
            val request = GenerateContentRequest(
                contents = listOf(
                    Content(
                        parts = listOf(
                            Part("Provide a comprehensive, detailed study guide based on the following text. Break down key concepts, provide explanations, add potential exam questions, and highlight important insights. Text: $noteContent")
                        )
                    )
                )
            )

            val response = geminiService.generateContent(
                apiKey = BuildConfig.GEMINI_API_KEY,
                request = request
            )

            response.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: "Unable to generate study details"
        } catch (e: Exception) {
            // Similar error handling as summarizeNote method
            when (e) {
                is retrofit2.HttpException -> {
                    when (e.code()) {
                        403 -> "API access denied. Check your API key."
                        429 -> "Too many requests. Please try again later."
                        else -> "Network error occurred: ${e.message()}"
                    }
                }

                is java.net.UnknownHostException -> "No internet connection"
                else -> "Unexpected error: ${e.localizedMessage}"
            }
        }
    }
}