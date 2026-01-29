package mx.dev.cmg.android.biometrics.ui.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import mx.dev.cmg.android.biometrics.ui.feature.splash.vm.SplashSideEffect
import mx.dev.cmg.android.biometrics.ui.feature.splash.vm.SplashViewModel

@Serializable
data object Splash: NavKey

@Serializable
data object Login: NavKey

@Serializable
data object Home: NavKey

@Composable
fun MainNavHost(modifier: Modifier = Modifier) {
    val context = LocalContext.current
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

                val viewModel: SplashViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(Unit) {
                    viewModel.sideEffect.collect { effect ->
                        when(effect) {
                            is SplashSideEffect.NavigateToLogin -> {
                                backStack.clear()
                                backStack.add(Login)
                            }
                            is SplashSideEffect.NavigateToHome -> {
                                backStack.clear()
                                backStack.add(Home)
                            }
                            is SplashSideEffect.ShowBiometricPrompt -> {
                                // TODO Handle biometric prompt navigation if needed
                                Toast.makeText(context, "Show Biometric Prompt", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

                SplashLayout(
                    modifier = Modifier.fillMaxSize(),
                    uiState = uiState
                )
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