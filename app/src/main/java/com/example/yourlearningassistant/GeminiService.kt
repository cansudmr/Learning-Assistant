package com.example.yourlearningassistant

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface GeminiService {
    @Headers("Content-Type: application/json")
    @POST("v1beta/models/gemini-pro:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GenerateContentRequest
    ): GenerateContentResponse
}

data class GenerateContentRequest(
    val contents: List<Content>
)

data class Content(
    val parts: List<Part>
)

data class Part(
    val text: String
)

data class GenerateContentResponse(
    val candidates: List<Candidate>
)

data class Candidate(
    val content: Content
)