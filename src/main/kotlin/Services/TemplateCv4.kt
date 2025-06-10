package com.daccvo.Services

import com.daccvo.models.domain.CVRequest
import com.daccvo.models.domain.Education
import com.daccvo.models.domain.Experience
import com.daccvo.models.domain.Language
import com.daccvo.models.domain.PersonalInfo
import com.itextpdf.io.font.constants.StandardFonts
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.font.PdfFont
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.canvas.PdfCanvas
import com.itextpdf.layout.Document
import com.itextpdf.layout.borders.*
import com.itextpdf.layout.element.*
import com.itextpdf.layout.properties.*
import java.io.File

class TemplateCv4(
    private val colorPrincipal: DeviceRgb,
    private val textColor: DeviceRgb,
    private val sidebarColor: DeviceRgb
) {

    // Couleurs du design
//    private val redColor = hexToDeviceRgb("#B73A3A")     // Rouge principal
//    private val darkBlueColor = hexToDeviceRgb("#2F3640")// Bleu foncé
//    private val grayColor = hexToDeviceRgb("#808080")   // Gris pour le texte
//    private val lightGrayColor = hexToDeviceRgb("#F0F0F0") // Gris clair pour fond

    // Polices
    private lateinit var regularFont: PdfFont
    private lateinit var boldFont: PdfFont
    private lateinit var pdfDocument: PdfDocument

    fun generateCV(cvRequest: CVRequest,image : String) {
        val outputPath = "pdfgenerer/${cvRequest.personalInfo.lastName}_${cvRequest.personalInfo.firstName}_Modern.pdf"

        val outputFile = File(outputPath)
        outputFile.parentFile?.mkdirs()

        val writer = PdfWriter(outputFile)
        pdfDocument = PdfDocument(writer)
        pdfDocument.defaultPageSize = PageSize.A4

        val document = Document(pdfDocument)
        document.setMargins(0f, 0f, 0f, 0f)

        // Initialiser les polices
        regularFont = PdfFontFactory.createFont(StandardFonts.HELVETICA)
        boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD)

        val imgSrc = "images/tayc.png"

        // Créer l'en-tête avec design géométrique
        createHeader(document, cvRequest.personalInfo, image)

        // Créer le contenu principal
        createMainContent(document, cvRequest)

        document.close()
    }

    private fun createHeader(document: Document, personalInfo: PersonalInfo, photoPath: String?) {
        document.add(Paragraph("")) // Crée une page pour que drawHeaderBackground fonctionne
        drawHeaderBackground(document)

        val headerTable = Table(floatArrayOf(1f, 2f))
        headerTable.setWidth(UnitValue.createPercentValue(100f))
        headerTable.setBorder(Border.NO_BORDER)


        // === Cellule de gauche ===
        val leftCell = Cell()
        leftCell.setBorder(Border.NO_BORDER)
        leftCell.setPadding(0f)
        leftCell.setHeight(200f)

        // Créer un div avec fond bleu
        val leftDiv = Div()
        leftDiv.setHeight(200f)
        //leftDiv.setBackgroundColor(darkBlueColor)

        // Ajouter la photo si fournie
        photoPath?.let { path ->
            try {
                if (File(path).exists()) {
                    val imageData = ImageDataFactory.create(path)
                    val image = Image(imageData)
                    image.setHeight(150f)
                    image.setWidth(150f)
                    image.setMarginTop(20f)
                    image.setMarginLeft(0f)
                    image.setHorizontalAlignment(HorizontalAlignment.CENTER)
                    leftDiv.add(image)
                }
            } catch (e: Exception) {
                println("Erreur lors du chargement de l'image: ${e.message}")
            }
        }

        leftCell.add(leftDiv)
        headerTable.addCell(leftCell)

        // === Cellule de droite ===
        val rightCell = Cell()
        rightCell.setBorder(Border.NO_BORDER)
        rightCell.setPadding(30f)
        rightCell.setVerticalAlignment(VerticalAlignment.MIDDLE)

        val fullName = Paragraph("${personalInfo.firstName.uppercase()} ${personalInfo.lastName.uppercase()}")
            .setFont(boldFont)
            .setFontSize(24f)
            .setMarginBottom(5f)
        rightCell.add(fullName)

        val jobTitle = Paragraph("DÉVELOPPEUR")
            .setFont(regularFont)
            .setFontSize(14f)
            .setFontColor(textColor)
            .setMarginBottom(20f)
        rightCell.add(jobTitle)

        personalInfo.summary?.let { summary ->
            val summaryParagraph = Paragraph(summary)
                .setFont(regularFont)
                .setFontSize(10f)
                .setFontColor(textColor)
                .setTextAlignment(TextAlignment.JUSTIFIED)
            rightCell.add(summaryParagraph)
        }

        headerTable.addCell(rightCell)
        document.add(headerTable)

        // MAINTENANT dessiner le triangle une fois le contenu en place
        //drawTriangle(document)
    }

    private fun drawTriangle(document: Document) {
        val page = document.pdfDocument.getFirstPage()
        val pdfCanvas = PdfCanvas(page)
        val pageSize = page.pageSize

        val blueBoxX = document.leftMargin  // Position X de la cellule de gauche
        val blueBoxY = pageSize.top - document.topMargin  // Top Y (haut de la page)
        val blueBoxHeight = 200.0
        val blueBoxWidth = (pageSize.width - document.leftMargin - document.rightMargin) * (1.0 / 3.0)

        val triangleSize = 80.0

        // Triangle rouge dans le coin supérieur droit de la boîte bleue
        val triangleX = blueBoxX + blueBoxWidth
        val triangleY = blueBoxY

        pdfCanvas
            .saveState()
            .setFillColor(colorPrincipal)
            .moveTo(triangleX, triangleY.toDouble())
            .lineTo(triangleX - triangleSize, triangleY.toDouble())
            .lineTo(triangleX, triangleY - triangleSize)
            .closePath()
            .fill()
            .restoreState()
    }

    private fun drawHeaderBackground(document: Document) {
        val page = document.pdfDocument.firstPage
        val canvas = PdfCanvas(page)
        val pageSize = page.pageSize

        val topY = pageSize.top
        val bottomY = pageSize.top - 150f  // Hauteur de la bande

        // === Triangle rouge (haut) ===
        canvas.saveState()
        canvas.setFillColor(DeviceRgb(173, 0, 0)) // Rouge foncé
        canvas.moveTo(0.0, topY.toDouble())
        canvas.lineTo(pageSize.width.toDouble(), topY.toDouble())
        canvas.lineTo(pageSize.width.toDouble(), bottomY.toDouble())
        canvas.closePath()
        canvas.fill()
        canvas.restoreState()

        // === Triangle noir (dessous) ===
        canvas.saveState()
        canvas.setFillColor(DeviceRgb(30, 30, 30)) // Noir/gris foncé
        canvas.moveTo(0.0, bottomY.toDouble())
        canvas.lineTo(pageSize.width * 0.5, bottomY - 40.0)
        canvas.lineTo(0.0, bottomY - 40.0)
        canvas.closePath()
        canvas.fill()
        canvas.restoreState()
    }


    private fun createMainContent(document: Document, cvRequest: CVRequest) {
        val mainTable = Table(floatArrayOf(1f, 2f))
        mainTable.setWidth(UnitValue.createPercentValue(100f))
        mainTable.setBorder(Border.NO_BORDER)

        // Colonne de gauche (Profil)
        val leftColumn = Cell()
        leftColumn.setBorder(Border.NO_BORDER)
        leftColumn.setPadding(20f)
        leftColumn.setBackgroundColor(sidebarColor)

        createProfileSection(leftColumn, cvRequest.personalInfo)
        createSkillsSection(leftColumn, cvRequest.skills)
        createLanguagesSection(leftColumn, cvRequest.languages)

        mainTable.addCell(leftColumn)

        // Colonne de droite (Expériences et Formations)
        val rightColumn = Cell()
        rightColumn.setBorder(Border.NO_BORDER)
        rightColumn.setPadding(30f)

        createExperienceSection(rightColumn, cvRequest.experience)
        createEducationSection(rightColumn, cvRequest.education)

        mainTable.addCell(rightColumn)

        document.add(mainTable)
    }

    private fun createProfileSection(cell: Cell, personalInfo: PersonalInfo) {
        val profileTitle = Paragraph("PROFIL")
        profileTitle.setFont(boldFont)
        profileTitle.setFontSize(14f)
        profileTitle.setMarginBottom(15f)
        cell.add(profileTitle)

        // Adresse
        addProfileItem(cell, "Adresse", "${personalInfo.address}, ${personalInfo.city}")

        // Email
        addProfileItem(cell, "E-mail", personalInfo.email)

        // Téléphone
        addProfileItem(cell, "Téléphone", personalInfo.phone)

        // Pays
        addProfileItem(cell, "Pays", personalInfo.country)

        // LinkedIn
        personalInfo.linkedin?.let { linkedin ->
            addProfileItem(cell, "LinkedIn", linkedin)
        }

        // GitHub
        personalInfo.github?.let { github ->
            addProfileItem(cell, "GitHub", github)
        }
    }

    private fun addProfileItem(cell: Cell, label: String, value: String) {
        val labelParagraph = Paragraph(label)
        labelParagraph.setFont(boldFont)
        labelParagraph.setFontSize(10f)
        labelParagraph.setMarginBottom(2f)
        cell.add(labelParagraph)

        val valueParagraph = Paragraph(value)
        valueParagraph.setFont(regularFont)
        valueParagraph.setFontSize(9f)
        valueParagraph.setFontColor(textColor)
        valueParagraph.setMarginBottom(10f)
        cell.add(valueParagraph)
    }

    private fun createSkillsSection(cell: Cell, skills: List<String>) {
        if (skills.isEmpty()) return

        val skillsTitle = Paragraph("COMPÉTENCES")
        skillsTitle.setFont(boldFont)
        skillsTitle.setFontSize(14f)
        skillsTitle.setMarginBottom(15f)
        skillsTitle.setMarginTop(20f)
        cell.add(skillsTitle)

        skills.forEach { skill ->
            val skillParagraph = Paragraph(skill)
            skillParagraph.setFont(regularFont)
            skillParagraph.setFontSize(10f)
            skillParagraph.setMarginBottom(5f)
            cell.add(skillParagraph)
        }
    }

    private fun createLanguagesSection(cell: Cell, languages: List<Language>) {
        if (languages.isEmpty()) return

        val languagesTitle = Paragraph("LANGUES")
        languagesTitle.setFont(boldFont)
        languagesTitle.setFontSize(14f)
        languagesTitle.setMarginBottom(15f)
        languagesTitle.setMarginTop(20f)
        cell.add(languagesTitle)

        languages.forEach { language ->
            val languageParagraph = Paragraph("${language.name} - ${language.level}")
            languageParagraph.setFont(regularFont)
            languageParagraph.setFontSize(10f)
            languageParagraph.setMarginBottom(5f)
            cell.add(languageParagraph)
        }
    }

    private fun createExperienceSection(cell: Cell, experiences: List<Experience>) {
        if (experiences.isEmpty()) return

        val experienceTitle = Paragraph("EXPÉRIENCES PROFESSIONNELLES")
        experienceTitle.setFont(boldFont)
        experienceTitle.setFontSize(14f)
        experienceTitle.setFontColor(colorPrincipal)
        experienceTitle.setMarginBottom(20f)
        cell.add(experienceTitle)

        experiences.forEach { experience ->
            // Dates
            val dates = Paragraph("${experience.startDate} - ${experience.endDate}")
            dates.setFont(regularFont)
            dates.setFontSize(10f)
            dates.setFontColor(textColor)
            dates.setMarginBottom(2f)
            cell.add(dates)

            // Poste - Entreprise
            val position = Paragraph("${experience.position.uppercase()} - ${experience.company.uppercase()}")
            position.setFont(boldFont)
            position.setFontSize(12f)
            position.setMarginBottom(5f)
            cell.add(position)

            // Description
            experience.description.forEach { desc ->
                val descParagraph = Paragraph(desc)
                descParagraph.setFont(regularFont)
                descParagraph.setFontSize(10f)
                descParagraph.setFontColor(textColor)
                descParagraph.setTextAlignment(TextAlignment.JUSTIFIED)
                descParagraph.setMarginBottom(3f)
                cell.add(descParagraph)
            }

            // Technologies
            if (experience.technologies.isNotEmpty()) {
                val techParagraph = Paragraph("Technologies: ${experience.technologies.joinToString(", ")}")
                techParagraph.setFont(regularFont)
                techParagraph.setFontSize(9f)
                techParagraph.setFontColor(textColor)
                techParagraph.setItalic()
                techParagraph.setMarginBottom(15f)
                cell.add(techParagraph)
            } else {
                val spacer = Paragraph(" ")
                spacer.setMarginBottom(15f)
                cell.add(spacer)
            }
        }
    }

    private fun createEducationSection(cell: Cell, educations: List<Education>) {
        if (educations.isEmpty()) return

        val educationTitle = Paragraph("FORMATIONS")
        educationTitle.setFont(boldFont)
        educationTitle.setFontSize(14f)
        educationTitle.setFontColor(colorPrincipal)
        educationTitle.setMarginBottom(20f)
        educationTitle.setMarginTop(20f)
        cell.add(educationTitle)

        educations.forEach { education ->
            // Dates
            val dates = Paragraph("${education.startDate} - ${education.endDate}")
            dates.setFont(regularFont)
            dates.setFontSize(10f)
            dates.setFontColor(textColor)
            dates.setMarginBottom(2f)
            cell.add(dates)

            // Diplôme
            val degree = Paragraph(education.degree.uppercase())
            degree.setFont(boldFont)
            degree.setFontSize(12f)
            degree.setMarginBottom(2f)
            cell.add(degree)

            // Institution
            val institution = Paragraph(education.institution.uppercase())
            institution.setFont(regularFont)
            institution.setFontSize(10f)
            institution.setFontColor(textColor)
            institution.setMarginBottom(5f)
            cell.add(institution)

            // Description
            education.description?.let { desc ->
                val descParagraph = Paragraph(desc)
                descParagraph.setFont(regularFont)
                descParagraph.setFontSize(10f)
                descParagraph.setFontColor(textColor)
                descParagraph.setTextAlignment(TextAlignment.JUSTIFIED)
                descParagraph.setMarginBottom(15f)
                cell.add(descParagraph)
            } ?: run {
                val spacer = Paragraph(" ")
                spacer.setMarginBottom(15f)
                cell.add(spacer)
            }
        }
    }
}