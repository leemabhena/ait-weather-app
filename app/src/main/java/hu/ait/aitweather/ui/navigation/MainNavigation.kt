package hu.ait.aitweather.ui.navigation

sealed class MainNavigation(val route: String) {
    object CitiesScreen: MainNavigation(route = "cities")
    object WeatherDetailsScreen: MainNavigation(route = "weather_details/{city}") {
        fun getRoute(city: String): String {
            return "weather_details/$city"
        }
    }
}