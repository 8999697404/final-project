package com.luisenricke.kotlinext

@Suppress("unused")
fun String.removeWhiteSpaces(): String {
    return this.replace("\\s".toRegex(), "")
}
