package com.daccvo.route

import com.daccvo.models.domain.CVRequest
import com.daccvo.models.domain.CvColors
import com.daccvo.repository.CvRepository
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import kotlinx.serialization.json.Json
import java.io.File

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
        val multipart = call.receiveMultipart()
        var cvRequestJson: String? = null
        var image: String? = null

        multipart.forEachPart { part ->
            when (part) {
                is PartData.FileItem -> {
                    val originalFileName = part.originalFileName ?: "file_${System.currentTimeMillis()}.jpg"
                    val fileBytes = part.streamProvider().readBytes()

                    val folder = File("uploads/cv")
                    folder.mkdirs()

                    val fileName = "${System.currentTimeMillis()}_$originalFileName"
                    val file = folder.resolve(fileName)

                    file.writeBytes(fileBytes)

                    // Chemin relatif pour accès web
                    image = "uploads/cv/$fileName"
                }

                is PartData.FormItem -> {
                    if (part.name == "cvRequest") {
                        cvRequestJson = part.value
                    }
                }

                else -> {}
            }

            part.dispose()
        }

        val cv = call.parameters["cv"]
        val colors = CvColors(
            colorPrincipal = call.request.queryParameters["red"] ?: "#B73A3A",
            sectionBackground = call.request.queryParameters["darkBlue"] ?: "#2F3640",
            textColor = call.request.queryParameters["gray"] ?: "#808080",
            sidebarColor = call.request.queryParameters["lightGray"] ?: "#F0F0F0"
        )
        if (cv.isNullOrBlank()) {
            call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "cv parameter is required")
            )
            return@post
        }

        if (cvRequestJson == null) {
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to "cvRequest JSON is required in multipart form-data"))
            return@post
        }

        try {
            val cvRequest = Json.decodeFromString<CVRequest>(cvRequestJson!!)
            val imagePath = image ?: "images/tayc.png"
            cvRepository.initialisation(cv.toInt(), cvRequest, imagePath,colors)

            call.respond(HttpStatusCode.OK, mapOf("success" to "réussi", "image" to imagePath))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
        }
    }


    get("/api/health") {
        call.respond(mapOf("status" to "OK", "service" to "CV Generator"))
    }


}
