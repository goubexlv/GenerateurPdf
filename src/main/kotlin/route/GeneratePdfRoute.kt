package com.daccvo.route

import com.daccvo.models.Endpoint
import com.daccvo.repository.GeneratePdfRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.*

fun Route.generatePdfRoute(generatePdfRepository: GeneratePdfRepository){

    get (Endpoint.GeneretePdf.path){
        try {
            generatePdfRepository.initialisation()

            call.respond(
                status = HttpStatusCode.OK,
                message = "lancement"
            )
        } catch (e : Exception){
            e.printStackTrace()  // <-- Affiche toute l’exception dans la console/terminal
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = "Erreur : ${e.message}"
            )
        }

    }

}