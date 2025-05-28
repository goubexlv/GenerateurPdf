package com.daccvo

import com.daccvo.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureFrameworks()
    configureSerialization()
    configureMonitoring()
    //configureHTTP()
    //configureSecurity()
    configureRouting()
}
