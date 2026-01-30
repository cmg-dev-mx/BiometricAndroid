package mx.dev.cmg.android.biometrics.ui.navigation

import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import mx.dev.cmg.android.biometrics.helper.biometric.BiometricPromptManager
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

                val activity = context as? AppCompatActivity
                val biometricPromptManager = remember(activity) {
                    activity?.let { BiometricPromptManager(it) }
                }

                LaunchedEffect(biometricPromptManager) {
                    biometricPromptManager?.promptResults?.collect { result ->
                        when (result) {
                            is BiometricPromptManager.BiometricResult.AuthenticationSuccess -> {
                                backStack.clear()
                                backStack.add(Home)
                            }

                            is BiometricPromptManager.BiometricResult.AuthenticationError -> {
                                backStack.clear()
                                backStack.add(Login)
                            }

                            BiometricPromptManager.BiometricResult.AuthenticationFailed -> {
                                Toast.makeText(
                                    context,
                                    "Autenticación fallida. Intenta de nuevo.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            BiometricPromptManager.BiometricResult.AuthenticationNotSet -> {
                                backStack.clear()
                                backStack.add(Home)
                            }

                            BiometricPromptManager.BiometricResult.HardwareUnavailable,
                            BiometricPromptManager.BiometricResult.FeatureUnavailable -> {
                                backStack.clear()
                                backStack.add(Home)
                            }
                        }
                    }
                }

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
                                biometricPromptManager?.showBiometricPrompt(
                                    title = "Autenticación biométrica",
                                    description = "Usa tu huella o rostro para acceder"
                                ) ?: run {
                                    Toast.makeText(
                                        context,
                                        "Error al inicializar biometría",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    backStack.clear()
                                    backStack.add(Login)
                                }
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

                val enrollLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult(),
                    onResult = {
                        Toast.makeText(
                            context,
                            "Por favor, intenta habilitar la biometría nuevamente",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )

                LaunchedEffect(Unit) {
                    viewModel.sideEffect.collect { effect ->
                        when (effect) {
                            is HomeSideEffect.LogoutSuccess -> {
                                backStack.clear()
                                backStack.add(Login)
                            }

                            HomeSideEffect.ToggleBiometricSuccess -> {
                                Toast.makeText(
                                    context,
                                    "Biometría actualizada correctamente",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            HomeSideEffect.ShowBiometricEnrollment -> {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                    val enrollIntent =
                                        Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                                            putExtra(
                                                Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                                                BIOMETRIC_STRONG
                                            )
                                        }
                                    enrollLauncher.launch(enrollIntent)
                                } else {
                                    val enrollIntent = Intent(Settings.ACTION_SECURITY_SETTINGS)
                                    enrollLauncher.launch(enrollIntent)
                                }
                            }

                            is HomeSideEffect.ShowError -> {
                                Toast.makeText(context, effect.message, Toast.LENGTH_LONG).show()
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