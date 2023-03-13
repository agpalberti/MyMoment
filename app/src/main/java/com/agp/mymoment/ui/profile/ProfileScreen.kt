package com.agp.mymoment.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.agp.mymoment.R
import com.agp.mymoment.navigation.ThemedNavBar
import com.agp.mymoment.ui.theme.Shapes

@Composable
fun ProfileScreen(navController: NavHostController) {
    ProfileScreenBody(navController)
}

@Composable
@Preview
fun ProfileScreenBody(
    navController: NavHostController? = null,
    viewModel: ProfileScreenViewModel = hiltViewModel()
) {
    ThemedNavBar(navController = navController!!, "klayssu") {

        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {

            Divider(color = MaterialTheme.colors.onSecondary, thickness = 0.dp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
            ) {
                Row(Modifier.fillMaxHeight(0.25f)) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colors.onSecondary)
                    ) {

                        Box {
                            Image(
                                painter = painterResource(R.drawable.test2),
                                contentDescription = "Banner",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .height(175.dp)
                                    .background(MaterialTheme.colors.onSecondary)
                            )
                            Divider(
                                color = MaterialTheme.colors.primary,
                                thickness = 1.dp,
                                modifier = Modifier.align(Alignment.BottomCenter)
                            )
                        }
                    }
                }
            }
            
            //region BODY
            Column(
                Modifier
                    .fillMaxSize()

                    .offset(y = (-80).dp)) {

                //region PFP
                Row(
                    Modifier
                        .height(IntrinsicSize.Max)
                        .padding(horizontal = 25.dp)
                        ){
                    Image(
                        painter = painterResource(R.drawable.test),
                        contentDescription = "Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                    )

                }
                
                Row(Modifier.padding(horizontal = 15.dp)){
                    Column {
                        Spacer(modifier = Modifier.size(10.dp))
                        Text(text = "Alejandro González Parra", style = MaterialTheme.typography.subtitle1, fontWeight = FontWeight.Bold)
                        Text(text = "Biografia", style = MaterialTheme.typography.body1)
                    }
                    
                }
                //endregion
                
                
                
            }
            //endregion
            
            
            

        }
    }
}