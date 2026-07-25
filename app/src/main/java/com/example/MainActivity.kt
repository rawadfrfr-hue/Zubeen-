package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.CalculatorDisplay
import com.example.ui.components.CalculatorKeypad
import com.example.ui.components.HistorySheet
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.CalculatorViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: CalculatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                CalculatorApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun CalculatorApp(
    viewModel: CalculatorViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.systemBars
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            CalculatorDisplay(
                state = state,
                onToggleLanguage = viewModel::onToggleLanguage,
                onToggleHistory = viewModel::onToggleHistory,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            CalculatorKeypad(
                language = state.language,
                onDigitClick = viewModel::onDigitClick,
                onOperatorClick = viewModel::onOperatorClick,
                onDecimalClick = viewModel::onDecimalClick,
                onClearClick = viewModel::onClearClick,
                onBackspaceClick = viewModel::onBackspaceClick,
                onPlusMinusClick = viewModel::onPlusMinusClick,
                onPercentageClick = viewModel::onPercentageClick,
                onEqualsClick = viewModel::onEqualsClick
            )
        }

        if (state.showHistory) {
            HistorySheet(
                history = state.history,
                language = state.language,
                onDismiss = viewModel::onToggleHistory,
                onSelectItem = viewModel::onSelectHistoryItem,
                onClearHistory = viewModel::onClearHistory
            )
        }
    }
}
