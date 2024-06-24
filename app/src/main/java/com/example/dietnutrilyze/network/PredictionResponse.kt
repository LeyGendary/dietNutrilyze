package com.example.dietnutrilyze.network

data class PredictionResponse(
    val status: String,
    val message: String,
    val data: PredictionData
)

data class PredictionData(
    val id: String,
    val result: String,
    val Calories: String,
    // Add other fields if needed
    val confidenceScore: Double,
    val createdAt: String
)
