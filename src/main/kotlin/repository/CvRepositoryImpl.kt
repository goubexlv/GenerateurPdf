package com.daccvo.repository


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


class CvRepositoryImpl : CvRepository {

    override suspend fun initialisation() {
        val espa = ""
        val para1 = Paragraph(espa)
        val outputPath = "pdfgenerer/cv.pdf"
        val document = creationdoc(outputPath)

        //corps du cv1
        document.add(Template1Entete())
        document.add(Template1Profil())

        document.close()
    }
    override suspend fun creationdoc(path: String): Document {
        val outputFile = File(path)
        outputFile.parentFile?.mkdirs()

        val writer = PdfWriter(outputFile)
        val pdfDocument = PdfDocument(writer)
        pdfDocument.defaultPageSize = PageSize.A4

        val document = Document(pdfDocument)
        document.setMargins(0f, 0f, 0f, 0f)

        return document
    }
    private suspend fun Template1Entete() : Table {
        val colonetaille = floatArrayOf(300f, 300f, 300f)
        val table: Table = Table(colonetaille)

        //parametre
        table.addCell(Cell(5, 0).add(Paragraph(" ")).setFontColor(whileColor).setBackgroundColor(backgroundBlue))
        table.addCell(Cell().add(Paragraph("Prénom ")).setFont(fontBold).setFontSize(14f).setFontColor(whileColor).setBackgroundColor(backgroundBlue))
        table.addCell(Cell().add(Paragraph(" ")).setFontColor(whileColor).setBackgroundColor(backgroundBlue))

        table.addCell(Cell(3, 0).add(Paragraph("NOM")).setFontColor(whileColor).setBackgroundColor(backgroundBlue))
        table.addCell(Cell().add(Paragraph("Téléphone")).setFont(fontBold).setFontSize(taille).setFontColor(whileColor).setBackgroundColor(backgroundBlue))

        //table.addCell(Cell().add(Paragraph(" ")).setFont(fontBold).setFontSize(14f))
        table.addCell(Cell().add(Paragraph("Lieu ")).setFont(fontBold).setFontSize(taille).setFontColor(whileColor).setBackgroundColor(backgroundBlue))

        //table.addCell(Cell().add(Paragraph(" ")).setFont(fontBold).setFontSize(14f))
        table.addCell(Cell().add(Paragraph("E-mail ")).setFont(fontBold).setFontSize(taille).setFontColor(whileColor).setBackgroundColor(backgroundBlue))

        table.addCell(Cell().add(Paragraph("Employée Polyvalente")).setFont(fontBold).setFontSize(14f).setFontColor(whileColor).setBackgroundColor(backgroundBlue))
        table.addCell(Cell().add(Paragraph(" ")).setFont(fontBold).setFontSize(taille).setFontColor(whileColor).setBackgroundColor(backgroundBlue))


        return table
    }
    private suspend fun Template1Profil() : Table {
        val colonetaille = floatArrayOf(600f)
        val table: Table = Table(colonetaille)

        table.addCell(Cell(1, colonetaille.size).add(Paragraph(" Je suis très dynamique, travailleuse et ponctuelle. Je maîtrise l'outil informatique, le pack Microsoft, l'infographie et le développement web. Convaincue que mes compétences et mon enthousiasme seront un atout pour votre équipe, je donnerai le meilleur de moi-même pour contribuer au succès de votre entreprise."))
            .setFontSize(12f)
            .setTextAlignment(TextAlignment.CENTER)
            .setVerticalAlignment(VerticalAlignment.MIDDLE) )

        return table

    }

    // Couleurs modernes
    private val primaryColor = DeviceRgb(41, 128, 185) // Bleu moderne
    private val accentColor = DeviceRgb(52, 73, 94)    // Gris foncé
    private val lightGray = DeviceRgb(236, 240, 241)   // Gris clair
    private val darkGray = DeviceRgb(127, 140, 141)    // Gris moyen
    private val headerFont: PdfFont by lazy { PdfFontFactory.createFont() }
    private val regularFont: PdfFont by lazy { PdfFontFactory.createFont() }
    private val boldFont: PdfFont by lazy { PdfFontFactory.createFont() }

    override suspend fun generateCV(cvRequest: CVRequest) {
        val outputPath = "pdfgenerer/${cvRequest.personalInfo.lastName}_${cvRequest.personalInfo.firstName}.pdf"

        val outputFile = File(outputPath)
        outputFile.parentFile?.mkdirs()

        val writer = PdfWriter(outputFile)
        val pdfDocument = PdfDocument(writer)
        pdfDocument.defaultPageSize = PageSize.A4

        val document = Document(pdfDocument)
        document.setMargins(10f, 10f, 10f, 10f)

        // Ajout des sections avec nouveau design
        addModernHeader(document, cvRequest.personalInfo)
        addSummarySection(document, cvRequest.personalInfo.summary)
        addExperienceSection(document, cvRequest.experience)
        addEducationSection(document, cvRequest.education)

        // Section à deux colonnes pour compétences et langues
        addSkillsAndLanguagesSection(document, cvRequest.skills, cvRequest.languages)

        addCertificationsSection(document, cvRequest.certifications)
        addProjectsSection(document, cvRequest.projects)

        document.close()
    }

    private fun addModernHeader(document: Document, personalInfo: PersonalInfo) {
        // Bande colorée en haut
        val headerBackground = Table(1)
            .setWidth(UnitValue.createPercentValue(100f))
            .setMarginBottom(5f)

        val headerCell = Cell()
            .setBackgroundColor(primaryColor)
            .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER)
            .setPadding(25f)

        // Nom en blanc sur fond coloré
        val nameTitle = Paragraph("${personalInfo.firstName} ${personalInfo.lastName}")
            .setFont(headerFont)
            .setFontSize(24f)
            .setBold()
            .setFontColor(ColorConstants.WHITE)
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginBottom(5f)

        headerCell.add(nameTitle)
        headerBackground.addCell(headerCell)
        document.add(headerBackground)

        // Section contact moderne
        val contactSection = Table(UnitValue.createPercentArray(floatArrayOf(1f, 1f, 1f)))
            .setWidth(UnitValue.createPercentValue(100f))
            .setBackgroundColor(lightGray)
            .setPadding(5f)
            .setMarginBottom(5f)

        // Colonne 1 - Contact principal
        val contactCol1 = Cell()
            .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER)
            .add(Paragraph("📧 ${personalInfo.email}")
                .setFont(regularFont).setFontSize(10f).setMarginBottom(3f))
            .add(Paragraph("📱 ${personalInfo.phone}")
                .setFont(regularFont).setFontSize(10f).setMarginBottom(3f))

        // Colonne 2 - Localisation
        val contactCol2 = Cell()
            .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER)
            .add(Paragraph("📍 ${personalInfo.city}")
                .setFont(regularFont).setFontSize(10f).setMarginBottom(3f))
            .add(Paragraph("${personalInfo.address}")
                .setFont(regularFont).setFontSize(10f).setMarginBottom(3f))

        // Colonne 3 - Liens
        val contactCol3 = Cell()
            .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER)

        personalInfo.linkedin?.let {
            contactCol3.add(Paragraph("💼 ${personalInfo.linkedin}")
                .setFont(regularFont).setFontSize(10f).setMarginBottom(3f))
        }
        personalInfo.github?.let {
            contactCol3.add(Paragraph("💻 ${personalInfo.github}")
                .setFont(regularFont).setFontSize(10f).setMarginBottom(3f))
        }
        personalInfo.website?.let {
            contactCol3.add(Paragraph("🌐 ${personalInfo.website}")
                .setFont(regularFont).setFontSize(10f).setMarginBottom(3f))
        }

        contactSection.addCell(contactCol1)
        contactSection.addCell(contactCol2)
        contactSection.addCell(contactCol3)

        document.add(contactSection)
    }

    private fun addSummarySection(document: Document, summary: String?) {
        if (summary.isNullOrBlank()) return

        val summaryTitle = createModernSectionTitle("PROFIL PROFESSIONNEL")
        document.add(summaryTitle)

        // Boîte avec fond coloré pour le résumé
        val summaryBox = Table(1)
            .setWidth(UnitValue.createPercentValue(100f))
            .setMarginBottom(5f)

        val summaryCell = Cell()
            .setBackgroundColor(lightGray)
            .setBorder(SolidBorder(primaryColor, 2f))
            .setPadding(15f)
            .add(Paragraph(summary)
                .setFont(regularFont)
                .setFontSize(10f)
                .setTextAlignment(TextAlignment.JUSTIFIED)
                .setMarginBottom(0f))

        summaryBox.addCell(summaryCell)
        document.add(summaryBox)
    }

    private fun addExperienceSection(document: Document, experiences: List<Experience>) {
        if (experiences.isEmpty()) return

        val experienceTitle = createModernSectionTitle("EXPÉRIENCE PROFESSIONNELLE")
        document.add(experienceTitle)

        experiences.forEach { exp ->
            // Conteneur pour une expérience
            val expContainer = Table(1)
                .setWidth(UnitValue.createPercentValue(100f))
                .setMarginBottom(5f)

            val expCell = Cell()
                .setBorder(SolidBorder(lightGray, 1f))
                .setPadding(15f)

            // En-tête de l'expérience avec fond coloré
            val headerTable = Table(UnitValue.createPercentArray(floatArrayOf(3f, 1f)))
                .setWidth(UnitValue.createPercentValue(100f))
                .setMarginBottom(5f)

            val titleCell = Cell()
                .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER)
                .add(Paragraph("${exp.position}")
                    .setFont(boldFont).setFontSize(13f).setBold().setFontColor(primaryColor))
                .add(Paragraph("${exp.company}")
                    .setFont(boldFont).setFontSize(11f).setFontColor(accentColor))

            val dateCell = Cell()
                .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER)
                .setTextAlignment(TextAlignment.RIGHT)
                .add(Paragraph("${exp.startDate} - ${exp.endDate}")
                    .setFont(regularFont).setFontSize(10f).setFontColor(darkGray))
                .add(Paragraph("${exp.location}")
                    .setFont(regularFont).setFontSize(10f).setFontColor(darkGray))

            headerTable.addCell(titleCell)
            headerTable.addCell(dateCell)
            expCell.add(headerTable)

            // Description avec puces modernes
            val descriptionList = ITextList()
                .setListSymbol("▸ ")
                .setMarginLeft(15f)
                .setFont(regularFont)
                .setFontSize(10f)
                .setMarginBottom(3f)
                .setMarginBottom(10f)

            exp.description.forEach { desc ->
                val listItem = ITextListItem(desc) // constructor(text: String)
                    .setFont(regularFont)
                    .setFontSize(10f)
                    .setMarginBottom(3f)
                descriptionList.add(desc)
            }

            expCell.add(descriptionList)

            // Technologies avec badges
            if (exp.technologies.isNotEmpty()) {
                val techParagraph = Paragraph()
                    .setMarginBottom(0f)

                exp.technologies.forEach { tech ->
                    val badge = Paragraph(tech)
                        .setBackgroundColor(primaryColor)
                        .setFontColor(ColorConstants.WHITE)
                        .setFont(regularFont)
                        .setFontSize(8f)
                        .setMargin(0f)
                        .setPadding(2f)
                        .setFixedLeading(10f)

                    techParagraph.add(badge)
                    techParagraph.add(Text(" "))
                }

                expCell.add(techParagraph)
            }

            expContainer.addCell(expCell)
            document.add(expContainer)
        }
    }

    private fun addEducationSection(document: Document, educations: List<Education>) {
        if (educations.isEmpty()) return

        val educationTitle = createModernSectionTitle("FORMATION")
        document.add(educationTitle)

        educations.forEach { edu ->
            val eduContainer = Table(1)
                .setWidth(UnitValue.createPercentValue(100f))
                .setMarginBottom(5f)

            val eduCell = Cell()
                .setBorder(SolidBorder(lightGray, 1f))
                .setPadding(12f)

            // En-tête formation
            val headerTable = Table(UnitValue.createPercentArray(floatArrayOf(3f, 1f)))
                .setWidth(UnitValue.createPercentValue(100f))
                .setMarginBottom(5f)

            val titleCell = Cell()
                .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER)
                .add(Paragraph("${edu.degree} en ${edu.fieldOfStudy}")
                    .setFont(boldFont).setFontSize(12f).setBold().setFontColor(primaryColor))
                .add(Paragraph("${edu.institution}")
                    .setFont(regularFont).setFontSize(10f).setFontColor(accentColor))

            val dateCell = Cell()
                .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER)
                .setTextAlignment(TextAlignment.RIGHT)
                .add(Paragraph("${edu.startDate} - ${edu.endDate}")
                    .setFont(regularFont).setFontSize(10f).setFontColor(darkGray))

            headerTable.addCell(titleCell)
            headerTable.addCell(dateCell)
            eduCell.add(headerTable)

            // Détails supplémentaires
            edu.gpa?.let { gpa ->
                eduCell.add(Paragraph("Moyenne: $gpa")
                    .setFont(regularFont)
                    .setFontSize(10f)
                    .setFontColor(primaryColor)
                    .setMarginBottom(3f))
            }

            edu.description?.let { desc ->
                eduCell.add(Paragraph(desc)
                    .setFont(regularFont)
                    .setFontSize(10f)
                    .setMarginBottom(0f))
            }

            eduContainer.addCell(eduCell)
            document.add(eduContainer)
        }
    }

    private fun addSkillsAndLanguagesSection(document: Document, skills: List<String>, languages: List<Language>) {
        if (skills.isEmpty() && languages.isEmpty()) return

        // Section à deux colonnes
        val twoColumnTable = Table(UnitValue.createPercentArray(floatArrayOf(1f, 1f)))
            .setWidth(UnitValue.createPercentValue(100f))
            .setMarginBottom(5f)

        // Colonne Compétences
        val skillsCell = Cell()
            .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER)
            .setPaddingRight(10f)

        if (skills.isNotEmpty()) {
            skillsCell.add(createModernSectionTitle("COMPÉTENCES").setMarginBottom(10f))

            // Grille de compétences avec style moderne
            val skillsGrid = Table(2)
                .setWidth(UnitValue.createPercentValue(100f))

            skills.chunked(2).forEach { chunk ->
                chunk.forEach { skill ->
                    val skillCell = Cell()
                        .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER)
                        .setBackgroundColor(lightGray)
                        .setPadding(8f)
                        .setMargin(2f)
                        .add(Paragraph(skill)
                            .setFont(regularFont)
                            .setFontSize(10f)
                            .setTextAlignment(TextAlignment.CENTER))
                    skillsGrid.addCell(skillCell)
                }
                // Compléter la ligne si nécessaire
                if (chunk.size == 1) {
                    skillsGrid.addCell(Cell().setBorder(com.itextpdf.layout.borders.Border.NO_BORDER))
                }
            }
            skillsCell.add(skillsGrid)
        }

        // Colonne Langues
        val languagesCell = Cell()
            .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER)
            .setPaddingLeft(10f)

        if (languages.isNotEmpty()) {
            languagesCell.add(createModernSectionTitle("LANGUES").setMarginBottom(10f))

            languages.forEach { lang ->
                val langContainer = Table(UnitValue.createPercentArray(floatArrayOf(2f, 1f)))
                    .setWidth(UnitValue.createPercentValue(100f))
                    .setMarginBottom(5f)

                val langNameCell = Cell()
                    .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER)
                    .add(Paragraph(lang.name).setFont(regularFont).setFontSize(10f))

                val langLevelCell = Cell()
                    .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .add(Paragraph(lang.level)
                        .setFont(regularFont)
                        .setFontSize(9f)
                        .setFontColor(primaryColor)
                        .setBold())

                langContainer.addCell(langNameCell)
                langContainer.addCell(langLevelCell)
                languagesCell.add(langContainer)
            }
        }

        twoColumnTable.addCell(skillsCell)
        twoColumnTable.addCell(languagesCell)
        document.add(twoColumnTable)
    }

    private fun addCertificationsSection(document: Document, certifications: List<Certification>) {
        if (certifications.isEmpty()) return

        // Titre de la section
        val certificationsTitle = createModernSectionTitle("CERTIFICATIONS")
        document.add(certificationsTitle)

        // Grille 2 colonnes
        val certGrid = Table(2)
            .setWidth(UnitValue.createPercentValue(100f))
            .setMarginBottom(5f)

        certifications.chunked(2).forEach { chunk ->
            chunk.forEach { cert ->
                val certCell = Cell()
                    .setBorder(SolidBorder(lightGray, 1f))
                    .setPadding(10f)
                    .setMargin(2f)

                // Nom de la certification
                val certTitle = Paragraph(cert.name)
                    .setFont(boldFont)
                    .setFontSize(11f)
                    .setFontColor(primaryColor)
                    .setMarginBottom(3f)
                certCell.add(certTitle)

                // Émetteur
                val certIssuer = Paragraph(cert.issuer)
                    .setFont(regularFont)
                    .setFontSize(10f)
                    .setFontColor(accentColor)
                    .setMarginBottom(3f)
                certCell.add(certIssuer)

                // Détails (date + expiration éventuelle)
                val certDetails = StringBuilder("🗓 ${cert.date}")
                cert.expirationDate?.let { exp -> certDetails.append(" (Expire le: $exp)") }

                val certDetailsParagraph = Paragraph(certDetails.toString())
                    .setFont(regularFont)
                    .setFontSize(9f)
                    .setFontColor(darkGray)
                certCell.add(certDetailsParagraph)

                certGrid.addCell(certCell)
            }

            // Ajouter une cellule vide si ligne incomplète
            if (chunk.size == 1) {
                certGrid.addCell(Cell().setBorder(Border.NO_BORDER))
            }
        }

        document.add(certGrid)
    }

    private fun addProjectsSection(document: Document, projects: List<Project>) {
        if (projects.isEmpty()) return

        // Titre de la section
        val projectsTitle = createModernSectionTitle("PROJETS")
        document.add(projectsTitle)

        // Grille 2 colonnes
        val projectGrid = Table(2)
            .setWidth(UnitValue.createPercentValue(100f))
            .setMarginBottom(5f)

        projects.chunked(2).forEach { chunk ->
            chunk.forEach { project ->
                val projectCell = Cell()
                    .setBorder(SolidBorder(lightGray, 1f))
                    .setPadding(10f)
                    .setMargin(2f)

                // Nom du projet
                val title = Paragraph(project.name)
                    .setFont(boldFont)
                    .setFontSize(11f)
                    .setFontColor(primaryColor)
                    .setMarginBottom(3f)
                projectCell.add(title)

                // Date
                val date = Paragraph("📅 ${project.date}")
                    .setFont(regularFont)
                    .setFontSize(9f)
                    .setFontColor(darkGray)
                    .setMarginBottom(3f)
                projectCell.add(date)

                // Description
                val desc = Paragraph(project.description)
                    .setFont(regularFont)
                    .setFontSize(10f)
                    .setMarginBottom(4f)
                projectCell.add(desc)

                // Technologies utilisées
                if (project.technologies.isNotEmpty()) {
                    val tech = Paragraph("🛠 ${project.technologies.joinToString(", ")}")
                        .setFont(regularFont)
                        .setFontSize(9f)
                        .setFontColor(accentColor)
                        .setMarginBottom(4f)
                    projectCell.add(tech)
                }

                // Lien du projet
                project.url?.let { url ->
                    val link = Paragraph("🔗 $url")
                        .setFont(regularFont)
                        .setFontSize(9f)
                        .setFontColor(ColorConstants.BLUE)
                        .setUnderline()
                    projectCell.add(link)
                }

                projectGrid.addCell(projectCell)
            }

            // Compléter la ligne si elle n’a qu’un seul projet
            if (chunk.size == 1) {
                projectGrid.addCell(Cell().setBorder(Border.NO_BORDER))
            }
        }

        document.add(projectGrid)
    }


    // Fonctions utilitaires
    private fun createModernSectionTitle(title: String): Paragraph {
        return Paragraph(title)
            .setFont(boldFont)
            .setFontSize(14f)
            .setBold()
            .setFontColor(accentColor)
            .setMarginTop(25f)
            .setMarginBottom(15f)
            .setBorderLeft(SolidBorder(primaryColor, 4f))
            .setPaddingLeft(10f)
}

private fun createContactCell(text: String): Cell {
    return Cell()
        .add(Paragraph(text).setFont(regularFont).setFontSize(10f))
        .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER)
        .setTextAlignment(TextAlignment.CENTER)
}

private fun createSkillCell(skill: String): Cell {
    return Cell()
        .add(Paragraph(skill).setFont(regularFont).setFontSize(10f))
        .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER)
        .setPadding(5f)
}

}


