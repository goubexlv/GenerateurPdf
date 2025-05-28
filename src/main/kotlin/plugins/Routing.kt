package com.daccvo.plugins

import com.daccvo.repository.GeneratePdfRepository
import com.daccvo.route.generatePdfRoute
import com.daccvo.route.rootRoute
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    routing {
        val generatePdfRepository : GeneratePdfRepository by application.inject()
        rootRoute()
        generatePdfRoute(generatePdfRepository)

        // Static plugin. Try to access `/static/index.html`
        staticResources("/pdfgenerer", "pdfgenerer")

    }
}
