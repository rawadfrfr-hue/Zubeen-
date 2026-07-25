package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculator.CalculatorEngine
import com.example.model.NumberLanguage

enum class KeypadButtonType {
    DIGIT,
    OPERATOR,
    ACTION,
    EQUALS
}

@Composable
fun CalculatorKeypad(
    language: NumberLanguage,
    onDigitClick: (String) -> Unit,
    onOperatorClick: (String) -> Unit,
    onDecimalClick: () -> Unit,
    onClearClick: () -> Unit,
    onBackspaceClick: () -> Unit,
    onPlusMinusClick: () -> Unit,
    onPercentageClick: () -> Unit,
    onEqualsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp),
        color = MaterialTheme.colorScheme.surfaceContainer,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Divider handle
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .background(MaterialTheme.colorScheme.outline, CircleShape)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Row 1: AC, ⌫, %, ÷
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                KeypadButton(
                    text = "AC",
                    type = KeypadButtonType.ACTION,
                    modifier = Modifier.weight(1f).testTag("btn_clear"),
                    onClick = onClearClick
                )
                KeypadButton(
                    text = "⌫",
                    type = KeypadButtonType.ACTION,
                    modifier = Modifier.weight(1f).testTag("btn_backspace"),
                    onClick = onBackspaceClick
                )
                KeypadButton(
                    text = "%",
                    type = KeypadButtonType.ACTION,
                    modifier = Modifier.weight(1f).testTag("btn_percent"),
                    onClick = onPercentageClick
                )
                KeypadButton(
                    text = "÷",
                    type = KeypadButtonType.OPERATOR,
                    modifier = Modifier.weight(1f).testTag("btn_divide"),
                    onClick = { onOperatorClick("÷") }
                )
            }

            // Row 2: 7, 8, 9, ×
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                KeypadButton(
                    text = CalculatorEngine.formatDisplay("7", language),
                    type = KeypadButtonType.DIGIT,
                    modifier = Modifier.weight(1f).testTag("btn_7"),
                    onClick = { onDigitClick("7") }
                )
                KeypadButton(
                    text = CalculatorEngine.formatDisplay("8", language),
                    type = KeypadButtonType.DIGIT,
                    modifier = Modifier.weight(1f).testTag("btn_8"),
                    onClick = { onDigitClick("8") }
                )
                KeypadButton(
                    text = CalculatorEngine.formatDisplay("9", language),
                    type = KeypadButtonType.DIGIT,
                    modifier = Modifier.weight(1f).testTag("btn_9"),
                    onClick = { onDigitClick("9") }
                )
                KeypadButton(
                    text = "×",
                    type = KeypadButtonType.OPERATOR,
                    modifier = Modifier.weight(1f).testTag("btn_multiply"),
                    onClick = { onOperatorClick("×") }
                )
            }

            // Row 3: 4, 5, 6, -
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                KeypadButton(
                    text = CalculatorEngine.formatDisplay("4", language),
                    type = KeypadButtonType.DIGIT,
                    modifier = Modifier.weight(1f).testTag("btn_4"),
                    onClick = { onDigitClick("4") }
                )
                KeypadButton(
                    text = CalculatorEngine.formatDisplay("5", language),
                    type = KeypadButtonType.DIGIT,
                    modifier = Modifier.weight(1f).testTag("btn_5"),
                    onClick = { onDigitClick("5") }
                )
                KeypadButton(
                    text = CalculatorEngine.formatDisplay("6", language),
                    type = KeypadButtonType.DIGIT,
                    modifier = Modifier.weight(1f).testTag("btn_6"),
                    onClick = { onDigitClick("6") }
                )
                KeypadButton(
                    text = "-",
                    type = KeypadButtonType.OPERATOR,
                    modifier = Modifier.weight(1f).testTag("btn_subtract"),
                    onClick = { onOperatorClick("-") }
                )
            }

            // Row 4: 1, 2, 3, +
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                KeypadButton(
                    text = CalculatorEngine.formatDisplay("1", language),
                    type = KeypadButtonType.DIGIT,
                    modifier = Modifier.weight(1f).testTag("btn_1"),
                    onClick = { onDigitClick("1") }
                )
                KeypadButton(
                    text = CalculatorEngine.formatDisplay("2", language),
                    type = KeypadButtonType.DIGIT,
                    modifier = Modifier.weight(1f).testTag("btn_2"),
                    onClick = { onDigitClick("2") }
                )
                KeypadButton(
                    text = CalculatorEngine.formatDisplay("3", language),
                    type = KeypadButtonType.DIGIT,
                    modifier = Modifier.weight(1f).testTag("btn_3"),
                    onClick = { onDigitClick("3") }
                )
                KeypadButton(
                    text = "+",
                    type = KeypadButtonType.OPERATOR,
                    modifier = Modifier.weight(1f).testTag("btn_add"),
                    onClick = { onOperatorClick("+") }
                )
            }

            // Row 5: +/-, 0, ., =
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                KeypadButton(
                    text = "+/-",
                    type = KeypadButtonType.ACTION,
                    modifier = Modifier.weight(1f).testTag("btn_plus_minus"),
                    onClick = onPlusMinusClick
                )
                KeypadButton(
                    text = CalculatorEngine.formatDisplay("0", language),
                    type = KeypadButtonType.DIGIT,
                    modifier = Modifier.weight(1f).testTag("btn_0"),
                    onClick = { onDigitClick("0") }
                )
                KeypadButton(
                    text = ".",
                    type = KeypadButtonType.DIGIT,
                    modifier = Modifier.weight(1f).testTag("btn_decimal"),
                    onClick = onDecimalClick
                )
                KeypadButton(
                    text = "=",
                    type = KeypadButtonType.EQUALS,
                    modifier = Modifier.weight(1f).testTag("btn_equals"),
                    onClick = onEqualsClick
                )
            }
        }
    }
}

@Composable
private fun KeypadButton(
    text: String,
    type: KeypadButtonType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.93f else 1.0f,
        animationSpec = tween(durationMillis = 100),
        label = "button_scale"
    )

    val containerColor: Color
    val contentColor: Color
    val border: BorderStroke?

    when (type) {
        KeypadButtonType.DIGIT -> {
            containerColor = MaterialTheme.colorScheme.surface
            contentColor = MaterialTheme.colorScheme.onSurface
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        }
        KeypadButtonType.OPERATOR -> {
            containerColor = MaterialTheme.colorScheme.secondaryContainer
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            border = null
        }
        KeypadButtonType.ACTION -> {
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            border = null
        }
        KeypadButtonType.EQUALS -> {
            containerColor = MaterialTheme.colorScheme.primary
            contentColor = MaterialTheme.colorScheme.onPrimary
            border = null
        }
    }

    Button(
        onClick = onClick,
        modifier = modifier
            .aspectRatio(1.0f)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        border = border,
        interactionSource = interactionSource,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (type == KeypadButtonType.EQUALS) 2.dp else 0.dp,
            pressedElevation = 1.dp
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = if (text.length > 2) 20.sp else 26.sp,
                fontWeight = if (type == KeypadButtonType.EQUALS || type == KeypadButtonType.OPERATOR) FontWeight.Bold else FontWeight.Medium
            )
        )
    }
}

