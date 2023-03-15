package com.agp.mymoment.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun TextIconButton(
    text: String,
    iconId: Int,
    contentDescription: String,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    onClick: () -> Unit
) {
    Row(
        modifier.clickable(onClick = onClick),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment
    ) {
        Icon(
            painterResource(id = iconId),
            contentDescription = contentDescription,
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