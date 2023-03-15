package com.agp.mymoment.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun NoRippleTextButton(modifier: Modifier = Modifier, onClick: () -> Unit, content: @Composable() (RowScope.() -> Unit)) {

    val interactionSource = remember {
        MutableInteractionSource()
    }

    Row(content = content, modifier = modifier.clickable(
        indication = null, interactionSource = interactionSource, onClick = onClick) )
}