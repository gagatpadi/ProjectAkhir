package com.example.projectakhir.api

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

// --- Model Data untuk Request & Response Gemini ---
data class GeminiRequest(
    val contents: List<Content>,
    val systemInstruction: SystemInstruction? = null
)

data class Content(val parts: List<Part>)
data class Part(val text: String)
data class SystemInstruction(val parts: List<Part>)

data class GeminiResponse(val candidates: List<Candidate>)
data class Candidate(val content: Content)

/**
 * Antarmuka untuk memanggil API Gemini Google.
 * Berkas ini diletakkan di dalam package 'api'.
 */
interface GeminiApiService {
    @POST("v1beta/models/gemini-2.5-flash-preview-09-2025:generateContent")
    suspend fun generateMessage(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}