package com.daccvo.utils

import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.font.PdfFont
import com.itextpdf.kernel.font.PdfFontFactory
import javax.swing.text.StyleConstants


object Constants {

    const val taille: Float = 9f
    const val taille1: Float = 11f
    const val TAILLE2: Float = 8f
    val font: PdfFont = PdfFontFactory.createFont("Times-Roman")
    val fontBold: PdfFont = PdfFontFactory.createFont("Times-Bold")
    val whileColor = DeviceRgb(255, 255, 255) // Blanc
    val backgroundColor = DeviceRgb(182, 102, 210) // Violet

}