package com.example.demoenbd.validation

import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class IBANValidationModelViewTest {
    private lateinit var viewModel: IBANValidationModelView

    @Before
    fun setUp() {
        viewModel = IBANValidationModelView()
    }

    @Test
    fun `when text starts with AE, no error message is shown`() {
        viewModel.onPasteText("AE123456789012345678901")
        assertEquals("", viewModel.errorMessage.value)
        assertEquals("AE12 3456 7890 1234 5678 901", viewModel.textState.value.text)
    }

    @Test
    fun `when text does not start with AE, error message is shown`() {
        viewModel.onTextChange("BE1234567890")
        assertEquals("IBAN value need 23 char", viewModel.errorMessage.value)
    }

    @Test
    fun `when text exceeds 23 characters, it is trimmed`() {
        viewModel.onTextChange("AE12345678901234567890123456789")
        assertEquals("AE12 3456 7890 1234 5678 901", viewModel.textState.value.text)
    }

    @Test
    fun `onPasteText with valid text starting with AE works correctly`() {
        viewModel.onPasteText("AE12345678901234567890123")
        assertEquals("AE12 3456 7890 1234 5678 901", viewModel.textState.value.text)
        assertEquals("", viewModel.errorMessage.value)
    }

    @Test
    fun `onPasteText with invalid text shows error message`() {
        viewModel.onPasteText("BE12345678901234567890123")
        assertEquals("IBAN start with AE", viewModel.errorMessage.value)
    }
}