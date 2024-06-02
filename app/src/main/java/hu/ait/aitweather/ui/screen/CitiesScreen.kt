package hu.ait.aitweather.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.ait.aitweather.R
import hu.ait.aitweather.data.City
import hu.ait.aitweather.ui.navigation.MainNavigation
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitiesScreen(
    citiesViewModel: CitiesViewModel = hiltViewModel(),
    onNavigateToDetails: (String) -> Unit = {}
) {

    var isDialogOpen by remember {
        mutableStateOf(false)
    }

    val cities by citiesViewModel.getAllCities().collectAsState(emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                Text(
                    text = stringResource(id = R.string.app_name),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { isDialogOpen = true }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        },
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            LazyColumn {
                items(cities) { city ->
                    CitiesListItem(city = city, citiesViewModel = citiesViewModel) { cityName ->
                        onNavigateToDetails(MainNavigation.WeatherDetailsScreen.getRoute(cityName))
                    }
                }
            }
        }

        if (isDialogOpen) {
            AddCityDialog(citiesViewModel) {
                isDialogOpen = false
            }
        }
    }
}

@Composable
fun CitiesListItem(
    city: City,
    citiesViewModel: CitiesViewModel,
    onNavigateToCity: (String) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable {
                onNavigateToCity(city.city)
            }
    ) {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            val (cityContainer, date, cancel) = createRefs()

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .constrainAs(cityContainer) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        bottom.linkTo(date.top)
                    }
            ) {
                Icon(imageVector = Icons.Default.LocationOn, contentDescription = null)
                Text(
                    text = city.city,
                    style = MaterialTheme.typography.headlineSmall,
                )
            }

            val currentDateTimeString =
                SimpleDateFormat("EEE, MMMM d hh:mm a", Locale.getDefault()).format(Date())

            Text(
                text = currentDateTimeString,
                modifier = Modifier
                    .constrainAs(date) {
                        top.linkTo(cityContainer.bottom)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(top = 12.dp)
            )

            IconButton(
                onClick = { citiesViewModel.deleteCity(city) },
                modifier = Modifier
                    .constrainAs(cancel) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
            ) {
                Icon(imageVector = Icons.Default.Close, contentDescription = null)
            }


        }

    }
}

@Composable
fun AddCityDialog(
    citiesViewModel: CitiesViewModel,
    onDismissRequest: () -> Unit = {}
) {
    var cityName by remember {
        mutableStateOf("")
    }
    
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(20.dp)
        ) {

            Text(
                text = "Add New City",
            )
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                value = cityName, 
                onValueChange = {cityName = it},
                label = {
                    Text(text = "City")
                },
                singleLine = true
            )
            Row {
                TextButton(
                    onClick = { onDismissRequest() },
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(text = "Cancel")
                }
                
                TextButton(
                    onClick = {
                        citiesViewModel.addCity(City(city = cityName))
                        onDismissRequest()
                              },
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(text = "Add")
                }
            }
        }
    }

}