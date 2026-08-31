package com.erno.app.data.model

data class PredictionResult(
    val label: String,
    val confidence: Float,
    val heatmapUrl: String? = null
)
