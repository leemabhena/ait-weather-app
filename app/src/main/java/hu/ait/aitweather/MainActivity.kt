package hu.ait.aitweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import hu.ait.aitweather.ui.navigation.MainNavigation
import hu.ait.aitweather.ui.screen.CitiesScreen
import hu.ait.aitweather.ui.screen.WeatherDetailsScreen
import hu.ait.aitweather.ui.theme.AITWeatherTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AITWeatherTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation()
                }
            }
        }
    }
}

@Composable
fun Navigation(
    startDestination: String = MainNavigation.CitiesScreen.route,
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(MainNavigation.CitiesScreen.route) {
            CitiesScreen(onNavigateToDetails = navController::navigate)
        }

        composable(
            MainNavigation.WeatherDetailsScreen.route,
            arguments = listOf(navArgument("city") { type = NavType.StringType })
        ) {backStackEntry ->
            WeatherDetailsScreen(
                cityName = backStackEntry.arguments?.getString("city") ?: "",
                onNavigateToCitiesList = {
                    navController.navigate(MainNavigation.CitiesScreen.route)
                }
            )
        }
    }

}