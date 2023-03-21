package com.luisenricke.kotlinext

import org.junit.Assert
import org.junit.Test

class RoundDecimals {

    @Test
    fun piNumber() {
        val number = 3.14159265358979323
        val format = number.roundDecimals(2)
        Assert.assertEquals(3.14.toString(), format)
    }

    @Test
    fun majorFromRoundNumber() {
        val number = 1.1234567890
        val format = number.roundDecimals(5)
        Assert.assertEquals(1.12346.toString(), format)
    }

    @Test
    fun minorFromRoundNumber() {
        val number = 1.1234547890
        val format = number.roundDecimals(5)
        Assert.assertEquals(1.12345.toString(), format)
    }

    @Test
    fun roundZeroNumber() {
        val number = 1.123
        val format = number.roundDecimals(5)
        Assert.assertEquals(1.123.toFloat(), format.toFloat())
    }

    @Test
    fun roundZeroStringNumber() {
        val number = 1.123
        val format = number.roundDecimals(5)
        Assert.assertEquals("1.12300", format)
    }

}
