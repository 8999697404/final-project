package com.luisenricke.room.converter

import androidx.room.TypeConverter
import java.util.Date

@Suppress("unused")
object Date {
    @TypeConverter
    @JvmStatic
    fun fromTimestamp(value: Long?): Date? = if (value == null) Date() else Date(value)

    @TypeConverter
    @JvmStatic
    fun dateToTimestamp(date: Date?): Long? = date?.time ?: Date().time
}
