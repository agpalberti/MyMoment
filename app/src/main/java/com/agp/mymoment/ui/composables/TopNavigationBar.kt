package com.agp.mymoment.ui.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TopNavigationBar(content: @Composable() (RowScope.() -> Unit)){
    TopAppBar(
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp
    ){

        Row(Modifier.padding(horizontal = 5.dp), content = content)
    }


}