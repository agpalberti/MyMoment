package com.agp.mymoment.ui.profile.followers

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material.TabRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.agp.mymoment.model.classes.User
import com.agp.mymoment.navigation.Destinations
import com.agp.mymoment.ui.composables.ThemedNavBar
import com.agp.mymoment.ui.composables.UserListView
import com.agp.mymoment.ui.profile.ProfileScreenViewModel
import com.agp.mymoment.ui.search.SearchScreenViewModel


@Composable
fun FollowersScreen(
    navController: NavController,
    userUID: String,
    index: Int,
    viewModel: ProfileScreenViewModel = hiltViewModel()
) {

    viewModel.getUserData(userUID)
    ThemedNavBar(navController = navController, topBarContent = {

        Row(Modifier.fillMaxWidth(0.5f), Arrangement.Start) {
            Text(
                viewModel.userData.nickname ?: "",
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.primary
            )
        }

    }) {

        FollowersScreenBody(navController, index)

    }

}


@Composable
fun FollowersScreenBody(
    navController: NavController,
    index: Int,
    profileScreenViewModel: ProfileScreenViewModel = hiltViewModel(),
    searchScreenViewModel: SearchScreenViewModel = hiltViewModel()
) {
    //todo implementar swipe
    @Composable
    fun TabRowsContent(currentList: List<String>?, item: Int) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(80.dp)
        ) {

            val followingUser by searchScreenViewModel.amIFollowing(currentList!![item])
                .collectAsState(initial = false)
            val following by searchScreenViewModel.isHeFollowing(currentList!![item])
                .collectAsState(initial = false)
            val pfp by searchScreenViewModel.getPfp(currentList!![item])
                .collectAsState(initial = "")
            val user by searchScreenViewModel.getUser(currentList!![item])
                .collectAsState(initial = null)

            UserListView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                pfpUrl = pfp,
                user = user ?: User(
                    "", "", "", emptyList(), emptyList(),
                    emptyList()
                ),
                followingUser = followingUser ?: false,
                following = following ?: false,
                onUserClick = { navController.navigate("${Destinations.ProfileScreen.ruta}/${currentList!![item]}") }) {
            }
        }
    }


    val tabs = listOf("Seguidores", "Seguidos")
    var selectedTabIndex by remember { mutableStateOf(index) }
    val currentList =
        if (selectedTabIndex == 0) profileScreenViewModel.userData.followers else profileScreenViewModel.userData.follows

    Column(Modifier.fillMaxSize()) {

        TabRow(
            modifier = Modifier
                .fillMaxWidth(),
            selectedTabIndex = selectedTabIndex,
            backgroundColor = MaterialTheme.colors.background,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    color = MaterialTheme.colors.primary,
                    height = 2.dp,
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex])
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = index == selectedTabIndex,
                    onClick = { selectedTabIndex = index }
                )
            }
        }

        when (selectedTabIndex) {
            0 -> {

                LazyColumn(Modifier.fillMaxSize()) {
                    items(currentList?.size ?: 0) { item ->
                        TabRowsContent(currentList = currentList, item = item)
                    }
                }
            }
            1 -> {
                LazyColumn(
                    Modifier
                        .fillMaxSize()
                        .padding(vertical = 1.dp)
                ) {
                    items(currentList?.size ?: 0) { item ->
                        TabRowsContent(currentList = currentList, item = item)
                    }
                }
            }
        }
    }
}