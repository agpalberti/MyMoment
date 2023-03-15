package com.agp.mymoment.ui.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun DropdownThemeItem(text: String, iconId: Int, onClick: () -> Unit) {
    DropdownMenuItem(onClick = onClick) {
        Icon(
            painterResource(id = iconId),
            contentDescription = text,
            modifier = Modifier.size(25.dp)
        )

        Spacer(modifier = Modifier.size(10.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.primary
        )
    }
}