package com.agp.mymoment.ui.composables

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.agp.mymoment.config.DeviceConfig
import com.agp.mymoment.model.classes.Post
import com.agp.mymoment.model.classes.User
import com.agp.mymoment.navigation.Destinations

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StoryView(
    user: User,
    stories: MutableList<Post>?,
    uid: String,
    pfp: String,
    navController: NavHostController?
) {

    val interactionSource = remember {
        MutableInteractionSource()
    }

    var index by remember { mutableStateOf(0) }


    fun next() {
        if ((index + 1) < stories?.size!!) {
            index++
        }
    }

    fun previous() {
        if ((index - 1) >= 0) {
            index--
        }
    }

    Log.i("Story", "index: $index")
    Column(Modifier.fillMaxWidth()) {

        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

            Row(
                Modifier.clickable(
                    indication = null,
                    interactionSource = interactionSource
                ) {
                    navController!!.navigate(
                        "${Destinations.ProfileScreen.ruta}/$uid"
                    )
                }, verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .size(35.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.background),
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .matchParentSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                        model = pfp,
                        contentDescription = "Image"
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Text(text = user.nickname ?: "")
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(end = 20.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {


                if (stories != null) {
                    (if (stories[index].date?.isEmpty() == true) "" else stories[index].date?.let {
                        DeviceConfig.translateDate(
                            it
                        )
                    })?.let {
                        Text(
                            text = it, fontSize = 10.sp
                        )
                    }
                }
            }

        }

        /*

        Row(Modifier.fillMaxWidth()) {

            var indicatorProgress by remember { mutableStateOf(0f) }

            LaunchedEffect(index){
                indicatorProgress = 0f
                indicatorProgress = 1f
            }
            MyIndicator(indicatorProgress = indicatorProgress)

        }*/
    }
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box(Modifier.fillMaxHeight(0.89f)) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop,
                model = stories?.get(index)?.download_link,
                contentDescription = "Image"
            )

            Row(
                Modifier
                    .fillMaxSize()
                    .alpha(0f)
            ) {
                Button(
                    onClick = { previous() }, modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.5f)
                ) { }
                Button(onClick = { next() }, modifier = Modifier.fillMaxSize()) { }
            }
        }
    }

}

