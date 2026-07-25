package com.example

import com.example.calculator.CalculatorEngine
import com.example.calculator.EvaluationResult
import com.example.model.NumberLanguage
import org.junit.Assert.assertEquals
import org.junit.Test

class CalculatorEngineTest {

    @Test
    fun testAddition() {
        val result = CalculatorEngine.evaluate("15 + 25")
        assert(result is EvaluationResult.Success)
        assertEquals("40", (result as EvaluationResult.Success).value)
    }

    @Test
    fun testSubtraction() {
        val result = CalculatorEngine.evaluate("100 - 45")
        assert(result is EvaluationResult.Success)
        assertEquals("55", (result as EvaluationResult.Success).value)
    }

    @Test
    fun testMultiplication() {
        val result = CalculatorEngine.evaluate("12 × 5")
        assert(result is EvaluationResult.Success)
        assertEquals("60", (result as EvaluationResult.Success).value)
    }

    @Test
    fun testDivision() {
        val result = CalculatorEngine.evaluate("100 ÷ 4")
        assert(result is EvaluationResult.Success)
        assertEquals("25", (result as EvaluationResult.Success).value)
    }

    @Test
    fun testDivideByZero() {
        val result = CalculatorEngine.evaluate("50 ÷ 0")
        assert(result is EvaluationResult.Error)
        assertEquals("শূন্য দিয়ে ভাগ করা সম্ভব নয়", (result as EvaluationResult.Error).message)
    }

    @Test
    fun testBengaliDigitFormatting() {
        val formatted = CalculatorEngine.formatDisplay("1234567890", NumberLanguage.BENGALI)
        assertEquals("১২৩৪৫৬৭৮৯০", formatted)
    }
}
