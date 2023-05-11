package com.agp.mymoment.ui.image

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.agp.mymoment.R
import com.agp.mymoment.ui.composables.DropdownThemeItem


@Composable
fun ImageView(
    url: String,
    pfpUrl: String?,
    userUid: String,
    viewModel: ImageViewModel = hiltViewModel(),
    onUserClick: () -> Unit,
    onDeleteRequest:() -> Unit,
    onDismissRequest: () -> Unit
) {

    val interactionSource = remember {
        MutableInteractionSource()
    }

    viewModel.updatePostInfo(url, userUid)

    Dialog(
        onDismissRequest = onDismissRequest,
        DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Surface(
            Modifier
                .fillMaxWidth()
                .height(400.dp)
                .clip(shape = RoundedCornerShape(20.dp)),
            elevation = 1.dp
        ) {

            Column(Modifier.fillMaxSize()) {

                Box(Modifier.weight(0.12f), contentAlignment = Alignment.CenterStart) {
                    //usuario y pfp
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp)
                            .clickable(
                                indication = null,
                                interactionSource = interactionSource,
                                onClick = onUserClick
                            ), verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(35.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colors.background),
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .matchParentSize()
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop,
                                model = pfpUrl,
                                contentDescription = "Image"
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(text = viewModel.user.nickname ?: "")
                    }

                }

                Box(Modifier.weight(0.76f)) {
                    //la imagen
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        AsyncImage(
                            model = url,
                            contentDescription = "Image",
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                Box(Modifier.weight(0.12f), contentAlignment = Alignment.Center) {

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {

                        IconButton(
                            onClick = {
                                if (viewModel.isLiked) viewModel.dislike(userUid)
                                else viewModel.like(userUid)
                            }) {
                            Icon(
                                modifier = Modifier.size(30.dp),
                                painter =
                                if (viewModel.isLiked) painterResource(id = R.drawable.dislike)
                                else painterResource(id = R.drawable.like),
                                contentDescription = "Like"
                            )
                        }
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                modifier = Modifier.size(30.dp),
                                painter = painterResource(id = R.drawable.comment),
                                contentDescription = "Comment"
                            )
                        }

                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                modifier = Modifier.size(30.dp),
                                painter = painterResource(id = R.drawable.save),
                                contentDescription = "Save"
                            )
                        }



                        IconButton(onClick = { viewModel.enableOptions = true }) {
                            Icon(modifier = Modifier.size(30.dp), painter = painterResource(id = R.drawable.more), contentDescription = "More")

                        }


                        }

                }

                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    DropdownMenu(
                        expanded = viewModel.enableOptions,
                        onDismissRequest = { viewModel.enableOptions = false }) {

                        DropdownThemeItem(text = "Reportar", iconId = R.drawable.error) {

                        }

                        if (viewModel.getCurrentUserUid() == userUid) {
                            DropdownThemeItem(text = "Eliminar", iconId = R.drawable.delete) {
                                viewModel.deletePost()
                                onDismissRequest()
                                onDeleteRequest()
                            }
                        }
                    }
                }


            }


        }
    }
}