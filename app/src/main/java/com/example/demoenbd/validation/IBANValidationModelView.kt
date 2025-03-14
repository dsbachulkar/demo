package com.example.demoenbd.validation

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextRange
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import androidx.compose.ui.text.input.TextFieldValue

class IBANValidationModelView : ViewModel() {
    private val _textState = mutableStateOf(TextFieldValue())
    val textState: State<TextFieldValue> = _textState

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private val _isError = mutableStateOf<Boolean?>(false)
    val isError: State<Boolean?> = _isError

/**
 * Text must start with "AE".
 * If it doesn't start with "AE", other text entries are not allowed.
 * Add a space after every 4 characters.
 * The maximum length of the text, excluding spaces, should be 23 characters.
 * @param inputText User-provided text to validate.
*/
    fun onTextChange(inputText: String) {
        // Remove spaces and validate the inputText
        val cleanedText = inputText.replace(" ", "")

        if ((cleanedText.length==1 && !cleanedText.startsWith("A"))
            || (cleanedText.length==2 && !cleanedText.startsWith("AE"))) {
            _isError.value = true
            _errorMessage.value = "IBAN start with AE"
            return // Block inputText if it doesn't start with "AE"
        }

        // Limit the length to 23 characters (excluding spaces)
        val trimmedText = cleanedText.take(23)

        // Add spaces after every 4 characters
        val formattedText = buildString {
            for (i in trimmedText.indices) {
                append(trimmedText[i])
                if ((i + 1) % 4 == 0 && i + 1 != trimmedText.length) {
                    append(' ')
                }
            }
        }
        // Update the state with the formatted text and set the cursor at the end
        _textState.value = TextFieldValue(
            text = formattedText,
            selection = TextRange(formattedText.length)
        )

        if(trimmedText.length ==23 || trimmedText.isEmpty()){
            _isError.value = false
            _errorMessage.value = ""
        }else{
            _isError.value = true
            _errorMessage.value = "IBAN value need 23 char"
        }
    }

/**
 * When pasting text, ensure it starts with "AE" and only copy up to 23 characters.
 * If pasted text doesn't start with "AE", disallow the paste.
 * Add a space after every 4 characters.
 *
 * @param pastedText User-provided text to validate.
 **/
    fun onPasteText(pastedText: String) {
        val cleanedText = pastedText.replace(" ", "")
        if (cleanedText.startsWith("AE")) {
            onTextChange(cleanedText.take(23))
        }else{
            _isError.value = true
            _errorMessage.value = "IBAN start with AE"
        }
    }


//    fun onTextChange(input: String) {
//        val cleanedText = input.replace(" ", "") // Remove existing spaces
//        val formattedText = buildString {
//            for (i in cleanedText.indices) {
//                append(cleanedText[i])
//                if ((i + 1) % 4 == 0 && i + 1 != cleanedText.length) {
//                    append(' ')
//                }
//            }
//        }
//
//        _textState.value = TextFieldValue(
//            text = formattedText,
//            selection = TextRange(formattedText.length) // Set cursor at the end
//        )
//    }
}