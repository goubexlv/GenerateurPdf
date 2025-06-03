package com.daccvo.repository


import com.daccvo.Services.TemplateCv1
import com.daccvo.Services.TemplateCv2
import com.daccvo.Services.TemplateCv3
import com.daccvo.Services.TemplateCv4
import com.daccvo.models.domain.CVRequest



class CvRepositoryImpl(
    private val templateCv1: TemplateCv1,
    private val templateCv2: TemplateCv2,
    private val templateCv3: TemplateCv3,
    private val templateCv4: TemplateCv4
) : CvRepository {

   override suspend fun initialisation(choixCv: Int, cvRequest: CVRequest) {

       when(choixCv){
           1 -> templateCv1.generateCV(cvRequest)
           2 -> templateCv2.generateCV(cvRequest)
           3 -> templateCv3.generateCV(cvRequest)
           4 -> templateCv4.generateCV(cvRequest)
           else -> "null"
       }

    }


}


