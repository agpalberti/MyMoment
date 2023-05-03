package com.agp.mymoment.ui.search

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
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
import com.agp.mymoment.navigation.Destinations
import com.agp.mymoment.ui.composables.ImageContainer
import com.agp.mymoment.ui.composables.ThemedNavBar
import com.agp.mymoment.ui.composables.ThemedTextField

@Composable
fun SearchScreen(
    navController: NavHostController,
    viewModel: SearchScreenViewModel = hiltViewModel()
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


        if (viewModel.searchText.isNotBlank()){
            ExploreScreenBody(navController)
        }
        else{
            SearchScreenBody(navController)
        }

    }

}

@Composable
@Preview
fun ExploreScreenBody(
    navController: NavHostController? = null,
    viewModel: SearchScreenViewModel = hiltViewModel()
) {


}
@Composable
@Preview
fun SearchScreenBody(
    navController: NavHostController? = null,
    viewModel: SearchScreenViewModel = hiltViewModel()
){
    viewModel.updateScreen()

    Log.i("Buscar", "${viewModel.posts}")

    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(0.dp)
    ) {
        items(viewModel.posts.size){
                item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                Arrangement.Center
            ){
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