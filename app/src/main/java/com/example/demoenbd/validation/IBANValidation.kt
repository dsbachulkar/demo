package com.example.demoenbd.validation

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.demoenbd.ui.theme.DemoEnbdTheme


@Composable
fun IBANValidation() {
    val context = LocalContext.current
    val viewModel: IBANValidationModelView = viewModel()
    val textState by viewModel.textState
    val isValid = viewModel.isValid.value
    val clipboardManager = LocalClipboardManager.current

    val errorMessage by viewModel.errorMessage
    val isError by viewModel.isError

    viewModel.clearClipboard(context)

    // Handle paste from clipboard
    LaunchedEffect(Unit) {
        viewModel.clearText()
        val clipboardText = clipboardManager.getText()?.text ?: ""
        if (clipboardText.isNotEmpty()) {
            viewModel.onPasteText(clipboardText)
        }
    }

    ComponentIBANValidation(
        value = textState,
        onValueChange = { newValue ->
            viewModel.onTextChange(newValue.text)
//            viewModel.onTextChange2(newValue.text)
            // Detect paste event when text length increases drastically
//            if (newValue.text.length > 2) {
//                viewModel.onPasteText(newValue.text)
//            } else {
//                viewModel.onTextChange(newValue.text)
//            }
//            previousTextLength = newValue.text.length
        },
        label = { Text("Enter IBAN") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        isError = isError ?: false,
        supportingText = {
            errorMessage?.let {
                Text(it)
            }
        },
        trailingIcon = {
            if (textState.text.isNotEmpty()) {
                IconButton(onClick = {
                    if (isValid) {
                        println("Valid Text: ${textState.text}")
                    } else {
                        // Handle invalid click (e.g., show error dialog)
                        println("Invalid Text: ${textState.text}")
                        viewModel.clearText()
                    }
                }) {
                    Icon(
                        painter = painterResource(
                            id = if (isValid) android.R.drawable.checkbox_on_background
                            else android.R.drawable.ic_delete
                        ),
                        contentDescription = "Validation Icon",
                        tint = if (isValid) androidx.compose.ui.graphics.Color.Green else androidx.compose.ui.graphics.Color.Red
                    )
                }
            }
        },
    )

}

@Composable
fun ComponentIBANValidation(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = OutlinedTextFieldDefaults.shape,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors()
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = modifier.testTag("textBAN"),
        singleLine = singleLine,
        isError = isError,
        supportingText = supportingText,
        trailingIcon = trailingIcon
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
If you want a regex that strictly enforces a minimum of 3 characters and a maximum of 60 characters, allowing letters, numbers, special characters, and at most one space, you can use the following Kotlin regex:

Kotlin Code

val regex = """^[a-zA-Z0-9@#\$%^&*()\-+=]{3,60}$|^[a-zA-Z0-9@#\$%^&*()\-+=]{1,59} [a-zA-Z0-9@#\$%^&*()\-+=]{1,59}$""".toRegex()

fun isValidInput(input: String): Boolean {
    return regex.matches(input)
}

// Test cases Enter 1ˢᵗ string at the top of the input field
fun main() {
    val testInputs = listOf(
        "Hello123",        // ✅ Valid
        "H@",              // ❌ Invalid (less than 3 characters)
        "Hello 123@",      // ✅ Valid (one space allowed)
        "Hello  World",    // ❌ Invalid (more than one space)
        "A_valid-entry!",  // ✅ Valid
        "A very very long text that has too many characters exceeding the limit", // ❌ Invalid
    )

    testInputs.forEach {
        println("$it -> ${isValidInput(it)}")
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DemoEnbdTheme {
        IBANValidation()
    }
}
