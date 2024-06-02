package hu.ait.aitweather.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hu.ait.aitweather.data.AppDatabase
import hu.ait.aitweather.data.CitiesDao
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun provideCityDao(appDatabase: AppDatabase): CitiesDao {
        return appDatabase.cityDao()
    }

    @Provides
    @Singleton
    fun provideCityDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return AppDatabase.getDatabase(appContext)
    }
}