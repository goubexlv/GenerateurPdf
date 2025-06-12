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
    private val templateCv1: TemplateCv1
) : CvRepository {

    // Create TemplateCv4 instances as needed
    private fun createTemplateCv4(colorPrincipal: DeviceRgb, textColor: DeviceRgb, sidebarColor: DeviceRgb): TemplateCv4 {
        return TemplateCv4(colorPrincipal, textColor, sidebarColor)
    }

    private fun createTemplateCv3(textColorSecondary: DeviceRgb, sectionBackground: DeviceRgb, textColor: DeviceRgb,sidebarColor: DeviceRgb): TemplateCv3 {
        return TemplateCv3(textColorSecondary, sectionBackground, textColor,sidebarColor)
    }
    private fun createTemplateCv2(colorPrincipal: DeviceRgb, textColor: DeviceRgb, sidebarColor: DeviceRgb): TemplateCv4 {
        return TemplateCv4(colorPrincipal, textColor, sidebarColor)
    }


   override suspend fun initialisation(cv: Int, cvRequest: CVRequest,image : String, colors: CvColors?) {
       val defaultColors = CvColors()
       when(cv){
           1 -> templateCv1.generateCV(cvRequest)
           2 -> {
               val colorPrincipal = hexToDeviceRgb(colors?.colorPrincipal ?: defaultColors.colorPrincipal)
               val textColor = hexToDeviceRgb(colors?.textColor ?: defaultColors.textColor)
               val sidebarColor = hexToDeviceRgb(colors?.sidebarColor ?: defaultColors.sidebarColor)

               // Crée une nouvelle instance avec les couleurs personnalisées
               val customizedTemplate = createTemplateCv2(colorPrincipal, textColor, sidebarColor)
               customizedTemplate.generateCV(cvRequest, image)
           }
           3 -> {
               val textColorSecondary = hexToDeviceRgb(colors?.textColorSecondary ?: defaultColors.textColorSecondary)
               val sectionBackground = hexToDeviceRgb(colors?.sectionBackground ?: defaultColors.sectionBackground)
               val textColor = hexToDeviceRgb(colors?.textColor ?: defaultColors.textColor)
               val sidebarColor = hexToDeviceRgb(colors?.sidebarColor ?: defaultColors.sidebarColor)

               // Crée une nouvelle instance avec les couleurs personnalisées
               val customizedTemplate = createTemplateCv3(textColorSecondary, sectionBackground, textColor,sidebarColor)
               customizedTemplate.generateCV(cvRequest, image)
           }
           4 -> {

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


