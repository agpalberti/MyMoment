package com.agp.mymoment.ui.composables

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.TextUnit

@Composable
fun EditTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    fontSize: TextUnit = TextUnit.Unspecified,
) {
    //todo singleline? y maxCharacters
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default,
    )
}
