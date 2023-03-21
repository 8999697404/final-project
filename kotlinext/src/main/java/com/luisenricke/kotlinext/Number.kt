package com.luisenricke.kotlinext

@Suppress("unused")
fun Double.roundDecimals(digits: Int) = "%.${digits}f".format(this)
