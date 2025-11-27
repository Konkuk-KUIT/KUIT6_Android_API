package com.example.kuit6_android_api.ui.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.kuit6_android_api.ui.post.screen.LoginScreen
import com.example.kuit6_android_api.ui.post.screen.PostCreateScreen
import com.example.kuit6_android_api.ui.post.screen.PostDetailScreen
import com.example.kuit6_android_api.ui.post.screen.PostEditScreen
import com.example.kuit6_android_api.ui.post.screen.PostListScreen
import com.example.kuit6_android_api.ui.post.viewmodel.LoginViewModel
import com.example.kuit6_android_api.ui.post.viewmodel.PostCreateViewModel
import com.example.kuit6_android_api.ui.post.viewmodel.PostDetailViewModel
import com.example.kuit6_android_api.ui.post.viewmodel.PostEditViewModel
import com.example.kuit6_android_api.ui.post.viewmodel.PostListViewModel
import com.example.kuit6_android_api.ui.post.viewmodel.loginViewModelFactory
import com.example.kuit6_android_api.ui.post.viewmodel.postViewModelFactory

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
        composable<PostListRoute> { backStackEntry ->
//            val viewModel = viewModel<PostListViewModel>(
//                factory = PostViewModelFactory.Factory
//            )
            PostListScreen(
                onPostClick = { postId ->
                    navController.navigate(PostDetailRoute(postId))
                },
                onCreatePostClick = {
                    navController.navigate(PostCreateRoute)
                },
                onLoginClick = {
                    navController.navigate(LoginRoute)
                },
                viewModel = viewModel(factory = postViewModelFactory {
                    PostListViewModel(it)
                })
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
                snackBarState = snackBarState,
                viewModel = viewModel(factory = postViewModelFactory {
                    PostDetailViewModel(
                        it,
                        route.postId
                    )
                })
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
                snackBarState = snackBarState,
                viewModel = viewModel(factory = postViewModelFactory {
                    PostCreateViewModel(it)
                })
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
                snackBarState = snackBarState,
                viewModel = viewModel(factory = postViewModelFactory {
                    PostEditViewModel(
                        it,
                        route.postId
                    )
                })
            )
        }

        composable<LoginRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<LoginRoute>()

            LoginScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                viewModel = viewModel(factory = loginViewModelFactory { loginRepo, tokenRepo ->
                    LoginViewModel(
                        loginRepository = loginRepo,
                        tokenRepository = tokenRepo
                    )
                })
            )
        }
    }
}
