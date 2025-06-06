package com.daccvo.repository

import com.daccvo.models.domain.CVRequest
import com.itextpdf.layout.Document

interface CvRepository {

    suspend fun initialisation(cv: Int, cvRequest: CVRequest,image : String)
    //suspend fun creationdoc(path : String) : Document
}