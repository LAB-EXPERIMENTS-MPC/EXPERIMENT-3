package com.example.calculatorapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvDisplay: TextView
    private lateinit var tvResult: TextView

    private var currentNumber = ""
    private var previousNumber = ""
    private var operator = ""
    private var isNewOperation = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvDisplay = findViewById(R.id.tvDisplay)
        tvResult = findViewById(R.id.tvResult)

        val numberButtons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
            R.id.btnDot
        )

        for (id in numberButtons) {
            findViewById<Button>(id).setOnClickListener {
                onNumberClicked(it as Button)
            }
        }

        findViewById<Button>(R.id.btnAdd).setOnClickListener { onOperatorClicked("+") }
        findViewById<Button>(R.id.btnSubtract).setOnClickListener { onOperatorClicked("-") }
        findViewById<Button>(R.id.btnMultiply).setOnClickListener { onOperatorClicked("×") }
        findViewById<Button>(R.id.btnDivide).setOnClickListener { onOperatorClicked("÷") }
        findViewById<Button>(R.id.btnEquals).setOnClickListener { onEqualsClicked() }
        findViewById<Button>(R.id.btnClear).setOnClickListener { onClearClicked() }
    }

    private fun onNumberClicked(button: Button) {
        val value = button.text.toString()

        if (isNewOperation) {
            currentNumber = ""
            isNewOperation = false
        }

        if (value == "." && currentNumber.contains(".")) return

        currentNumber += value
        tvDisplay.text = currentNumber
    }

    private fun onOperatorClicked(op: String) {
        if (currentNumber.isEmpty() && previousNumber.isEmpty()) return

        if (previousNumber.isNotEmpty() && currentNumber.isNotEmpty() && operator.isNotEmpty()) {
            onEqualsClicked()
        }

        if (currentNumber.isNotEmpty()) {
            previousNumber = currentNumber
        }

        operator = op
        isNewOperation = true
        tvResult.text = "$previousNumber $operator"
    }

    private fun onEqualsClicked() {
        if (previousNumber.isEmpty() || currentNumber.isEmpty() || operator.isEmpty()) return

        val num1 = previousNumber.toDoubleOrNull()
        val num2 = currentNumber.toDoubleOrNull()

        if (num1 == null || num2 == null) {
            tvDisplay.text = "Error"
            return
        }

        val result = when (operator) {
            "+" -> num1 + num2
            "-" -> num1 - num2
            "×" -> num1 * num2
            "÷" -> {
                if (num2 == 0.0) {
                    tvDisplay.text = "Cannot divide by zero"
                    tvResult.text = ""
                    resetValues()
                    return
                }
                num1 / num2
            }
            else -> return
        }

        val displayResult = if (result == result.toLong().toDouble()) {
            result.toLong().toString()
        } else {
            String.format("%.10g", result)
        }

        tvResult.text = "$previousNumber $operator $currentNumber ="
        tvDisplay.text = displayResult
        currentNumber = displayResult
        previousNumber = ""
        operator = ""
        isNewOperation = true
    }

    private fun onClearClicked() {
        resetValues()
        tvDisplay.text = "0"
        tvResult.text = ""
    }

    private fun resetValues() {
        currentNumber = ""
        previousNumber = ""
        operator = ""
        isNewOperation = true
    }
}
