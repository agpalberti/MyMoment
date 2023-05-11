package com.agp.mymoment.ui.search

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.agp.mymoment.R
import com.agp.mymoment.model.classes.User
import com.agp.mymoment.navigation.Destinations
import com.agp.mymoment.ui.composables.*
import com.agp.mymoment.ui.image.ImageView

@Composable
fun SearchScreen(
    navController: NavHostController,
    viewModel: SearchScreenViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = true){
        viewModel.updateScreen()
    }

    if (viewModel.isPopLaunched && !viewModel.openImageView) viewModel.isPopLaunched = false

    var blur = 0.dp
    if (viewModel.openImageView) {
        BackPressHandler(onBackPressed = {
            viewModel.resetImageView()
        })
        blur = 10.dp
    }
    Column(
        Modifier
            .fillMaxSize()
            .blur(blur, blur)
    ) {
        ThemedNavBar(navController = navController, topBarContent = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
            ) {

                ThemedTextField(
                    value = viewModel.searchText,
                    onValueChange = { viewModel.searchText = it },
                    labelText = stringResource(id = R.string.search),
                    keyBoardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    fontSize = 14.sp
                )

            }
        }) {


            if (viewModel.searchText.isNotBlank()) {
                SearchScreenBody(navController)
            } else {
                ExploreScreenBody(navController)
            }

        }
    }


}

@Composable
@Preview
fun SearchScreenBody(
    navController: NavHostController? = null,
    viewModel: SearchScreenViewModel = hiltViewModel()
) {
    Log.i("Buscar", "${viewModel.users}")

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {

        val filteredMap = viewModel.users.filter {
            it.value.name?.contains(viewModel.searchText) == true || it.value.nickname?.contains(
                viewModel.searchText
            ) == true
        }
        val keyList = filteredMap.keys.toList()

        items(keyList.size) { item ->

            val followingUser by viewModel.amIFollowing(keyList[item])
                .collectAsState(initial = false)
            val following by viewModel.isHeFollowing(keyList[item]).collectAsState(initial = false)
            val pfp by viewModel.getPfp(keyList[item]).collectAsState(initial = "")

            Log.i(
                "Search",
                "Result: " + keyList[item] + ":\n " + viewModel.users[keyList[item]] + "\n pfp: $pfp\n isHeFollowing: $following\n AmIFollowing: $followingUser"
            )

            Row(
                Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            ) {
                UserListView(
                    Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    pfpUrl = pfp,
                    user = viewModel.users[keyList[item]] ?: User(),
                    followingUser = followingUser ?: false,
                    following = following ?: false,
                    onUserClick = { navController!!.navigate("${Destinations.ProfileScreen.ruta}/${keyList[item]}") }) {
                    //todo
                }
            }
        }
    }
}

@Composable
@Preview
fun ExploreScreenBody(
    navController: NavHostController? = null,
    viewModel: SearchScreenViewModel = hiltViewModel()
) {


    Log.i("Explore", "${viewModel.posts}")

// todo arreglar que se vea la ultima fila que esta cortada por alguna razon


    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.925f),
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(0.dp)
    ) {
        items(viewModel.posts.size) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                Arrangement.Center
            ) {

                val user =
                    viewModel.users.filter { it.value.posts!!.contains(viewModel.posts[viewModel.item]) }
                if (user.isNotEmpty()) {
                    val pfp by viewModel.getPfp(user.keys.first()).collectAsState(initial = "")

                    ImageContainer(
                        image = viewModel.posts[item].download_link ?: "",
                        contentDescription = "Image $item",
                        modifier = Modifier
                            .size(130.dp)
                            .padding(2.dp)
                    ) {
                        viewModel.openImageView = true
                        viewModel.item = item
                    }

                    if (viewModel.openImageView && !viewModel.isPopLaunched) {
                        viewModel.isPopLaunched = true
                        Log.i("Popup", "User: ${viewModel.posts[viewModel.item]}")
                        ImageView(
                            url = viewModel.posts[viewModel.item].download_link ?: "",
                            pfpUrl = pfp,
                            userUid = user.keys.first(),
                            onDeleteRequest = {viewModel.updateScreen()},
                            onUserClick = {
                                navController!!.navigate("${Destinations.ProfileScreen.ruta}/${user.keys.first()}")
                                viewModel.resetImageView()
                            }
                        ) {
                            viewModel.resetImageView()
                        }

                    }
                }
            }
        }
    }
}