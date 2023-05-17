package com.agp.mymoment.ui.composables

import android.util.Log
import android.widget.Button
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.agp.mymoment.model.classes.Post
import com.agp.mymoment.model.classes.User

@Composable
fun StoryView(user: User, stories: MutableList<Post>?) {

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

    Log.i("Story" , "index: $index")

    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center ) {

        Box(Modifier.fillMaxHeight(0.89f)) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop,
                model = stories?.get(index)?.download_link,
                contentDescription = "Image"
            )


            Row(Modifier.fillMaxSize().alpha(0f)) {
                Button(onClick = { previous() }, modifier = Modifier.fillMaxHeight().fillMaxWidth(0.5f)) {  }
                Button(onClick = { next() }, modifier = Modifier.fillMaxSize()) { }
            }


        }


    }

}