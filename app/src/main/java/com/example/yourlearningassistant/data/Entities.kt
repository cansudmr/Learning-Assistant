package com.example.yourlearningassistant.data

import androidx.room.*

@Entity(tableName = "lectures")
data class Lecture(
    @PrimaryKey(autoGenerate = true)
    val lectureId: Long = 0,
    val lectureName: String,
    val lectureDetails: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "notes",
    foreignKeys = [ForeignKey(
        entity = Lecture::class,
        parentColumns = ["lectureId"],
        childColumns = ["lectureId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Note(
    @PrimaryKey(autoGenerate = true)
    val noteId: Long = 0,
    val lectureId: Long,
    val noteTitle: String,
    val noteContent: String,
    val timestamp: Long = System.currentTimeMillis()
)