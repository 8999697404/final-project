package com.luisenricke.room

import java.util.concurrent.Executors

private val IO_EXECUTOR = Executors.newSingleThreadExecutor()

@Suppress("unused")
fun ioThread(f: () -> Unit) = IO_EXECUTOR.execute(f)

