package com.agp.mymoment.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun NoRippleIconButton(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    Icon(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier
            .size(30.dp)
            .clickable(indication = null, interactionSource = interactionSource, onClick = onClick),
        tint = MaterialTheme.colors.secondary
    )
}