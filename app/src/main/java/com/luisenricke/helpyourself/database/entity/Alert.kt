package com.luisenricke.helpyourself.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.luisenricke.helpyourself.database.entity.Alert.SCHEMA.ID
import com.luisenricke.helpyourself.database.entity.Alert.SCHEMA.LATITUDE
import com.luisenricke.helpyourself.database.entity.Alert.SCHEMA.LONGITUDE
import com.luisenricke.helpyourself.database.entity.Alert.SCHEMA.TABLE
import com.luisenricke.helpyourself.database.entity.Alert.SCHEMA.TIMESTAMP
import java.util.Date

@Entity(tableName = TABLE)
data class Alert(
    @ColumnInfo(name = LATITUDE) var latitude: Double = 0.0,
    @ColumnInfo(name = LONGITUDE) var longitude: Double = 0.0,
    @ColumnInfo(name = TIMESTAMP) var timestamp: Date = Date(System.currentTimeMillis()),
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = ID) val id: Long = 0
) {

    object SCHEMA {
        const val TABLE = "Alert"
        const val ID = "id"
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
        const val TIMESTAMP = "timestamp"
    }

    override fun toString(): String {
        return """/* ${System.lineSeparator()}
            |id:         $id
            |latitude:   $latitude
            |longitude:  $longitude
            |timestamp:  ${timestamp.toString()}
        ${System.lineSeparator()} */""".trimMargin()
    }
}
