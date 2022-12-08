package com.example.calculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CalculatorViewModel: ViewModel() {

    var state by mutableStateOf(CalculatorState())
        private set

    fun onAction(action: CalculatorAction) {
        when(action) {
            is CalculatorAction.Number -> enterNumber(action.number)
            is CalculatorAction.Calculate -> performCalculation()
            is CalculatorAction.Decimal -> enterDecimal()
            is CalculatorAction.Operations -> enterOperator(action.operation)
            is CalculatorAction.Delete -> performDeletion()
            is CalculatorAction.Clear -> state = CalculatorState()
        }
    }

    private fun performDeletion() {
        when {
            state.number2.isNotBlank() -> {
                state = state.copy(number2 = state.number2.dropLast(1))
            }
            state.operation != null -> {
                state = state.copy(operation = null)
            }
            state.number1.isNotBlank() -> {
                state = state.copy(number1 = state.number1.dropLast(1))
            }
        }
    }

    private fun enterOperator(operation: CalculatorOperation) {

        if (state.operation != null) {
            performCalculation()
        }

        if (state.number1.isNotBlank()) {
            state = state.copy(operation = operation)
        }
    }

    private fun enterDecimal() {
        if (state.operation == null && !state.number1.contains(".") && state.number1.isNotBlank()) {
            state = state.copy(number1 = state.number1 + ".")
            return
        }
        if (!state.number2.contains(".") && state.number2.isNotBlank()) {
            state = state.copy(number2 = state.number2 + ".")
        }
    }

    private fun performCalculation() {
        val number1 = state.number1.toDoubleOrNull()
        val number2 = state.number2.toDoubleOrNull()
        if (number1 != null && number2 != null) {
            val result = when(state.operation) {
                CalculatorOperation.Add -> number1 + number2
                CalculatorOperation.Subract -> number1 - number2
                CalculatorOperation.Multiply -> number1 * number2
                CalculatorOperation.Divide -> number1 / number2
                else -> return
            }

            state = state.copy(
                number1 = removeTrailingZeros(result.toString()),
                number2 = "",
                operation = null
            )
        }
    }

    private fun enterNumber(number: Int) {
        if (state.operation == null) {
            if (state.number1.length >= MAX_NUMBER_LENGTH) return
            state = state.copy(number1 = state.number1 + number)
            return
        }

        if (state.number2.length >= MAX_NUMBER_LENGTH) return
        state = state.copy(number2 = state.number2 + number)
    }

    private fun removeTrailingZeros(num: String): String {
        if(!num.contains('.'))
            return num
        return num
            .dropLastWhile { it == '0' }
            .dropLastWhile { it == '.' }
    }

    companion object {
        const val MAX_NUMBER_LENGTH = 8
    }
}