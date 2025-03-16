package com.example.demoenbd.validation

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextRange
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import androidx.compose.ui.text.input.TextFieldValue
import android.content.ClipboardManager
import android.content.ClipData
import android.content.Context

class IBANValidationModelView : ViewModel() {
    private val _textState = mutableStateOf(TextFieldValue())
    val textState: State<TextFieldValue> = _textState

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private val _isError = mutableStateOf<Boolean?>(false)
    val isError: State<Boolean?> = _isError


    private val _isValid = mutableStateOf(true)
    val isValid: State<Boolean> = _isValid

    private val regexPattern = Regex("^[A-Z0-9]*\$") // Only capital letters and numbers


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

        if ((cleanedText.length == 1 && !cleanedText.startsWith("A"))
            || (cleanedText.length == 2 && !cleanedText.startsWith("AE"))
        ) {
            _isError.value = true
            _isValid.value = false
            _errorMessage.value = "IBAN start with AE"
            return // Block inputText if it doesn't start with "AE"
        }
        if (!regexPattern.matches(cleanedText.replace(" ", ""))) {
            _errorMessage.value = "Only capital letters and numbers are allowed"
            _isError.value = true
            _isValid.value = false
            return
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

        if (trimmedText.length == 23 || trimmedText.isEmpty()) {
            _isError.value = false
            _errorMessage.value = ""
            _isValid.value = true
        } else {
            _isError.value = true
            _isValid.value = false
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

        if (!cleanedText.startsWith("AE")) {
            _textState.value = TextFieldValue()
            _isError.value = true
            _isValid.value = false
            _errorMessage.value = "IBAN start with AE"
        } else if (!regexPattern.matches(cleanedText.replace(" ", ""))) {
            _textState.value = TextFieldValue()
            _errorMessage.value = "Only capital letters and numbers are allowed"
            _isError.value = true
            _isValid.value = false
            return
        } else {
            onTextChange(cleanedText.take(23))

        }
    }

    fun clearText() {
        _textState.value = TextFieldValue()
        _isValid.value = true
        _isError.value = false
        _errorMessage.value = ""
    }

    fun clearClipboard(context: Context) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val emptyClip = ClipData.newPlainText("", "")
        clipboard.setPrimaryClip(emptyClip)
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