package com.example.demoenbd.validation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.demoenbd.ui.theme.DemoEnbdTheme


@Composable
fun IBANValidation() {
    val viewModel: IBANValidationModelView = viewModel()
    val textState by viewModel.textState
    val errorMessage by viewModel.errorMessage
    val isError by viewModel.isError
    var previousTextLength by remember { mutableIntStateOf(0) }

    OutlinedTextField(
        value = textState,
        onValueChange = { newValue ->
//            viewModel.onTextChange2(newValue.text)
            // Detect paste event when text length increases drastically
            if (newValue.text.length > previousTextLength + 3) {
                viewModel.onPasteText(newValue.toString())
            } else {
                viewModel.onTextChange(newValue.text)
            }
            previousTextLength = newValue.text.length
        },
        label = { Text("Enter IBAN") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        isError = isError?:false,
        supportingText = {
            errorMessage?.let {
                Text(it)
            }
        }
    )
}









//@Composable
//fun CustomTextField() {
//    OutlinedTextField(
//        value = textState,
//        onValueChange = { newValue ->
//            val cleanedText = newValue.text.replace(" ", "") // Remove existing spaces
//
//            val formattedText = buildString {
//                for (i in cleanedText.indices) {
//                    append(cleanedText[i])
//                    if ((i + 1) % 4 == 0 && i + 1 != cleanedText.length) {
//                        append(' ')
//                    }
//                }
//            }
//
//            // Update text and explicitly set cursor to the end
//            textState = TextFieldValue(
//                text = formattedText,
//                selection = TextRange(formattedText.length) // Cursor at end
//            )
//        },
//        label = { Text("Enter text") },
//        modifier = Modifier.fillMaxWidth(),
//        maxLines = 1,
//        singleLine = true
//    )
//}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DemoEnbdTheme {
        IBANValidation()
    }
}