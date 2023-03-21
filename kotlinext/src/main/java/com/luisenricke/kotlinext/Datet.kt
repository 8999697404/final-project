package com.luisenricke.kotlinext

import java.text.SimpleDateFormat
import java.util.*

const val PATTERN_TIME = "kk:mm:ss"
const val PATTERN_DATE_TIME = "dd/MM/yyyy kk:mm:ss"
const val PATTERN_DATE = "dd/MM/yyyy"
const val PATTERN_EXTENDED = "dd MMMM yyyy kk:mm:ss"

fun Long.formatDate(): String = SimpleDateFormat(PATTERN_DATE, Locale.getDefault()).format(Date(this))

fun Long.formatTime(): String = SimpleDateFormat(PATTERN_TIME, Locale.getDefault()).format(Date(this))

fun Long.formatDateTime(): String = SimpleDateFormat(PATTERN_DATE_TIME, Locale.getDefault()).format(Date(this))

fun Long.formatDateTimeExtended(): String = SimpleDateFormat(PATTERN_EXTENDED, Locale.getDefault()).format(Date(this))

fun Date.addMinute(i: Int): Date? {
    val cal: Calendar = Calendar.getInstance()
    cal.time = this
    cal.add(Calendar.MINUTE, i)
    return cal.time
}

fun Date.addHour(i: Int): Date? {
    val cal: Calendar = Calendar.getInstance()
    cal.time = this
    cal.add(Calendar.HOUR_OF_DAY, i)
    return cal.time
}

fun Date.addDay(i: Int): Date? {
    val cal: Calendar = Calendar.getInstance()
    cal.time = this
    cal.add(Calendar.DAY_OF_YEAR, i)
    return cal.time
}

fun Date.addMonth(i: Int): Date? {
    val cal: Calendar = Calendar.getInstance()
    cal.time = this
    cal.add(Calendar.MONTH, i)
    return cal.time
}

fun Date.addYear(i: Int): Date? {
    val cal: Calendar = Calendar.getInstance()
    cal.time = this
    cal.add(Calendar.YEAR, i)
    return cal.time
}

fun Date.subMinute(i: Int): Date? {
    val cal: Calendar = Calendar.getInstance()
    cal.time = this
    cal.add(Calendar.MINUTE, i * -1)
    return cal.time
}

fun Date.subHour(i: Int): Date? {
    val cal: Calendar = Calendar.getInstance()
    cal.time = this
    cal.add(Calendar.HOUR_OF_DAY, i * -1)
    return cal.time
}

fun Date.subDay(i: Int): Date? {
    val cal: Calendar = Calendar.getInstance()
    cal.time = this
    cal.add(Calendar.DAY_OF_YEAR, i * -1)
    return cal.time
}

fun Date.subMonth(i: Int): Date? {
    val cal: Calendar = Calendar.getInstance()
    cal.time = this
    cal.add(Calendar.MONTH, i * -1)
    return cal.time
}

fun Date.subYear(i: Int): Date? {
    val cal: Calendar = Calendar.getInstance()
    cal.time = this
    cal.add(Calendar.YEAR, i * -1)
    return cal.time
}

fun Date.getSecond(): Int {
    val cal: Calendar = Calendar.getInstance()
    cal.time = this
    return cal.get(Calendar.SECOND)
}

fun Date.getMinute(): Int {
    val cal: Calendar = Calendar.getInstance()
    cal.time = this
    return cal.get(Calendar.MINUTE)
}

fun Date.getHour(): Int {
    val cal: Calendar = Calendar.getInstance()
    cal.time = this
    return cal.get(Calendar.HOUR_OF_DAY)
}

fun Date.getDayy(): Int {
    val cal: Calendar = Calendar.getInstance()
    cal.time = this
    return cal.get(Calendar.DAY_OF_MONTH)
}

fun Date.getMonthh(): Int {
    val cal: Calendar = Calendar.getInstance()
    cal.time = this
    return cal.get(Calendar.MONTH)
}

fun Date.getYearr(): Int {
    val cal: Calendar = Calendar.getInstance()
    cal.time = this
    return cal.get(Calendar.YEAR)
}

fun Date.setSecond(second: Int) {
    val cal: Calendar = Calendar.getInstance()
    cal.time = this
    cal.set(Calendar.SECOND, second)
}

fun Date.setMinute(minute: Int) {
    val cal: Calendar = Calendar.getInstance()
    cal.time = this
    cal.set(Calendar.MINUTE, minute)
}

fun Date.setHour(hour: Int) {
    val cal: Calendar = Calendar.getInstance()
    cal.time = this
    cal.set(Calendar.HOUR_OF_DAY, hour)
}

fun Date.setDay(day: Int) {
    val cal: Calendar = Calendar.getInstance()
    cal.time = this
    cal.set(Calendar.DAY_OF_MONTH, day)
}

fun Date.setMonthh(month: Int) {
    val cal: Calendar = Calendar.getInstance()
    cal.time = this
    cal.set(Calendar.MONTH, month - 1)
}

fun Date.setYearr(year: Int) {
    val cal: Calendar = Calendar.getInstance()
    cal.time = this
    cal.set(Calendar.YEAR, year)
}

fun Date.setStartTime(): Date {
    val c: Calendar = Calendar.getInstance()
    c.time = this
    c.set(Calendar.HOUR_OF_DAY, 0)
    c.set(Calendar.MINUTE, 0)
    c.set(Calendar.SECOND, 0)
    c.set(Calendar.MILLISECOND, 0)
    return c.time
}

fun Date.setEndTime(): Date {
    val c: Calendar = Calendar.getInstance()
    c.time = this
    c.set(Calendar.HOUR_OF_DAY, 23)
    c.set(Calendar.MINUTE, 59)
    c.set(Calendar.SECOND, 59)
    c.set(Calendar.MILLISECOND, 999)
    return c.time
}
