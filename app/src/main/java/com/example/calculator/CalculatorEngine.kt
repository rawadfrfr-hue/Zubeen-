package com.example.calculator

import com.example.model.NumberLanguage
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import java.util.Stack

object CalculatorEngine {

    private val decimalFormat = DecimalFormat("#,##0.########", DecimalFormatSymbols(Locale.US))

    /**
     * Converts standard digits (0-9) to Bengali digits (০-৯) or vice versa.
     */
    fun formatDisplay(text: String, language: NumberLanguage): String {
        if (language == NumberLanguage.ENGLISH) return text

        val englishDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
        val bengaliDigits = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')

        var result = text
        for (i in englishDigits.indices) {
            result = result.replace(englishDigits[i], bengaliDigits[i])
        }
        return result
    }

    /**
     * Converts Bengali input digits back to standard ASCII digits for calculation.
     */
    fun toStandardDigits(text: String): String {
        val englishDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
        val bengaliDigits = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')

        var result = text
        for (i in bengaliDigits.indices) {
            result = result.replace(bengaliDigits[i], englishDigits[i])
        }
        return result
    }

    /**
     * Evaluates a mathematical expression string.
     * Returns EvaluationResult.Success or EvaluationResult.Error.
     */
    fun evaluate(rawExpression: String): EvaluationResult {
        val expr = toStandardDigits(rawExpression)
            .replace("×", "*")
            .replace("÷", "/")
            .trim()

        if (expr.isEmpty()) {
            return EvaluationResult.Success("")
        }

        return try {
            val tokens = tokenize(expr)
            if (tokens.isEmpty()) return EvaluationResult.Success("")
            
            val valResult = evaluateTokens(tokens)
            if (valResult.isNaN() || valResult.isInfinite()) {
                EvaluationResult.Error("শূন্য দিয়ে ভাগ করা সম্ভব নয়") // Cannot divide by zero
            } else {
                val formatted = formatNumber(valResult)
                EvaluationResult.Success(formatted)
            }
        } catch (e: ArithmeticException) {
            EvaluationResult.Error("শূন্য দিয়ে ভাগ করা সম্ভব নয়")
        } catch (e: Exception) {
            EvaluationResult.Error("ভুল সমীকরণ") // Invalid expression
        }
    }

    private fun formatNumber(number: Double): String {
        return if (number % 1.0 == 0.0 && number < 1e12 && number > -1e12) {
            number.toLong().toString()
        } else {
            decimalFormat.format(number)
        }
    }

    private fun tokenize(expr: String): List<String> {
        val tokens = mutableListOf<String>()
        var i = 0
        val len = expr.length

        while (i < len) {
            val c = expr[i]
            when {
                c.isWhitespace() -> i++
                c in "0123456789." -> {
                    val sb = StringBuilder()
                    while (i < len && (expr[i] in "0123456789.")) {
                        sb.append(expr[i])
                        i++
                    }
                    tokens.add(sb.toString())
                }
                c in "+-*/%" -> {
                    // Check for unary minus or plus at start or after another operator
                    if ((c == '-' || c == '+') && (tokens.isEmpty() || tokens.last() in listOf("+", "-", "*", "/"))) {
                        val sb = StringBuilder()
                        sb.append(c)
                        i++
                        while (i < len && (expr[i] in "0123456789.")) {
                            sb.append(expr[i])
                            i++
                        }
                        if (sb.length > 1) {
                            tokens.add(sb.toString())
                        } else {
                            tokens.add(c.toString())
                        }
                    } else {
                        tokens.add(c.toString())
                        i++
                    }
                }
                else -> i++
            }
        }
        return tokens
    }

    private fun evaluateTokens(tokens: List<String>): Double {
        if (tokens.isEmpty()) return 0.0

        // Parse numbers and handle precedence
        val values = Stack<Double>()
        val ops = Stack<Char>()

        fun precedence(op: Char): Int = when (op) {
            '+', '-' -> 1
            '*', '/', '%' -> 2
            else -> -1
        }

        fun applyOp(op: Char, b: Double, a: Double): Double {
            return when (op) {
                '+' -> a + b
                '-' -> a - b
                '*' -> a * b
                '/' -> if (b == 0.0) throw ArithmeticException("Divide by zero") else a / b
                '%' -> a % b
                else -> 0.0
            }
        }

        var idx = 0
        while (idx < tokens.size) {
            val token = tokens[idx]

            // Check if token is number or operator
            val num = token.toDoubleOrNull()
            if (num != null) {
                var value = num
                // Check if followed by trailing % without explicit second operand
                if (idx + 1 < tokens.size && tokens[idx + 1] == "%") {
                    val nextTokenIsOpOrEnd = (idx + 2 >= tokens.size || tokens[idx + 2] in listOf("+", "-", "*", "/"))
                    if (nextTokenIsOpOrEnd) {
                        value /= 100.0
                        idx++ // skip %
                    }
                }
                values.push(value)
            } else if (token.length == 1 && token[0] in "+-*/%") {
                val op = token[0]
                while (!ops.isEmpty() && precedence(ops.peek()) >= precedence(op)) {
                    if (values.size < 2) break
                    val val2 = values.pop()
                    val val1 = values.pop()
                    values.push(applyOp(ops.pop(), val2, val1))
                }
                ops.push(op)
            }
            idx++
        }

        while (!ops.isEmpty()) {
            if (values.size < 2) break
            val val2 = values.pop()
            val val1 = values.pop()
            values.push(applyOp(ops.pop(), val2, val1))
        }

        return if (values.isNotEmpty()) values.pop() else 0.0
    }
}

sealed class EvaluationResult {
    data class Success(val value: String) : EvaluationResult()
    data class Error(val message: String) : EvaluationResult()
}
