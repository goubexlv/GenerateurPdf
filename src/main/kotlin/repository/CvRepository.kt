package com.daccvo.repository

import com.daccvo.models.domain.CVRequest
import com.daccvo.models.domain.CvColors
import com.itextpdf.layout.Document

interface CvRepository {

    suspend fun initialisation(cv: Int, cvRequest: CVRequest,image : String, colors: CvColors? = null)
    //suspend fun creationdoc(path : String) : Document
}