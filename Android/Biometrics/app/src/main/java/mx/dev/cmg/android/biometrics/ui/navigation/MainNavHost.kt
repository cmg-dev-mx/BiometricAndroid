package mx.dev.cmg.android.biometrics.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable
import mx.dev.cmg.android.biometrics.ui.feature.home.layout.HomeLayout
import mx.dev.cmg.android.biometrics.ui.feature.login.layout.LoginLayout
import mx.dev.cmg.android.biometrics.ui.feature.splash.layout.SplashLayout

@Serializable
data object Splash: NavKey

@Serializable
data object Login: NavKey

@Serializable
data object Home: NavKey

@Composable
fun MainNavHost(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(Splash)

    NavDisplay(
        modifier = modifier,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        backStack = backStack,
        entryProvider = entryProvider {
            entry<Splash> {
                SplashLayout(modifier = Modifier.fillMaxSize())
            }
            entry<Login> {
                LoginLayout(modifier = Modifier.fillMaxSize())
            }
            entry<Home> {
                HomeLayout(modifier = Modifier.fillMaxSize())
            }
        }
    )
}