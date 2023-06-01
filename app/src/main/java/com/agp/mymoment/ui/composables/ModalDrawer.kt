package com.agp.mymoment.ui.composables

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.agp.mymoment.config.DeviceConfig
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue
import kotlin.math.roundToInt


@Composable
fun ModalDrawer(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    switchSettings: () -> Unit,
    content: @Composable (ColumnScope.() -> Unit)
) {

    var offsetX by remember { mutableStateOf(0f) }
    val boxWidth =
        DeviceConfig.dpToFloat(DeviceConfig.DPwidthPercentage(DeviceConfig.screenWidth, 70))
    var returningDefault by remember { mutableStateOf(false) }

    val offsetAnimation: Dp by animateDpAsState(
        if (returningDefault) {
            0.dp
        } else DeviceConfig.pxToDp(offsetX),
        tween(durationMillis = 300, easing = LinearOutSlowInEasing)
    )
    if (returningDefault) {
        LaunchedEffect(Unit) {
            delay(300)
            returningDefault = false
        }
    }


    Row(
        Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .offset {
                IntOffset(
                    DeviceConfig
                        .dpToPx(offsetAnimation)
                        .roundToInt(), 0
                )
            }
            .pointerInput(Unit) {
                detectDragGestures(onDragEnd = {
                    //Si se mueve lo suficiente a la izquierda se cierra
                    if ((offsetX.absoluteValue * 0.7) > -DeviceConfig.tinyFloatToBig(
                            boxWidth
                        )
                    ) {
                        switchSettings()
                    } else {
                        //Si no, vuelve a su posicion
                        offsetX = 0f
                        returningDefault = true

                    }

                }) { change, dragAmount ->
                    change.consume()
                    if (dragAmount.x > 0) {
                        offsetX += dragAmount.x
                    } else if (dragAmount.x < 0 && offsetX > 0) {
                        offsetX += dragAmount.x
                    }

                }
            }) {

        Box(Modifier.weight(0.3f)) {
            //Boton invisible fuera para cerrar fondo
            Button(modifier = Modifier
                .fillMaxSize()
                .alpha(0f), onClick = { switchSettings() }) {}
        }

        Box(
            Modifier

                .weight(boxWidth)
        ) {

            Button(modifier = Modifier.fillMaxSize(), onClick = {}) {}

            Column(
                modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                verticalArrangement = verticalArrangement,
                horizontalAlignment = horizontalAlignment,
                content = content
            )
        }

    }


}

@Composable
fun BackPressHandler(
    onBackPressed: () -> Unit,
    backPressedDispatcher: OnBackPressedDispatcher? = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
) {
    val currentOnBackPressed by rememberUpdatedState(newValue = onBackPressed)

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currentOnBackPressed()
            }
        }
    }

    DisposableEffect(key1 = backPressedDispatcher) {
        backPressedDispatcher?.addCallback(backCallback)

        onDispose {
            backCallback.remove()
        }
    }
}