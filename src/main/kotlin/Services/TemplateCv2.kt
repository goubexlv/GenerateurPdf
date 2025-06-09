package com.daccvo.Services

import com.daccvo.models.domain.CVRequest
import com.daccvo.models.domain.Education
import com.daccvo.models.domain.Experience
import com.daccvo.models.domain.Language
import com.daccvo.models.domain.PersonalInfo
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.font.PdfFont
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.borders.Border
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.HorizontalAlignment
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import com.itextpdf.layout.properties.VerticalAlignment
import java.io.File
import kotlin.collections.forEach

class TemplateCv2 {

    // Couleurs du template moderne avec sidebar
    private val sidebarColor = DeviceRgb(44, 62, 80)      // Bleu-gris foncé
    private val primaryColor = DeviceRgb(52, 152, 219)    // Bleu moderne
    private val accentColor = DeviceRgb(149, 165, 166)    // Gris clair
    private val lightBackground = DeviceRgb(245, 245, 245) // Gris très clair
    private val textDark = DeviceRgb(44, 62, 80)          // Texte foncé

    private val headerFont: PdfFont by lazy { PdfFontFactory.createFont() }
    private val regularFont: PdfFont by lazy { PdfFontFactory.createFont() }
    private val boldFont: PdfFont by lazy { PdfFontFactory.createFont() }

    suspend fun generateCV(cvRequest: CVRequest, image : String) {
        val outputPath = "pdfgenerer/${cvRequest.personalInfo.lastName}_${cvRequest.personalInfo.firstName}_cv2.pdf"

        val outputFile = File(outputPath)
        outputFile.parentFile?.mkdirs()

        val writer = PdfWriter(outputFile)
        val pdfDocument = PdfDocument(writer)
        pdfDocument.defaultPageSize = PageSize.A4

        val document = Document(pdfDocument)
        document.setMargins(0f, 0f, 0f, 0f) // Pas de marges pour le layout à deux colonnes

        // Layout principal à deux colonnes
        createTwoColumnLayout(document, cvRequest,image)

        document.close()
    }

    private fun createTwoColumnLayout(document: Document, cvRequest: CVRequest, image : String) {
        // Table principale avec 2 colonnes (35% - 65%)
        val mainTable = Table(UnitValue.createPercentArray(floatArrayOf(35f, 65f)))
            .setWidth(UnitValue.createPercentValue(100f))
            .setHeight(UnitValue.createPercentValue(100f))

        // Colonne gauche (sidebar foncée)
        val leftColumn = Cell()
            .setBackgroundColor(sidebarColor)
            .setBorder(Border.NO_BORDER)
            .setPadding(25f)
            .setHeight(UnitValue.createPointValue(842f)) // Hauteur A4

        addSidebarContent(leftColumn, cvRequest.personalInfo, cvRequest.skills, cvRequest.languages,image)

        // Colonne droite (contenu principal)
        val rightColumn = Cell()
            .setBackgroundColor(ColorConstants.WHITE)
            .setBorder(Border.NO_BORDER)
            .setPadding(30f)
            .setHeight(UnitValue.createPointValue(842f))

        addMainContent(rightColumn, cvRequest)

        mainTable.addCell(leftColumn)
        mainTable.addCell(rightColumn)
        document.add(mainTable)
    }

    private fun addSidebarContent(
        sidebarCell: Cell,
        personalInfo: PersonalInfo,
        skills: List<String>,
        languages: List<Language>,
        image : String
    ) {

        val imgSrc = "images/tayc.png"
        val imageData = ImageDataFactory.create(image)
        val image = Image(imageData)
            .scaleToFit(150f, 150f) // adapte la taille
            .setHorizontalAlignment(HorizontalAlignment.CENTER)

// Table contenant l'image
        val photoTable = Table(1)
            .setWidth(UnitValue.createPercentValue(100f))
            .setMarginBottom(20f)

        val photoCell = Cell()
            .setBorder(Border.NO_BORDER)
            .setHeight(UnitValue.createPointValue(120f))
            .setVerticalAlignment(VerticalAlignment.MIDDLE)
            .setTextAlignment(TextAlignment.CENTER)
            .add(image) // Ajoute l'image dans la cellule

        photoTable.addCell(photoCell)
        sidebarCell.add(photoTable)

        // Nom et prénom
        val fullName = Paragraph("${personalInfo.firstName}\n${personalInfo.lastName}")
            .setFont(headerFont)
            .setFontSize(18f)
            .setBold()
            .setFontColor(ColorConstants.WHITE)
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginBottom(5f)
        sidebarCell.add(fullName)

        // Titre du poste souhaité (en italique)
        val jobTitle = Paragraph("Intitulé du poste")
            .setFont(regularFont)
            .setFontSize(12f)
            .setItalic()
            .setFontColor(accentColor)
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginBottom(25f)
        sidebarCell.add(jobTitle)

        // Ligne de séparation
        val separator = Table(1)
            .setWidth(UnitValue.createPercentValue(80f))
            .setMarginBottom(20f)
            .setHorizontalAlignment(HorizontalAlignment.CENTER)

        val separatorCell = Cell()
            .setBackgroundColor(accentColor)
            .setBorder(Border.NO_BORDER)
            .setHeight(UnitValue.createPointValue(2f))

        separator.addCell(separatorCell)
        sidebarCell.add(separator)

        // Section Contact
        addSidebarSection(sidebarCell, "CONTACT", listOf(
            "📞 ${personalInfo.phone}",
            "📧 ${personalInfo.email}",
            "📍 ${personalInfo.city}, ${personalInfo.address}"
        ))

        // Section Compétences
        if (skills.isNotEmpty()) {
            addSidebarSection(sidebarCell, "COMPÉTENCES", skills.map { "— $it" })
        }

        // Section Langues
        if (languages.isNotEmpty()) {
            val languageList = languages.map { "${it.name} : ${it.level}" }
            addSidebarSection(sidebarCell, "LANGUES", languageList)
        }

        // Section Centres d'intérêt (placeholder)
        addSidebarSection(sidebarCell, "CENTRES D'INTÉRÊT", listOf(
            "Équitation, bénévolat dans une",
            "association de défense des",
            "animaux, musique (piano)."
        ))
    }

    private fun addSidebarSection(sidebarCell: Cell, title: String, items: List<String>) {
        // Titre de section
        val sectionTitle = Paragraph(title)
            .setFont(boldFont)
            .setFontSize(12f)
            .setBold()
            .setFontColor(ColorConstants.WHITE)
            .setMarginTop(20f)
            .setMarginBottom(10f)
        sidebarCell.add(sectionTitle)

        // Contenu de la section
        items.forEach { item ->
            val itemParagraph = Paragraph(item)
                .setFont(regularFont)
                .setFontSize(9f)
                .setFontColor(ColorConstants.WHITE)
                .setMarginBottom(3f)
            sidebarCell.add(itemParagraph)
        }
    }

    private fun addMainContent(mainCell: Cell, cvRequest: CVRequest) {
        // En-tête avec nom en grand
        val headerName = Paragraph("${cvRequest.personalInfo.firstName} ${cvRequest.personalInfo.lastName}")
            .setFont(headerFont)
            .setFontSize(28f)
            .setBold()
            .setFontColor(textDark)
            .setMarginBottom(5f)
        mainCell.add(headerName)

        // Section Profil Professionnel
        addMainSection(mainCell, "PROFIL PROFESSIONNEL")

        val profileContent = cvRequest.personalInfo.summary ?:
        "Xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\n" +
        "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\n" +
        "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\n" +
        "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.\n\n" +
        "« Motivé, dynamique et curieux »"

        val profileParagraph = Paragraph(profileContent)
            .setFont(regularFont)
            .setFontSize(10f)
            .setTextAlignment(TextAlignment.JUSTIFIED)
            .setMarginBottom(20f)
        mainCell.add(profileParagraph)

        // Section Formation
        addMainSection(mainCell, "FORMATION")
        addEducationContent(mainCell, cvRequest.education)

        // Section Expériences Professionnelles
        addMainSection(mainCell, "EXPÉRIENCES PROFESSIONNELLES")
        addExperienceContent(mainCell, cvRequest.experience)
    }

    private fun addMainSection(mainCell: Cell, title: String) {
        val sectionTitle = Paragraph(title)
            .setFont(boldFont)
            .setFontSize(14f)
            .setBold()
            .setFontColor(textDark)
            .setMarginTop(15f)
            .setMarginBottom(10f)
        mainCell.add(sectionTitle)
    }

    private fun addEducationContent(mainCell: Cell, educations: List<Education>) {
        if (educations.isEmpty()) {
            // Contenu par défaut
            for (i in 1..3) {
                addEducationEntry(
                    mainCell,
                    "Intitulé du diplôme ou études",
                    "Université ou école",
                    "20XX - 20XX"
                )
            }
        } else {
            educations.forEach { edu ->
                addEducationEntry(
                    mainCell,
                    "${edu.degree} en ${edu.fieldOfStudy}",
                    edu.institution,
                    "${edu.startDate} - ${edu.endDate}"
                )
            }
        }
    }

    private fun addEducationEntry(mainCell: Cell, degree: String, institution: String, dates: String) {
        // Table pour aligner le contenu
        val eduTable = Table(UnitValue.createPercentArray(floatArrayOf(70f, 30f)))
            .setWidth(UnitValue.createPercentValue(100f))
            .setMarginBottom(8f)

        val contentCell = Cell()
            .setBorder(Border.NO_BORDER)
            .add(Paragraph(degree)
                .setFont(boldFont)
                .setFontSize(11f)
                .setBold()
                .setFontColor(textDark)
                .setMarginBottom(2f))
            .add(Paragraph(institution)
                .setFont(regularFont)
                .setFontSize(10f)
                .setFontColor(accentColor)
                .setItalic())

        val dateCell = Cell()
            .setBorder(Border.NO_BORDER)
            .setTextAlignment(TextAlignment.RIGHT)
            .add(Paragraph(dates)
                .setFont(regularFont)
                .setFontSize(10f)
                .setFontColor(accentColor))

        eduTable.addCell(contentCell)
        eduTable.addCell(dateCell)
        mainCell.add(eduTable)
    }

    private fun addExperienceContent(mainCell: Cell, experiences: List<Experience>) {
        if (experiences.isEmpty()) {
            // Contenu par défaut
            for (i in 1..3) {
                addExperienceEntry(
                    mainCell,
                    "Intitulé du poste",
                    "Entreprise",
                    "Sept. 20XX - Fév. 20XX",
                    listOf(
                        "Xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
                        "Xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
                        "Xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
                        "Xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
                    )
                )
            }
        } else {
            experiences.forEach { exp ->
                addExperienceEntry(
                    mainCell,
                    exp.position,
                    exp.company,
                    "${exp.startDate} - ${exp.endDate}",
                    exp.description
                )
            }
        }
    }

    private fun addExperienceEntry(
        mainCell: Cell,
        position: String,
        company: String,
        dates: String,
        descriptions: List<String>
    ) {
        // En-tête de l'expérience
        val expTable = Table(UnitValue.createPercentArray(floatArrayOf(70f, 30f)))
            .setWidth(UnitValue.createPercentValue(100f))
            .setMarginBottom(5f)

        val contentCell = Cell()
            .setBorder(Border.NO_BORDER)
            .add(Paragraph(position)
                .setFont(boldFont)
                .setFontSize(12f)
                .setBold()
                .setFontColor(textDark)
                .setMarginBottom(2f))
            .add(Paragraph(company)
                .setFont(regularFont)
                .setFontSize(10f)
                .setFontColor(accentColor)
                .setItalic())

        val dateCell = Cell()
            .setBorder(Border.NO_BORDER)
            .setTextAlignment(TextAlignment.RIGHT)
            .add(Paragraph(dates)
                .setFont(regularFont)
                .setFontSize(10f)
                .setFontColor(accentColor))

        expTable.addCell(contentCell)
        expTable.addCell(dateCell)
        mainCell.add(expTable)

        // Liste des tâches/réalisations
        descriptions.forEach { desc ->
            val bulletPoint = Paragraph("• $desc")
                .setFont(regularFont)
                .setFontSize(10f)
                .setMarginLeft(10f)
                .setMarginBottom(2f)
            mainCell.add(bulletPoint)
        }

        // Espacement après chaque expérience
        mainCell.add(Paragraph(" ").setMarginBottom(10f))
    }
}