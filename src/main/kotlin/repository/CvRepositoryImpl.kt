package com.daccvo.repository


import com.daccvo.Services.TemplateCv1
import com.daccvo.Services.TemplateCv2
import com.daccvo.Services.TemplateCv3
import com.daccvo.Services.TemplateCv4
import com.daccvo.models.domain.CVRequest
import com.daccvo.models.domain.CvColors
import com.daccvo.utils.hexToDeviceRgb
import com.itextpdf.kernel.colors.DeviceRgb


class CvRepositoryImpl(
    private val templateCv1: TemplateCv1,
    private val templateCv2: TemplateCv2,
    private val templateCv3: TemplateCv3,
) : CvRepository {

    // Create TemplateCv4 instances as needed
    fun createTemplateCv4(colorPrincipal: DeviceRgb, textColor: DeviceRgb, sidebarColor: DeviceRgb): TemplateCv4 {
        return TemplateCv4(colorPrincipal, textColor, sidebarColor)
    }

   override suspend fun initialisation(cv: Int, cvRequest: CVRequest,image : String, colors: CvColors?) {

       when(cv){
           1 -> templateCv1.generateCV(cvRequest)
           2 -> templateCv2.generateCV(cvRequest,image)
           3 -> templateCv3.generateCV(cvRequest,image)
           4 -> {

               val defaultColors = CvColors()
               val colorPrincipal = hexToDeviceRgb(colors?.colorPrincipal ?: defaultColors.colorPrincipal)
               val textColor = hexToDeviceRgb(colors?.textColor ?: defaultColors.textColor)
               val sidebarColor = hexToDeviceRgb(colors?.sidebarColor ?: defaultColors.sidebarColor)

               // Crée une nouvelle instance avec les couleurs personnalisées
               val customizedTemplate = createTemplateCv4(colorPrincipal, textColor, sidebarColor)
               customizedTemplate.generateCV(cvRequest, image)
           }
           else -> throw IllegalArgumentException("Modèle de CV non reconnu")
       }

    }


}


