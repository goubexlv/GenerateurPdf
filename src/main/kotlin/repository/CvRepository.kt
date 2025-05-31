package com.daccvo.repository

import com.daccvo.models.domain.CVRequest
import com.itextpdf.layout.Document

interface CvRepository {

    suspend fun initialisation()
    suspend fun creationdoc(path : String) : Document
    suspend fun generateCV(cvRequest: CVRequest)
}