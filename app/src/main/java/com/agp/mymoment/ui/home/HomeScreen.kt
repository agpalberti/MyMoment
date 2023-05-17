package com.agp.mymoment.ui.home

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.agp.mymoment.config.DeviceConfig
import com.agp.mymoment.model.classes.Post
import com.agp.mymoment.navigation.Destinations
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

            val interactionSource = remember {
                MutableInteractionSource()
            }

            if (user != null) {

                Row(
                    Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(35.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colors.background)
                            .clickable(
                                indication = null,
                                interactionSource = interactionSource,
                                onClick = {
                                    navController!!.navigate("${Destinations.ProfileScreen.ruta}/${newPosts.keys.elementAt(page)}")
                                }
                            ),
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .matchParentSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                            model = pfp,
                            contentDescription = "Image"
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(text = user!!.nickname ?: "")
                }

                StoryView(user = user!!, stories)


            }


        }

        /*
// Later, scroll to page 2
        scope.launch {
            pagerState.scrollToPage(2)
        }*/
    }
}