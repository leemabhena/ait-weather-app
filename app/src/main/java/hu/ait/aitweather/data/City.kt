package hu.ait.aitweather.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class City(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val city: String,
)
