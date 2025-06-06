package com.daccvo.Services

import com.daccvo.models.domain.CVRequest
import com.daccvo.models.domain.Certification
import com.daccvo.models.domain.Education
import com.daccvo.models.domain.Experience
import com.daccvo.models.domain.Language
import com.daccvo.models.domain.PersonalInfo
import com.daccvo.models.domain.Project
import com.daccvo.utils.Constants.backgroundBlue
import com.daccvo.utils.Constants.fontBold
import com.daccvo.utils.Constants.taille
import com.daccvo.utils.Constants.whileColor
import com.itextpdf.io.font.constants.StandardFonts
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.VerticalAlignment
import java.io.File


// Service de génération PDF

import com.itextpdf.layout.element.*
import com.itextpdf.layout.properties.*
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.font.PdfFont
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.layout.borders.Border
import com.itextpdf.layout.borders.SolidBorder
import com.itextpdf.layout.element.List as ITextList
import com.itextpdf.layout.element.ListItem as ITextListItem

class TemplateCv3 {
    // Couleurs exactes du template de la photo
    private val sidebarColor = DeviceRgb(44, 62, 80)      // Bleu-gris foncé sidebar
    private val primaryColor = DeviceRgb(52, 152, 219)    // Bleu pour les barres de compétences
    private val accentColor = DeviceRgb(149, 165, 166)    // Gris clair pour les textes secondaires
    private val lightBackground = DeviceRgb(236, 240, 241) // Gris clair pour les titres de section
    private val textDark = DeviceRgb(44, 62, 80)          // Texte foncé principal
    private val iconColor = DeviceRgb(52, 152, 219)       // Couleur des icônes

    private val headerFont: PdfFont by lazy { PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD) }
    private val regularFont: PdfFont by lazy { PdfFontFactory.createFont(StandardFonts.HELVETICA) }
    private val boldFont: PdfFont by lazy { PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD) }

    suspend fun generateCV(cvRequest: CVRequest, image : String) {
        val outputPath = "pdfgenerer/${cvRequest.personalInfo.lastName}_${cvRequest.personalInfo.firstName}_Modern.pdf"

        val outputFile = File(outputPath)
        outputFile.parentFile?.mkdirs()

        val writer = PdfWriter(outputFile)
        val pdfDocument = PdfDocument(writer)
        pdfDocument.defaultPageSize = PageSize.A4

        val document = Document(pdfDocument)
        document.setMargins(0f, 0f, 0f, 0f)

        // Layout principal à deux colonnes exactement comme la photo
        createModernTwoColumnLayout(document, cvRequest,image)

        document.close()
    }

    private fun createModernTwoColumnLayout(document: Document, cvRequest: CVRequest, image : String) {
        // Table principale avec proportions exactes de la photo (35% - 65%)
        val mainTable = Table(UnitValue.createPercentArray(floatArrayOf(35f, 65f)))
            .setWidth(UnitValue.createPercentValue(100f))
            .setHeight(UnitValue.createPercentValue(100f))

        // Colonne gauche (sidebar exacte comme la photo)
        val leftColumn = Cell()
            .setBackgroundColor(sidebarColor)
            .setBorder(Border.NO_BORDER)
            .setPadding(25f)
            .setHeight(UnitValue.createPointValue(842f))

        addModernSidebarContent(leftColumn, cvRequest,image)

        // Colonne droite (contenu principal comme la photo)
        val rightColumn = Cell()
            .setBackgroundColor(ColorConstants.WHITE)
            .setBorder(Border.NO_BORDER)
            .setPadding(30f)
            .setHeight(UnitValue.createPointValue(842f))

        addModernMainContent(rightColumn, cvRequest)

        mainTable.addCell(leftColumn)
        mainTable.addCell(rightColumn)
        document.add(mainTable)
    }

    private fun addModernSidebarContent(sidebarCell: Cell, cvRequest: CVRequest, image : String) {
        // Photo de profil circulaire comme dans la photo
        addProfilePhoto(sidebarCell, cvRequest.personalInfo,image)

        // Nom et titre professionnel centrés
        addNameAndTitle(sidebarCell, cvRequest.personalInfo)

        // Séparateur horizontal
        addSeparator(sidebarCell)

        // Section Contact avec icônes
        addContactSection(sidebarCell, cvRequest.personalInfo)

        // Section Compétences avec barres de progression
        addSkillsSection(sidebarCell, cvRequest.skills)

        // Section Langues
        addLanguagesSection(sidebarCell, cvRequest.languages)
    }

    private fun addProfilePhoto(sidebarCell: Cell, personalInfo: PersonalInfo, image : String) {
        val photoTable = Table(1)
            .setWidth(UnitValue.createPercentValue(100f))
            .setMarginBottom(20f)

        val photoCell = Cell()
            .setBorder(Border.NO_BORDER)
            .setHeight(UnitValue.createPointValue(120f))
            .setVerticalAlignment(VerticalAlignment.MIDDLE)
            .setTextAlignment(TextAlignment.CENTER)

        try {
            val imgSrc = "images/tayc.png"
            val imageData = ImageDataFactory.create(image)
            val image = Image(imageData)
                .scaleToFit(80f, 120f)
                .setHorizontalAlignment(HorizontalAlignment.CENTER)

            photoCell.add(image)
        } catch (e: Exception) {
            // Affiche un placeholder si l’image échoue à charger
            val placeholder = Paragraph("👤")
                .setFont(regularFont)
                .setFontSize(60f)
                .setFontColor(accentColor)
                .setTextAlignment(TextAlignment.CENTER)
            photoCell.add(placeholder)
        }

        photoTable.addCell(photoCell)
        sidebarCell.add(photoTable)
    }


    private fun addNameAndTitle(sidebarCell: Cell, personalInfo: PersonalInfo) {
        // Nom en majuscules et gras
        val fullName = Paragraph("${personalInfo.firstName.uppercase()}\n${personalInfo.lastName.uppercase()}")
            .setFont(headerFont)
            .setFontSize(18f)
            .setBold()
            .setFontColor(ColorConstants.WHITE)
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginBottom(5f)
            .setMultipliedLeading(1.1f)
        sidebarCell.add(fullName)

        // Titre professionnel en majuscules
        val jobTitle = Paragraph("BIOLOGISTE")
            .setFont(regularFont)
            .setFontSize(12f)
            .setFontColor(accentColor)
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginBottom(25f)
            .setCharacterSpacing(1f)
        sidebarCell.add(jobTitle)
    }

    private fun addSeparator(sidebarCell: Cell) {
        val separator = Table(1)
            .setWidth(UnitValue.createPercentValue(80f))
            .setMarginBottom(25f)
            .setHorizontalAlignment(HorizontalAlignment.CENTER)

        val separatorCell = Cell()
            .setBackgroundColor(accentColor)
            .setBorder(Border.NO_BORDER)
            .setHeight(UnitValue.createPointValue(2f))

        separator.addCell(separatorCell)
        sidebarCell.add(separator)
    }

    private fun addContactSection(sidebarCell: Cell, personalInfo: PersonalInfo) {
        // Titre de section avec icône
        val contactTitle = Paragraph("👤 CONTACT")
            .setFont(boldFont)
            .setFontSize(12f)
            .setBold()
            .setFontColor(ColorConstants.WHITE)
            .setMarginBottom(15f)
        sidebarCell.add(contactTitle)

        // Informations de contact
        val contactInfo = listOf(
            "📍 ${personalInfo.address}\n    ${personalInfo.city}",
            "📞 ${personalInfo.phone}",
            "📧 ${personalInfo.email}",
            "💼 Linkedin.fr/${personalInfo.firstName.lowercase()}${personalInfo.lastName.lowercase()}"
        )

        contactInfo.forEach { info ->
            val contactItem = Paragraph(info)
                .setFont(regularFont)
                .setFontSize(9f)
                .setFontColor(ColorConstants.WHITE)
                .setMarginBottom(8f)
                .setMultipliedLeading(1.3f)
            sidebarCell.add(contactItem)
        }
    }

    private fun addSkillsSection(sidebarCell: Cell, skills: List<String>) {
        // Titre de section
        val skillsTitle = Paragraph("⚡ COMPÉTENCES")
            .setFont(boldFont)
            .setFontSize(12f)
            .setBold()
            .setFontColor(ColorConstants.WHITE)
            .setMarginTop(25f)
            .setMarginBottom(15f)
        sidebarCell.add(skillsTitle)

        // Liste des compétences
        val defaultSkills = if (skills.isEmpty()) {
            listOf("COMPÉTENCE 1", "COMPÉTENCE 2", "COMPÉTENCE 3", "COMPÉTENCE 4")
        } else skills

        defaultSkills.forEach { skill ->
            val skillParagraph = Paragraph("• $skill")
                .setFont(regularFont)
                .setFontSize(10f)
                .setFontColor(ColorConstants.WHITE)
                .setMarginBottom(5f)
            sidebarCell.add(skillParagraph)
        }
    }

    private fun addSkillWithProgressBar(sidebarCell: Cell, skillName: String, level: Int) {
        // Nom de la compétence
        val skillText = Paragraph(skillName.uppercase())
            .setFont(regularFont)
            .setFontSize(9f)
            .setFontColor(ColorConstants.WHITE)
            .setMarginBottom(4f)
        sidebarCell.add(skillText)

        // Barre de progression
        val progressBarContainer = Table(UnitValue.createPercentArray(floatArrayOf(level.toFloat(), (100 - level).toFloat())))
            .setWidth(UnitValue.createPercentValue(100f))
            .setMarginBottom(12f)

        // Partie remplie de la barre
        val filledPart = Cell()
            .setBackgroundColor(primaryColor)
            .setBorder(Border.NO_BORDER)
            .setHeight(UnitValue.createPointValue(8f))

        // Partie vide de la barre
        val emptyPart = Cell()
            .setBackgroundColor(DeviceRgb(52, 73, 94)) // Plus foncé que la sidebar
            .setBorder(Border.NO_BORDER)
            .setHeight(UnitValue.createPointValue(8f))

        progressBarContainer.addCell(filledPart)
        if (level < 100) {
            progressBarContainer.addCell(emptyPart)
        }

        sidebarCell.add(progressBarContainer)
    }

    private fun addLanguagesSection(sidebarCell: Cell, languages: List<Language>) {
        // Titre de section
        val languagesTitle = Paragraph("🌐 LANGUES")
            .setFont(boldFont)
            .setFontSize(12f)
            .setBold()
            .setFontColor(ColorConstants.WHITE)
            .setMarginTop(25f)
            .setMarginBottom(15f)
        sidebarCell.add(languagesTitle)

        // Langues
        val defaultLanguages = if (languages.isEmpty()) {
            listOf("LANGUE 1", "LANGUE 2")
        } else {
            languages.map { "${it.name.uppercase()}" }
        }

        defaultLanguages.forEach { language ->
            val languageItem = Paragraph(language)
                .setFont(regularFont)
                .setFontSize(9f)
                .setFontColor(ColorConstants.WHITE)
                .setMarginBottom(10f)
            sidebarCell.add(languageItem)
        }
    }

    private fun addModernMainContent(mainCell: Cell, cvRequest: CVRequest) {
        // En-tête avec nom en grandes majuscules
        val headerName = Paragraph("${cvRequest.personalInfo.firstName.uppercase()} ${cvRequest.personalInfo.lastName.uppercase()}")
            .setFont(headerFont)
            .setFontSize(28f)
            .setBold()
            .setFontColor(textDark)
            .setMarginBottom(25f)
        mainCell.add(headerName)

        // Section Profil
        addMainSectionWithBackground(mainCell, "PROFIL")
        addProfileContent(mainCell, cvRequest.personalInfo.summary)

        // Section Formation
        addMainSectionWithBackground(mainCell, "FORMATION")
        addEducationContent(mainCell, cvRequest.education)

        // Section Expériences Professionnelles
        addMainSectionWithBackground(mainCell, "EXPÉRIENCES PROFESSIONNELLES")
        addExperienceContent(mainCell, cvRequest.experience)

        // Section Références
        addMainSectionWithBackground(mainCell, "RÉFÉRENCES")
        addReferencesContent(mainCell)
    }

    private fun addMainSectionWithBackground(mainCell: Cell, title: String) {
        val sectionTitle = Paragraph(title)
            .setFont(boldFont)
            .setFontSize(14f)
            .setBold()
            .setFontColor(textDark)
            .setBackgroundColor(lightBackground)
            .setPadding(8f)
            .setPaddingLeft(10f)
            .setMarginTop(20f)
            .setMarginBottom(15f)
            .setCharacterSpacing(1f)
        mainCell.add(sectionTitle)
    }

    private fun addProfileContent(mainCell: Cell, summary: String?) {
        val profileContent = summary ?:
        "Décrivez en quelques lignes vos compétences clés pour le poste et vos objectifs de carrière. Ceci est en fait une introduction à votre lettre de motivation."

        val profileParagraph = Paragraph(profileContent)
            .setFont(regularFont)
            .setFontSize(10f)
            .setTextAlignment(TextAlignment.JUSTIFIED)
            .setMarginBottom(20f)
            .setMultipliedLeading(1.4f)
        mainCell.add(profileParagraph)
    }

    private fun addEducationContent(mainCell: Cell, educations: List<Education>) {

        educations.forEach { education ->
            addEducationEntry(
                mainCell,
                education.degree,
                education.institution,
                "${education.startDate}-${education.endDate}"
            )
        }
    }

    private fun addEducationEntry(mainCell: Cell, degree: String, institution: String, period: String) {
        val educationContainer = Table(UnitValue.createPercentArray(floatArrayOf(10f, 90f)))
            .setWidth(UnitValue.createPercentValue(100f))
            .setMarginBottom(15f)

        // Icône
        val iconCell = Cell()
            .setBorder(Border.NO_BORDER)
            .setTextAlignment(TextAlignment.CENTER)
            .setVerticalAlignment(VerticalAlignment.TOP)
            .add(Paragraph("📍")
                .setFont(regularFont)
                .setFontSize(12f)
                .setFontColor(iconColor))

        // Contenu
        val contentCell = Cell()
            .setBorder(Border.NO_BORDER)
            .add(Paragraph(degree.uppercase())
                .setFont(boldFont)
                .setFontSize(12f)
                .setBold()
                .setFontColor(textDark)
                .setMarginBottom(3f))
            .add(Paragraph("$period | $institution")
                .setFont(regularFont)
                .setFontSize(10f)
                .setFontColor(accentColor)
                .setItalic())

        educationContainer.addCell(iconCell)
        educationContainer.addCell(contentCell)
        mainCell.add(educationContainer)
    }

    private fun addExperienceContent(mainCell: Cell, experiences: List<Experience>) {

        experiences.forEach { experience ->
            addExperienceEntry(
                mainCell,
                experience.position,
                experience.company,
                "${experience.startDate}-${experience.endDate}",
                experience.description
            )
        }
    }

    private fun addExperienceEntry(
        mainCell: Cell,
        position: String,
        company: String,
        period: String,
        descriptions: List<String>
    ) {
        val experienceContainer = Table(UnitValue.createPercentArray(floatArrayOf(10f, 90f)))
            .setWidth(UnitValue.createPercentValue(100f))
            .setMarginBottom(20f)

        // Icône
        val iconCell = Cell()
            .setBorder(Border.NO_BORDER)
            .setTextAlignment(TextAlignment.CENTER)
            .setVerticalAlignment(VerticalAlignment.TOP)
            .add(Paragraph("📍")
                .setFont(regularFont)
                .setFontSize(12f)
                .setFontColor(iconColor))

        // Contenu
        val contentCell = Cell()
            .setBorder(Border.NO_BORDER)
            .add(Paragraph(position.uppercase())
                .setFont(boldFont)
                .setFontSize(12f)
                .setBold()
                .setFontColor(textDark)
                .setMarginBottom(3f))
            .add(Paragraph("$period | $company")
                .setFont(regularFont)
                .setFontSize(10f)
                .setFontColor(accentColor)
                .setItalic()
                .setMarginBottom(8f))

        // Description
        descriptions.forEach { desc ->
            contentCell.add(Paragraph(desc)
                .setFont(regularFont)
                .setFontSize(10f)
                .setTextAlignment(TextAlignment.JUSTIFIED)
                .setMarginBottom(5f)
                .setMultipliedLeading(1.3f))
        }

        experienceContainer.addCell(iconCell)
        experienceContainer.addCell(contentCell)
        mainCell.add(experienceContainer)
    }

    private fun addReferencesContent(mainCell: Cell) {
        val referencesContainer = Table(UnitValue.createPercentArray(floatArrayOf(50f, 50f)))
            .setWidth(UnitValue.createPercentValue(100f))
            .setMarginTop(10f)

        // Référent 1
        val ref1Cell = createReferenceCell("Référent 1", "Titre du poste", "Nom de l'entreprise", "06 07 08 09 10", "referent@mail.fr")

        // Référent 2
        val ref2Cell = createReferenceCell("Référent 2", "Titre du poste", "Nom de l'entreprise", "06 07 08 09 10", "referent@mail.fr")

        referencesContainer.addCell(ref1Cell)
        referencesContainer.addCell(ref2Cell)
        mainCell.add(referencesContainer)
    }

    private fun createReferenceCell(name: String, title: String, company: String, phone: String, email: String): Cell {
        val refContainer = Table(UnitValue.createPercentArray(floatArrayOf(20f, 80f)))
            .setWidth(UnitValue.createPercentValue(100f))

        // Avatar
        val avatarCell = Cell()
            .setBorder(Border.NO_BORDER)
            .setTextAlignment(TextAlignment.CENTER)
            .setVerticalAlignment(VerticalAlignment.TOP)
            .add(Paragraph("👤")
                .setFont(regularFont)
                .setFontSize(24f)
                .setFontColor(accentColor))

        // Info référent
        val infoCell = Cell()
            .setBorder(Border.NO_BORDER)
            .add(Paragraph(name)
                .setFont(boldFont)
                .setFontSize(10f)
                .setBold()
                .setFontColor(textDark)
                .setMarginBottom(2f))
            .add(Paragraph(title)
                .setFont(regularFont)
                .setFontSize(9f)
                .setFontColor(accentColor)
                .setItalic()
                .setMarginBottom(1f))
            .add(Paragraph(company)
                .setFont(regularFont)
                .setFontSize(9f)
                .setFontColor(accentColor)
                .setItalic()
                .setMarginBottom(3f))
            .add(Paragraph(phone)
                .setFont(regularFont)
                .setFontSize(8f)
                .setFontColor(accentColor)
                .setMarginBottom(1f))
            .add(Paragraph(email)
                .setFont(regularFont)
                .setFontSize(8f)
                .setFontColor(accentColor))

        refContainer.addCell(avatarCell)
        refContainer.addCell(infoCell)

        return Cell()
            .setBorder(Border.NO_BORDER)
            .setPaddingRight(10f)
            .add(refContainer)
    }
}