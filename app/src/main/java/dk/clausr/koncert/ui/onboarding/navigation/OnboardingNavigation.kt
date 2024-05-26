package dk.clausr.koncert.ui.onboarding.navigation

import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import dk.clausr.koncert.MainActivity
import dk.clausr.koncert.ui.chat.navigation.navigateToChatRoom
import dk.clausr.koncert.ui.home.navigation.HomeDestination
import dk.clausr.koncert.ui.onboarding.chatroom.JoinOrCreateChatRoomRoute
import dk.clausr.koncert.ui.onboarding.username.CreateUserRoute
import dk.clausr.koncert.utils.extensions.collectWithLifecycle
import timber.log.Timber

const val CREATE_USER_ROUTE = "user_route"
const val JOIN_CHAT_ROOM_ROUTE = "join_chat_room_route"

fun NavGraphBuilder.onboardingGraph(
    navHostController: NavHostController,
) {
    composable(route = CREATE_USER_ROUTE) {
        CreateUserRoute(
            modifier = Modifier.fillMaxSize(),
            navController = navHostController,
        )
    }

    composable(route = JOIN_CHAT_ROOM_ROUTE) {
        JoinOrCreateChatRoomRoute(
            modifier = Modifier.fillMaxSize(),
            onCreateClicked = {
                Timber.d("Navigate to $it")
                navHostController.navigateToChatRoom(it) {
                    navOptions {
                        popUpTo(CREATE_USER_ROUTE) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            },
            onSkipClicked = {
                val context = navHostController.context
                val intent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    putExtra(MainActivity.NEW_SIGN_IN, true)
                }
                context.startActivity(intent)
            },
        )
    }

}

@Composable
fun OnboardingNavigation(
    viewModel: OnboardingViewModel,
    navController: NavController,
) {
    viewModel.viewEffects.collectWithLifecycle { viewEffect ->
        when (viewEffect) {
            OnboardingViewEffect.NavigateToChatOverview -> navController.navigate(HomeDestination.route)
            is OnboardingViewEffect.NavigateToChatRoom -> Unit//navController.navigate()
            OnboardingViewEffect.NavigateToJoinChatRoom -> navController.navigate(
                JOIN_CHAT_ROOM_ROUTE
            )
        }
    }
}