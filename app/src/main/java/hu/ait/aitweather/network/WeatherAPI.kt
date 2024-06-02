package hu.ait.aitweather.network

import hu.ait.aitweather.data.network.WeatherData
import retrofit2.http.GET
import retrofit2.http.Query

// HOST: https://api.openweathermap.org/
// data/2.5/weather
// ?
// q=Budapest,hu
// &
// units=metric
// &
// appid=f3d694bc3e1d44c1ed5a97bd1120e8fe


interface WeatherAPI {
    @GET("data/2.5/weather")
    suspend fun getWeatherData(@Query("q") cityName: String,
                               @Query("units") units: String,
                               @Query("appid") appID: String
                               ) : WeatherData
}