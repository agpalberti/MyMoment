package com.agp.mymoment.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.agp.mymoment.R
import com.agp.mymoment.model.classes.User

@Composable
fun UserListView(url: String?, user: User, followingUser: Boolean, following: Boolean, onUserClick: () -> Unit, onFollowClick: () -> Unit){

    Row(Modifier.clickable(onClick = onUserClick)){
        Row {

            AsyncImage(
                model = url,
                contentDescription = stringResource(id = R.string.pfp),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight()
                    .clip(CircleShape)
            )
            Column(Modifier.fillMaxSize()) {
                Text(text = user.nickname!!)
                Text(text = user.name!!)
                Text(text = user.description!!)
            }

            Text(text = if (following) "Te sigue" else "")


        }

        Row() {
            OutlinedButton(onClick = onFollowClick) {
                Text(text = if (followingUser) "Dejar de seguir" else "Seguir")
            }
        }
    }
    
}