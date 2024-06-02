package hu.ait.aitweather.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.ait.aitweather.data.network.WeatherData
import hu.ait.aitweather.network.WeatherAPI
import javax.inject.Inject

@HiltViewModel
class WeatherDetailsViewModel @Inject constructor(private val weatherAPI: WeatherAPI): ViewModel() {

    var weatherUIState: WeatherUIState by mutableStateOf(WeatherUIState.Init)

    suspend fun getWeatherData(cityName: String, units: String, appID: String) {
        weatherUIState = WeatherUIState.Loading
        try {
            val result = weatherAPI.getWeatherData(cityName, units, appID)
            weatherUIState = WeatherUIState.Success(result)
        } catch (e: Exception) {
            weatherUIState = WeatherUIState.Error(e.message!!)
        }

    }
}

sealed interface WeatherUIState {
    object Init : WeatherUIState
    object Loading : WeatherUIState
    data class Success(val weatherData: WeatherData) : WeatherUIState
    data class Error(val errorMsg: String) : WeatherUIState
}