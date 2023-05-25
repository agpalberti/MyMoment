package com.agp.mymoment.ui.profile.followers

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
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
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch


@Composable
fun FollowersScreen(
    navController: NavController,
    userUID: String,
    index: Int,
    viewModel: ProfileScreenViewModel = hiltViewModel()
) {

    //todo arreglar que la lista desaparece por la cara

    LaunchedEffect(Unit) {
        viewModel.getUserData(userUID)
    }

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


@OptIn(ExperimentalPagerApi::class)
@Composable
fun FollowersScreenBody(
    navController: NavController,
    index: Int,
    profileScreenViewModel: ProfileScreenViewModel = hiltViewModel(),
    searchScreenViewModel: SearchScreenViewModel = hiltViewModel()
) {
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
    val pagerState = rememberPagerState(index)

    val coroutineScope = rememberCoroutineScope()

    Column(Modifier.fillMaxSize()) {

        TabRow(
            modifier = Modifier
                .fillMaxWidth(),
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = MaterialTheme.colors.background,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    color = MaterialTheme.colors.primary,
                    height = 2.dp,
                    modifier = Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                )
            }
        ) {
            tabs.forEachIndexed { tabIndex, title ->
                Tab(
                    text = { Text(title) },
                    selected = pagerState.currentPage == tabIndex,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(tabIndex)
                        }
                    }
                )
            }
        }

        HorizontalPager(count = tabs.size, state = pagerState) { page ->

            when (page) {
                0 -> {


                    LazyColumn(Modifier.fillMaxSize()) {

                        val currentList  = profileScreenViewModel.userData.followers
                        items(currentList?.size ?: 0) { item ->
                            if (currentList?.isNotEmpty() == true)
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
                        val currentList  = profileScreenViewModel.userData.follows
                        items(currentList?.size ?: 0) { item ->
                            if (currentList?.isNotEmpty() == true) TabRowsContent(
                                currentList = currentList,
                                item = item
                            )
                        }
                    }
                }
            }
        }
    }
}