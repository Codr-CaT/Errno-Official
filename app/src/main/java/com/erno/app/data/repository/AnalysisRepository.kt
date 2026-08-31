package com.erno.app.data.repository

import com.erno.app.data.model.PredictionResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface AnalysisRepository {
    fun getPredictions(): Flow<List<PredictionResult>>
}

class AnalysisRepositoryImpl : AnalysisRepository {
    override fun getPredictions(): Flow<List<PredictionResult>> = flow {
        // Mocking some data for the initial foundation
        emit(emptyList())
    }
}
