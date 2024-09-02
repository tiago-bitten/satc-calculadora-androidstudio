package br.com.labian.calculadora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.labian.calculadora.ui.theme.CalculadoraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculadoraTheme {
                CalculatorScreen()
            }
        }
    }
}

@Composable
fun CalculatorScreen() {
    var result by remember { mutableStateOf("") }
    var currentInput by remember { mutableStateOf("") }
    var operation by remember { mutableStateOf<Char?>(null) }
    var firstOperand by remember { mutableStateOf("") }

    fun onDigitClick(digit: String) {
        currentInput += digit
    }

    fun onOperatorClick(op: Char) {
        if (currentInput.isNotEmpty()) {
            firstOperand = currentInput
            operation = op
            currentInput = ""
        }
    }

    fun onEqualClick() {
        if (operation != null && firstOperand.isNotEmpty() && currentInput.isNotEmpty()) {
            val operand1 = firstOperand.toDouble()
            val operand2 = currentInput.toDouble()
            result = when (operation) {
                '+' -> (operand1 + operand2).toString()
                '-' -> (operand1 - operand2).toString()
                '*' -> (operand1 * operand2).toString()
                '/' -> if (operand2 != 0.0) (operand1 / operand2).toString() else "Error"
                else -> ""
            }
            operation = null
            firstOperand = ""
            currentInput = result
        }
    }

    fun onClearClick() {
        currentInput = ""
        firstOperand = ""
        operation = null
        result = ""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = currentInput.ifEmpty { "0" },
            fontSize = 48.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End,
            maxLines = 1
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { onClearClick() }, modifier = Modifier.weight(1f)) {
                    Text("C")
                }
                Button(onClick = { onOperatorClick('/') }, modifier = Modifier.weight(1f)) {
                    Text("/")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            for (row in listOf(
                listOf("7", "8", "9", "*"),
                listOf("4", "5", "6", "-"),
                listOf("1", "2", "3", "+"),
                listOf("0", ".", "=")
            )) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (item in row) {
                        Button(
                            onClick = {
                                when (item) {
                                    "=" -> onEqualClick()
                                    "+", "-", "*", "/" -> onOperatorClick(item.first())
                                    else -> onDigitClick(item)
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                        ) {
                            Text(item, fontSize = 24.sp)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorPreview() {
    CalculadoraTheme {
        CalculatorScreen()
    }
}
