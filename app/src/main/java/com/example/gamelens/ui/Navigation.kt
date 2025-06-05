package com.example.gamelens.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.gamelens.ui.screens.DetailScreen
import com.example.gamelens.ui.screens.SearchScreen
import com.example.gamelens.ui.viewmodel.MainViewModel
import kotlinx.serialization.Serializable

class Routes {
    @Serializable
    data object SearchRoute

    @Serializable
    data class DetailRoute(val id: Int)
}

@Composable
fun AppNavigation(modifier:Modifier = Modifier) {

    val navHostController : NavHostController = rememberNavController()
    val mainViewModel : MainViewModel = viewModel()

    NavHost(navController = navHostController, startDestination = Routes.SearchRoute,
        modifier = modifier) {

        composable<Routes.SearchRoute> {
            SearchScreen( onPictureClick = { pictureBean ->
                navHostController.navigate(Routes.DetailRoute(pictureBean.id))
            }, viewModel = mainViewModel )
        }

        composable<Routes.DetailRoute> {
            val detailRoute = it.toRoute<Routes.DetailRoute>()
            val pictureBean = mainViewModel.dataList.collectAsStateWithLifecycle().value.first { it.id == detailRoute.id }

            DetailScreen(pictureBean = pictureBean,
                navHostController = navHostController,
                viewModel = mainViewModel
            )
        }
    }
}
