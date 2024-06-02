package hu.ait.aitweather.ui.screen


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import hu.ait.aitweather.R
import hu.ait.aitweather.util.Settings
import hu.ait.aitweather.util.convertTimestampToTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherDetailsScreen(
    cityName: String,
    weatherDetailsViewModel: WeatherDetailsViewModel = hiltViewModel(),
    onNavigateToCitiesList: () -> Unit = {}
    ) {

    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        val settings = Settings()
        weatherDetailsViewModel.getWeatherData(cityName, settings.units, settings.appID)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = cityName,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(imageVector = Icons.Default.LocationOn, contentDescription = null)
                    }
                        },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                navigationIcon = {
                    IconButton(onClick = { onNavigateToCitiesList() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) {
        Column(modifier = Modifier
            .padding(it)
            .verticalScroll(scrollState)
            .fillMaxSize()) {

            when(weatherDetailsViewModel.weatherUIState) {
                is WeatherUIState.Init -> {}
                is WeatherUIState.Loading -> CircularProgressIndicator()
                is WeatherUIState.Success -> {
                    val weatherInfo = (weatherDetailsViewModel.weatherUIState as WeatherUIState.Success)
                    MainContentContainer(
                        temperature = weatherInfo.weatherData.main?.temp,
                        description = weatherInfo.weatherData.weather?.get(0)?.description,
                        minTemp = weatherInfo.weatherData.main?.tempMin,
                        maxTemp = weatherInfo.weatherData.main?.tempMax,
                        feelsLike =  weatherInfo.weatherData.main?.feelsLike,
                        iconUrl = "https://openweathermap.org/img/w/${weatherInfo.weatherData.weather?.get(0)?.icon}.png",
                    )
                    WeatherConditionGrid(
                        humidity = weatherInfo.weatherData.main?.humidity,
                        wind = weatherInfo.weatherData.wind?.speed,
                        visibility = weatherInfo.weatherData.visibility,
                        pressure = weatherInfo.weatherData.main?.pressure
                    )
                    SunriseSunset(
                        sunrise = convertTimestampToTime(weatherInfo.weatherData.sys?.sunrise),
                        sunset = convertTimestampToTime(weatherInfo.weatherData.sys?.sunset)
                    )

                    // Google Maps here
                    WeatherMapView(
                        lat = weatherInfo.weatherData.coord?.lat,
                        long = weatherInfo.weatherData.coord?.lat,
                        cityName = cityName)

                }
                is WeatherUIState.Error -> {
                    Text(text = "Error: ${(weatherDetailsViewModel.weatherUIState as WeatherUIState.Error).errorMsg}")
                }
            }

        }
    }


}

@Composable
fun MainContentContainer(
    temperature: Double?,
    description: String?,
    minTemp: Double?,
    maxTemp: Double?,
    feelsLike: Double?,
    iconUrl: String?
) {
    ConstraintLayout(
        modifier = Modifier.fillMaxWidth()
    ) {
        val startGuide = createGuidelineFromStart(0.05f)
        val endGuide = createGuidelineFromEnd(0.05f)
        
        val (temp, desc, minMax, icon) = createRefs()
        
        Text(
            text = "$temperature\u00B0",
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Black,
            modifier = Modifier
                .constrainAs(temp) {
                    top.linkTo(parent.top)
                    start.linkTo(startGuide)
                    bottom.linkTo(desc.top)
                }
        )

        Text(
            text = description ?: "",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight(400),
            modifier = Modifier
                .constrainAs(desc) {
                    top.linkTo(temp.bottom)
                    start.linkTo(startGuide)
                    bottom.linkTo(minMax.top)
                }
        )

        Text(
            text = "$maxTemp° / $minTemp° Feels like $feelsLike°",
            fontSize = 20.sp,
            fontWeight = FontWeight(300),
            modifier = Modifier
                .constrainAs(minMax) {
                    top.linkTo(desc.bottom)
                    start.linkTo(startGuide)
                }
                .padding(top = 24.dp)
        )

        AsyncImage(
            model = iconUrl,
            contentDescription = null,
            modifier = Modifier
                .constrainAs(icon) {
                    top.linkTo(parent.top)
                    end.linkTo(endGuide)
                    bottom.linkTo(minMax.top)
                }
                .size(80.dp)
        )
    }
}


@Composable
fun WeatherConditionItem(
    image: Int,
    title: String,
    text: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                modifier = Modifier.size(25.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = title,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 18.sp
            )
        }

        Text(
            text = text,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 10.dp)
        )

    }
}

@Composable
fun WeatherConditionGrid(
    humidity: Int?,
    wind: Double?,
    visibility: Int?,
    pressure: Int?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp, start = 8.dp, end = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            WeatherConditionItem(
                image = R.drawable.humidity,
                title = "Humidity",
                text = "$humidity%",
                modifier = Modifier.weight(1f)
            )
            WeatherConditionItem(
                image = R.drawable.wind,
                title = "Wind",
                text = "${wind}km/h",
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            WeatherConditionItem(
                image = R.drawable.visibility,
                title = "Visibility",
                text = "$visibility",
                modifier = Modifier.weight(1f)
            )
            WeatherConditionItem(
                image = R.drawable.pressure,
                title = "Pressure",
                text = "$pressure mb",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun SunriseSunset(
    sunrise: String? = "07:30 AM",
    sunset: String? = "07:30 PM"
) {
    Column(
        modifier = Modifier
            .padding(vertical = 32.dp, horizontal = 16.dp)
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(32.dp)
            )
    ){
        Image(
            painter = painterResource(id = R.drawable.sunrise),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
        Row(
            horizontalArrangement = Arrangement.Absolute.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
        ) {
            Text(
                text = sunrise?: "",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(
                text = sunset?: "",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
                )
        }
    }
}

@Composable
fun WeatherMapView(lat: Double?, long: Double?, cityName: String?) {

    val defaultLat = 47.498  // Default latitude
    val defaultLong = 19.039  // Default longitude
    val zoomLevel = 20f  // Higher zoom level for a closer view

    var cameraState = rememberCameraPositionState {
        CameraPosition.fromLatLngZoom(
            LatLng(lat ?: defaultLat, long ?: defaultLong), zoomLevel
        )
    }

    var uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                zoomControlsEnabled = true,
                zoomGesturesEnabled = true,
            )
        )
    }
    var mapProperties by remember {
        mutableStateOf(
            MapProperties(
                mapType = MapType.NORMAL,
                isTrafficEnabled = true
            )
        )
    }

    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(16.dp),
        cameraPositionState = cameraState,
        uiSettings = uiSettings,
        properties = mapProperties,
    ) {
        Marker(
            state = MarkerState(position = LatLng(lat ?: defaultLat, long ?: defaultLong)),
            title = cityName,
            snippet = "Weather in $cityName",
            draggable = true,
            alpha = 0.5f
        )
    }
}
