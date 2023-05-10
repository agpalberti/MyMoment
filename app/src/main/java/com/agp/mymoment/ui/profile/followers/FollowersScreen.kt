package com.agp.mymoment.ui.profile.followers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.agp.mymoment.model.classes.User
import com.agp.mymoment.ui.composables.ThemedNavBar
import com.agp.mymoment.ui.composables.UserListView
import com.agp.mymoment.ui.profile.ProfileScreenViewModel
import com.agp.mymoment.ui.search.SearchScreenViewModel


@Composable
fun FollowersScreen(navController: NavController, userUID: String, viewModel:ProfileScreenViewModel = hiltViewModel(), searchScreenViewModel: SearchScreenViewModel = hiltViewModel()) {

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




        /*

                val currentList = if (selectedTabIndex == 0) viewModel.userData.followers else viewModel.userData.follows
                LazyColumn {
                    items(currentList?.size?: 0) { item->
                        Row() {

                            val followingUser by searchScreenViewModel.amIFollowing(currentList!![item])
                                .collectAsState(initial = false)
                            val following by searchScreenViewModel.isHeFollowing(currentList!![item]).collectAsState(initial = false)
                            val pfp by searchScreenViewModel.getPfp(currentList!![item]).collectAsState(initial = "")
                            val user by searchScreenViewModel.getUser(currentList!![item]).collectAsState(initial = null)

                            UserListView(
                                pfpUrl = pfp,
                                user = user?: User("","", "", emptyList(), emptyList(),
                                    emptyList()
                                ),
                                followingUser = followingUser?:false,
                                following = following?:false,
                                onUserClick = { /*TODO*/ }) {

                            }
                        }

                    }
                }

         */


        val tabs = listOf("Seguidores", "Seguidos")
        var selectedTabIndex by remember { mutableStateOf(0) }
        LazyRow(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceEvenly){
            itemsIndexed(tabs) { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = index == selectedTabIndex,
                    onClick = { selectedTabIndex = index }
                )


            }
        }

    }

}


@Composable
fun FollowersScreenBody() {

}