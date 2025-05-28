package com.daccvo.route

import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.rootRoute(){
    get("/") {
        call.respondText("Bienvenue!")
    }
}