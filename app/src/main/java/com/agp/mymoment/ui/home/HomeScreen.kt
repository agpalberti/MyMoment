package com.agp.mymoment.ui.home

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.agp.mymoment.R
import com.agp.mymoment.config.DeviceConfig
import com.agp.mymoment.model.classes.Post
import com.agp.mymoment.ui.composables.StoryView
import com.agp.mymoment.ui.composables.ThemedNavBar
import com.agp.mymoment.ui.theme.getLogoId
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController: NavHostController) {
    ThemedNavBar(navController = navController, topBarContent = {

        Image(painter = painterResource(getLogoId()), contentDescription = "logo")

    }) {
        HomeScreenBody(navController)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalPagerApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreenBody(
    navController: NavHostController? = null,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val pagerState = rememberPagerState()

        val thisUser by viewModel.getUserData().collectAsState(initial = null)

        Log.i("Home", "thisUser: $thisUser")
        val newPosts: MutableMap<String, MutableList<Post>> = mutableMapOf()

        thisUser?.follows?.forEach { uid ->
            Log.i("Home", "person: $uid")
            val user by viewModel.getUserData(uid).collectAsState(initial = null)
            user?.posts?.forEach { post ->
                Log.i("Home", "post: $post")
                if (DeviceConfig.isPostOnStories(post.date)) {
                    if (newPosts.containsKey(uid))
                        newPosts[uid]?.add(post)
                    else
                        newPosts[uid] = mutableListOf(post)
                }
            }
        }
        Log.i("Home", "newPosts: ${newPosts.keys}")


        if (newPosts.isNotEmpty()) {
            HorizontalPager(count = newPosts.size, state = pagerState) { page ->

                val user by viewModel.getUserData(newPosts.keys.elementAt(page))
                    .collectAsState(initial = null)
                val stories = newPosts[newPosts.keys.elementAt(page)]
                val scope = rememberCoroutineScope()
                var pfp by remember { mutableStateOf("") }

                LaunchedEffect(user) {
                    scope.launch {
                        pfp = viewModel.getPFP(newPosts.keys.elementAt(page))
                    }
                }

                if (user != null) {



                    StoryView(user = user!!, stories, newPosts.keys.elementAt(page), pfp , navController = navController)

                }

            }

        } else {
            Text(text = stringResource(id = R.string.no_stories))
        }

        /*
// Later, scroll to page 2
        scope.launch {
            pagerState.scrollToPage(2)
        }*/
    }
}