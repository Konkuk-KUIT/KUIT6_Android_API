package com.example.kuit6_android_api.ui.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.kuit6_android_api.ui.post.screen.PostCreateScreen
import com.example.kuit6_android_api.ui.post.screen.PostDetailScreen
import com.example.kuit6_android_api.ui.post.screen.PostEditScreen
import com.example.kuit6_android_api.ui.post.screen.PostListScreen
import com.example.kuit6_android_api.ui.post.viewmodel.PostCreateViewModel
import com.example.kuit6_android_api.ui.post.viewmodel.PostDetailViewModel
import com.example.kuit6_android_api.ui.post.viewmodel.PostEditViewModel
import com.example.kuit6_android_api.ui.post.viewmodel.PostListViewModel
import com.example.kuit6_android_api.ui.post.viewmodel.PostViewModelFactory

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: Any = PostListRoute,
    snackBarState: SnackbarHostState
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<PostListRoute> {
            val listViewModel: PostListViewModel = viewModel(
                factory = PostViewModelFactory { PostListViewModel(it) }
            )

            PostListScreen(
                onPostClick = { postId ->
                    navController.navigate(PostDetailRoute(postId))
                },
                onCreatePostClick = {
                    navController.navigate(PostCreateRoute)
                },
                viewModel = listViewModel
            )
        }

        composable<PostDetailRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<PostDetailRoute>()
            val detailViewModel: PostDetailViewModel = viewModel(
                factory = PostViewModelFactory { PostDetailViewModel(it) }
            )

            val editViewModel: PostEditViewModel = viewModel(
                factory = PostViewModelFactory { PostEditViewModel(it) }
            )
            PostDetailScreen(
                postId = route.postId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onEditClick = { postId ->
                    navController.navigate(PostEditRoute(postId))
                },
                snackBarState = snackBarState,
                detailViewModel=detailViewModel,
                editViewModel=editViewModel
            )
        }

        composable<PostCreateRoute> {
            val createViewModel: PostCreateViewModel = viewModel(
                factory = PostViewModelFactory { PostCreateViewModel(it) }
            )

            PostCreateScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onPostCreated = {
                    navController.popBackStack()
                },
                snackBarState = snackBarState,
                postCreateViewModel = createViewModel
            )
        }

        composable<PostEditRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<PostEditRoute>()
            val detailViewModel: PostDetailViewModel = viewModel(
                factory = PostViewModelFactory { PostDetailViewModel(it) }
            )

            val editViewModel: PostEditViewModel = viewModel(
                factory = PostViewModelFactory { PostEditViewModel(it) }
            )

            PostEditScreen(
                postId = route.postId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onPostUpdated = {
                    navController.popBackStack()
                },
                snackBarState = snackBarState,
                editViewModel = editViewModel,
                detailViewModel = detailViewModel
            )
        }
    }
}
