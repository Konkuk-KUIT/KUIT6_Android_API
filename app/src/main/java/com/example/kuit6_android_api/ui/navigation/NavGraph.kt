package com.example.kuit6_android_api.ui.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.kuit6_android_api.ui.post.screen.PostCreateScreen
import com.example.kuit6_android_api.ui.post.screen.PostDetailScreen
import com.example.kuit6_android_api.ui.post.screen.PostEditScreen
import com.example.kuit6_android_api.ui.post.screen.PostListScreen

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
            PostListScreen(
                onPostClick = { postId ->
                    navController.navigate(PostDetailRoute(postId))
                },
                onCreatePostClick = {
                    navController.navigate(PostCreateRoute)
                }
            )
        }

        composable<PostDetailRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<PostDetailRoute>()

            PostDetailScreen(
                postId = route.postId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onEditClick = { postId ->
                    navController.navigate(PostEditRoute(postId))
                },
                snackBarState = snackBarState
            )
        }

        composable<PostCreateRoute> {
            PostCreateScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onPostCreated = {
                    navController.popBackStack()
                },
                snackBarState = snackBarState
            )
        }

        composable<PostEditRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<PostEditRoute>()

            PostEditScreen(
                postId = route.postId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onPostUpdated = {
                    navController.popBackStack()
                },
                snackBarState = snackBarState
            )
        }
    }
}
