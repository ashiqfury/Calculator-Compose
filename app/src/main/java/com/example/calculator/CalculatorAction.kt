package com.example.calculator

sealed class CalculatorAction {
    data class Number(val number: Int): CalculatorAction()
    object Decimal: CalculatorAction()
    object Delete: CalculatorAction()
    object Calculate: CalculatorAction()
    object Clear: CalculatorAction()
    data class Operations(val operation: CalculatorOperation): CalculatorAction()
}
