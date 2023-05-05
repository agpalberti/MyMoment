package com.agp.mymoment.ui.search

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
import com.agp.mymoment.ui.composables.ImageContainer
import com.agp.mymoment.ui.composables.ThemedNavBar
import com.agp.mymoment.ui.composables.ThemedTextField
import com.agp.mymoment.ui.composables.UserListView

@Composable
fun SearchScreen(
    navController: NavHostController,
    viewModel: SearchScreenViewModel = hiltViewModel()
) {
    viewModel.updateScreen()
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


        val filteredMap = viewModel.users.filter { it.value.name?.contains(viewModel.searchText) == true || it.value.nickname?.contains(viewModel.searchText) == true }
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

            Row() {
                UserListView(
                    url = pfp,
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

    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(0.dp)
    ) {
        items(viewModel.posts.size) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                Arrangement.Center
            ) {
                ImageContainer(
                    image = viewModel.posts[item].download_link ?: "",
                    contentDescription = "Image $item",
                    modifier = Modifier
                        .size(130.dp)
                        .padding(2.dp)
                )
            }
        }
    }
}