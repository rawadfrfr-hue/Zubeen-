package com.example.viewmodel

import androidx.lifecycle.ViewModel
import com.example.calculator.CalculatorEngine
import com.example.calculator.EvaluationResult
import com.example.model.CalculationHistoryItem
import com.example.model.CalculatorState
import com.example.model.NumberLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CalculatorViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CalculatorState())
    val uiState: StateFlow<CalculatorState> = _uiState.asStateFlow()

    fun onDigitClick(digit: String) {
        _uiState.update { currentState ->
            val newExpr = if (currentState.isEvaluated) {
                digit
            } else {
                currentState.expression + digit
            }
            val preview = calculatePreview(newExpr)
            currentState.copy(
                expression = newExpr,
                previewResult = preview,
                isEvaluated = false,
                isError = false,
                errorMessage = null
            )
        }
    }

    fun onOperatorClick(op: String) {
        _uiState.update { currentState ->
            var currentExpr = currentState.expression

            // If expression was just evaluated, chain from preview/previous result
            if (currentState.isEvaluated && currentState.previewResult.isNotEmpty() && !currentState.isError) {
                currentExpr = currentState.previewResult
            }

            if (currentExpr.isEmpty()) {
                if (op == "-") {
                    val newExpr = "-"
                    return@update currentState.copy(
                        expression = newExpr,
                        isEvaluated = false,
                        isError = false
                    )
                }
                return@update currentState
            }

            // Replace trailing operator if already present
            val lastChar = currentExpr.lastOrNull()
            val newExpr = if (lastChar != null && isOperator(lastChar.toString())) {
                currentExpr.dropLast(1) + op
            } else {
                currentExpr + op
            }

            val preview = calculatePreview(newExpr)
            currentState.copy(
                expression = newExpr,
                previewResult = preview,
                isEvaluated = false,
                isError = false,
                errorMessage = null
            )
        }
    }

    fun onDecimalClick() {
        _uiState.update { currentState ->
            val currentExpr = if (currentState.isEvaluated) "0" else currentState.expression
            
            // Get last number token
            val tokens = currentExpr.split(Regex("[+\\-×÷%]"))
            val lastToken = tokens.lastOrNull() ?: ""

            if (lastToken.contains(".")) {
                currentState
            } else {
                val newExpr = if (lastToken.isEmpty()) "${currentExpr}0." else "$currentExpr."
                val preview = calculatePreview(newExpr)
                currentState.copy(
                    expression = newExpr,
                    previewResult = preview,
                    isEvaluated = false,
                    isError = false
                )
            }
        }
    }

    fun onPercentageClick() {
        onOperatorClick("%")
    }

    fun onPlusMinusClick() {
        _uiState.update { currentState ->
            var expr = currentState.expression
            if (expr.isEmpty()) return@update currentState

            // Find last number term
            val lastOpIndex = expr.indexOfLast { it in listOf('+', '-', '×', '÷') }
            if (lastOpIndex == -1) {
                expr = if (expr.startsWith("-")) expr.drop(1) else "-$expr"
            } else {
                val op = expr[lastOpIndex]
                val prefix = expr.substring(0, lastOpIndex)
                val number = expr.substring(lastOpIndex + 1)

                expr = when (op) {
                    '+' -> "$prefix-$number"
                    '-' -> "$prefix+$number"
                    else -> "$prefix$op-$number"
                }
            }

            val preview = calculatePreview(expr)
            currentState.copy(
                expression = expr,
                previewResult = preview,
                isEvaluated = false
            )
        }
    }

    fun onClearClick() {
        _uiState.update {
            it.copy(
                expression = "",
                previewResult = "",
                isEvaluated = false,
                isError = false,
                errorMessage = null
            )
        }
    }

    fun onBackspaceClick() {
        _uiState.update { currentState ->
            if (currentState.expression.isEmpty()) return@update currentState

            val newExpr = if (currentState.isEvaluated) {
                ""
            } else {
                currentState.expression.dropLast(1)
            }
            val preview = calculatePreview(newExpr)
            currentState.copy(
                expression = newExpr,
                previewResult = preview,
                isEvaluated = false,
                isError = false,
                errorMessage = null
            )
        }
    }

    fun onEqualsClick() {
        _uiState.update { currentState ->
            if (currentState.expression.isEmpty()) return@update currentState

            when (val eval = CalculatorEngine.evaluate(currentState.expression)) {
                is EvaluationResult.Success -> {
                    if (eval.value.isNotEmpty()) {
                        val historyItem = CalculationHistoryItem(
                            expression = currentState.expression,
                            result = eval.value
                        )
                        currentState.copy(
                            expression = currentState.expression,
                            previewResult = eval.value,
                            isEvaluated = true,
                            isError = false,
                            errorMessage = null,
                            history = listOf(historyItem) + currentState.history
                        )
                    } else currentState
                }
                is EvaluationResult.Error -> {
                    currentState.copy(
                        isError = true,
                        errorMessage = eval.message,
                        isEvaluated = true
                    )
                }
            }
        }
    }

    fun onToggleLanguage() {
        _uiState.update {
            val newLang = if (it.language == NumberLanguage.BENGALI) NumberLanguage.ENGLISH else NumberLanguage.BENGALI
            it.copy(language = newLang)
        }
    }

    fun onToggleHistory() {
        _uiState.update { it.copy(showHistory = !it.showHistory) }
    }

    fun onSelectHistoryItem(item: CalculationHistoryItem) {
        _uiState.update {
            it.copy(
                expression = item.result,
                previewResult = item.result,
                isEvaluated = false,
                showHistory = false
            )
        }
    }

    fun onClearHistory() {
        _uiState.update { it.copy(history = emptyList()) }
    }

    private fun calculatePreview(expr: String): String {
        if (expr.isEmpty()) return ""
        val last = expr.lastOrNull()
        if (last != null && isOperator(last.toString())) return ""

        return when (val eval = CalculatorEngine.evaluate(expr)) {
            is EvaluationResult.Success -> eval.value
            is EvaluationResult.Error -> ""
        }
    }

    private fun isOperator(c: String): Boolean {
        return c == "+" || c == "-" || c == "×" || c == "÷" || c == "%"
    }
}
