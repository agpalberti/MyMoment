package com.agp.mymoment.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.agp.mymoment.R
import com.agp.mymoment.model.classes.User

@Composable
fun UserListView(
    modifier: Modifier = Modifier,
    pfpUrl: String?,
    user: User,
    followingUser: Boolean,
    following: Boolean,
    onUserClick: () -> Unit,
    onFollowClick: () -> Unit
) {

    Row(modifier.clickable(onClick = onUserClick)) {
        Row {
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.background)
            ) {
                AsyncImage(
                    model = pfpUrl,
                    contentDescription = stringResource(id = R.string.pfp),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape)
                        .matchParentSize()
                )
            }

            Spacer(modifier = Modifier.size(10.dp))
            Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                Text(text = user.nickname!!)
                Text(text = user.name!!)
                Text(text = user.description!!)
            }

            Text(text = if (following) "Te sigue" else "")

            OutlinedButton(onClick = onFollowClick) {
                Text(text = if (followingUser) "Dejar de seguir" else "Seguir")

            }
        }
    }

}