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
import mx.dev.cmg.android.biometrics.ui.feature.home.vm.HomeSideEffect
import mx.dev.cmg.android.biometrics.ui.feature.home.vm.HomeViewModel
import mx.dev.cmg.android.biometrics.ui.feature.login.layout.LoginLayout
import mx.dev.cmg.android.biometrics.ui.feature.login.vm.LoginSideEffect
import mx.dev.cmg.android.biometrics.ui.feature.login.vm.LoginViewModel
import mx.dev.cmg.android.biometrics.ui.feature.splash.layout.SplashLayout
import mx.dev.cmg.android.biometrics.ui.feature.splash.vm.SplashSideEffect
import mx.dev.cmg.android.biometrics.ui.feature.splash.vm.SplashViewModel

@Serializable
data object Splash : NavKey

@Serializable
data object Login : NavKey

@Serializable
data object Home : NavKey

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
                        when (effect) {
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
                                Toast.makeText(context, "Show Biometric Prompt", Toast.LENGTH_SHORT)
                                    .show()
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
                val viewModel: LoginViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(Unit) {
                    viewModel.sideEffect.collect { effect ->
                        when (effect) {
                            is LoginSideEffect.NavigateToHome -> {
                                backStack.clear()
                                backStack.add(Home)
                            }

                            is LoginSideEffect.ErrorLoggingIn -> {
                                Toast.makeText(context, effect.error, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

                LoginLayout(
                    modifier = Modifier.fillMaxSize(),
                    uiState = uiState,
                    onEvent = viewModel::onEvent
                )
            }

            entry<Home> {
                val viewModel: HomeViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(Unit) {
                    viewModel.sideEffect.collect { effect ->
                        when (effect) {
                            is HomeSideEffect.LogoutSuccess -> {
                                backStack.clear()
                                backStack.add(Login)
                            }

                            HomeSideEffect.ToggleBiometricSuccess -> {
                                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

                HomeLayout(
                    modifier = Modifier.fillMaxSize(),
                    uiState = uiState,
                    onEvent = viewModel::onEvent
                )
            }
        }
    )
}