package com.agp.mymoment.ui.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit

@Composable
fun ThemedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    keyBoardOptions: KeyboardOptions,
    isError:Boolean = false,
    fontSize: TextUnit = TextUnit.Unspecified,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        placeholder = { Text(text = labelText, fontSize = fontSize) },
        keyboardOptions = keyBoardOptions,
        modifier = Modifier.fillMaxWidth(),
        isError = isError
    )
}

