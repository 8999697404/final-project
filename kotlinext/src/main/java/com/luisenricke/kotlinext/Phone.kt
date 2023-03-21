package com.luisenricke.kotlinext

@Suppress("unused")
fun String.formatPhone(separator: String): String {
    val filter = this.filter { it.isDigit() }

    val result = filter.filterIndexed { index, c ->
        index >= filter.length - 10 && index <= filter.length
    }

    return "${result.slice(0..2)}$separator${result.slice(3..5)}$separator${result.slice(6..9)}"
}
