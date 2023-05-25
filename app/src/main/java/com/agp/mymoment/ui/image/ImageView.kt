package com.agp.mymoment.ui.image

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.agp.mymoment.R
import com.agp.mymoment.config.DeviceConfig
import com.agp.mymoment.ui.composables.DropdownThemeItem


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ImageView(
    url: String,
    pfpUrl: String?,
    userUid: String,
    viewModel: ImageViewModel = hiltViewModel(),
    onUserClick: () -> Unit,
    onDeleteRequest: () -> Unit,
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

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(end = 20.dp), horizontalArrangement = Arrangement.End){

                        val date = viewModel.post.date

                        Text(text = if (date.isNullOrEmpty() ) "" else DeviceConfig.translateDate(date), fontSize = 8.sp)
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


                        var likes by remember{mutableStateOf(viewModel.post.likes?.size?:0)}

                            IconButton(
                                onClick = {
                                    if (viewModel.isLiked) {viewModel.dislike(userUid); likes--}
                                    else {viewModel.like(userUid); likes++}
                                }) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                                    Icon(
                                        modifier = Modifier.size(25.dp),
                                        painter =
                                        if (viewModel.isLiked) painterResource(id = R.drawable.dislike)
                                        else painterResource(id = R.drawable.like),
                                        contentDescription = "Like"
                                    )


                                    Text(text = likes.toString())
                                }
                            }


                        //region Comentado
                        /*
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
                        */
                        //endregion

                        IconButton(onClick = { viewModel.enableOptions = true }) {
                            Icon(
                                modifier = Modifier.size(30.dp),
                                painter = painterResource(id = R.drawable.more),
                                contentDescription = "More"
                            )

                        }


                    }

                }

                //fixme: no deja hacer click en el menu
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