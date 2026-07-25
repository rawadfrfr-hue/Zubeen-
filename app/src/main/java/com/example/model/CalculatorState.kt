package com.example.model

enum class NumberLanguage {
    ENGLISH,
    BENGALI
}

data class CalculationHistoryItem(
    val expression: String,
    val result: String,
    val timestampMillis: Long = System.currentTimeMillis()
)

data class CalculatorState(
    val expression: String = "",
    val previewResult: String = "",
    val isEvaluated: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = null,
    val language: NumberLanguage = NumberLanguage.BENGALI,
    val history: List<CalculationHistoryItem> = emptyList(),
    val showHistory: Boolean = false
)
