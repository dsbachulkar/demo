package com.example.demoenbd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.demoenbd.ui.theme.DemoEnbdTheme
import com.example.demoenbd.validation.IBANValidation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            DemoEnbdTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    IBANValidation()
}




@Composable
fun CustomTextField() {
    var textState by remember { mutableStateOf(TextFieldValue()) }

    OutlinedTextField(
        value = textState,
        onValueChange = { newValue ->
            val cleanedText = newValue.text.replace(" ", "") // Remove existing spaces

            val formattedText = buildString {
                for (i in cleanedText.indices) {
                    append(cleanedText[i])
                    if ((i + 1) % 4 == 0 && i + 1 != cleanedText.length) {
                        append(' ')
                    }
                }
            }

            // Update text and explicitly set cursor to the end
            textState = TextFieldValue(
                text = formattedText,
                selection = TextRange(formattedText.length) // Cursor at end
            )
        },
        label = { Text("Enter text") },
        modifier = Modifier.fillMaxWidth(),
        maxLines = 1,
        singleLine = true
    )
}

@Composable
fun ibanValidation(){
    var formattedText by remember {  mutableStateOf("") }

    OutlinedTextField(
        value = formattedText,
        onValueChange = { text ->
            // Add spaces after 4, 8, and 12 characters
            formattedText = text.insertSpacesAt(arrayOf(4, 8, 12))
        },
        label = { Text("Enter text") },
        modifier = Modifier.fillMaxWidth()
    )

}
// Extension function to insert spaces at specific indices
fun String.insertSpacesAt(indices: Array<Int>): String {
    val builder = StringBuilder(this)
    indices.forEach {
        if (it < builder.length) {
            builder.insert(it, ' ')
        }
    }
    return builder.toString()
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DemoEnbdTheme {
        Greeting("Android")
    }
}