package com.daccvo.repository

import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Table


interface GeneratePdfRepository {

    suspend fun initialisation()
    suspend fun creationdoc(path : String) : Document

}