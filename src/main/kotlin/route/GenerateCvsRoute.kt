package com.daccvo.route

import com.daccvo.Services.TemplateCv1
import com.daccvo.models.domain.CVRequest
import com.daccvo.repository.CvRepository
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.response.respondBytes
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post

fun Route.generateCvsRoute(cvRepository: CvRepository){

//    get ("/generate"){
//        try {
//            cvRepository.initialisation()
//            call.respond(
//                status = HttpStatusCode.Created,
//                message = "good"
//            )
//        }catch (e : Exception){
//            e.printStackTrace()  // <-- Affiche toute l’exception dans la console/terminal
//            call.respond(
//                status = HttpStatusCode.BadRequest,
//                message = "Erreur : ${e.message}"
//            )
//        }
//    }

    post("/api/cv/generate") {
        try {
            val cvRequest = call.receive<CVRequest>()
            cvRepository.initialisation(3,cvRequest)
            call.respond(HttpStatusCode.OK, mapOf("success" to "reusssi"))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
        }
    }

    get("/api/health") {
        call.respond(mapOf("status" to "OK", "service" to "CV Generator"))
    }


}
