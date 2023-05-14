package com.agp.mymoment.ui.image

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ImageView(
    url: String,
    pfpUrl: String?,
    userUid: String,
    viewModel: ImageViewModel = hiltViewModel(),
    onUserClick: () -> Unit,
    onDeleteRequest: () -> Unit
) {

}