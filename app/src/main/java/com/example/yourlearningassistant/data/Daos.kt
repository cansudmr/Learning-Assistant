package com.example.yourlearningassistant.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LectureDao {
    @Query("SELECT * FROM lectures ORDER BY timestamp DESC")
    fun getAllLectures(): Flow<List<Lecture>>

    @Insert
    suspend fun insertLecture(lecture: Lecture): Long

    @Delete
    suspend fun deleteLecture(lecture: Lecture)

    @Query("SELECT * FROM lectures WHERE lectureId = :lectureId")
    suspend fun getLectureById(lectureId: Long): Lecture
}

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes WHERE lectureId = :lectureId ORDER BY timestamp DESC")
    fun getNotesForLecture(lectureId: Long): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE noteId = :noteId")
    fun getNoteById(noteId: Long): Flow<Note>

    @Insert
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)
}