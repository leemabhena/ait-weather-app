# AITWeather

AITWeather is an Android application that shows the weather details of a city. The app uses Retrofit to fetch weather data from a weather API and Room database to store the results for offline access.

![AITWeather Demo](./weather-demo.gif)

## Features

- Display current weather of a selected city
- Fetch weather data using Retrofit from a weather API
- Store weather data locally using Room database for offline access
- Clean and intuitive user interface
- Supports light and dark themes

## Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/yourusername/AITWeather.git
    ```
2. Open the project in Android Studio.
3. Build the project and run it on an emulator or a physical device.

## Usage

1. Launch the AITWeather app.
2. Select a city to view its weather details.
3. The app will fetch the latest weather information from the API and display it.
4. The city is saved locally in the room database.

## Dependencies

- Retrofit: For making API calls
- Room: For local database storage
- Hilt: For dependency injection
- LiveData and ViewModel: For managing UI-related data lifecycle-conscious way
- Material Components: For modern UI design

## API

This app uses [OpenWeatherMap API](https://openweathermap.org/api) to fetch weather data. You need to obtain an API key from OpenWeatherMap and add it to your project.


