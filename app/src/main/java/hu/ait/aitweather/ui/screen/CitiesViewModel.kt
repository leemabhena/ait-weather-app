package hu.ait.aitweather.ui.screen

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.ait.aitweather.data.CitiesDao
import hu.ait.aitweather.data.City
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CitiesViewModel @Inject constructor(private val cityDao: CitiesDao): ViewModel() {

    fun getAllCities(): Flow<List<City>> {
        return cityDao.getAllCities()
    }

    fun addCity(city: City) {
        viewModelScope.launch {
            cityDao.insert(city)
        }
    }

    fun deleteCity(city: City) {
        viewModelScope.launch {
            cityDao.delete(city)
        }
    }

}