package com.daccvo.repository

import com.daccvo.utils.Constants.TAILLE2
import com.daccvo.utils.Constants.backgroundColor
import com.daccvo.utils.Constants.font
import com.daccvo.utils.Constants.fontBold
import com.daccvo.utils.Constants.taille
import com.daccvo.utils.Constants.taille1
import com.daccvo.utils.Constants.whileColor
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.borders.Border
import com.itextpdf.layout.borders.GrooveBorder
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.VerticalAlignment
import java.io.File


class GeneratePdfRepositoryImpl : GeneratePdfRepository {

    override suspend fun initialisation() {
        val espa = ""
        val para1 = Paragraph(espa)
        val outputPath = "pdfgenerer/sortie.pdf"
        val document = creationdoc(outputPath)

        document.add(creationTableEntete());
        document.add(creationTableauinfo());
        document.add(creationTableinfo2());
        document.add(creationTableau1());
        document.add(para1);
        document.add(creationTableau2());
        document.add(creationTableau4());
        document.add(creationTableau3());
        val fin =
            "Dans votre intérêt, et pour vous aider à faire valoir vos droits, conservez ce bulletin de paie sans limitation de durée. Informations complémentaires: www.service-public.fr"

        val para0 = Paragraph(fin)
        para0.setFont(font).setFontSize(8.5f)
        document.add(para0)

        document.close()
    }

    override suspend fun creationdoc(path : String): Document {
            val outputFile = File(path)
            outputFile.parentFile?.mkdirs()

            val writer = PdfWriter(outputFile)
            val pdfDocument = PdfDocument(writer)
            pdfDocument.defaultPageSize = PageSize.A4

            val document = Document(pdfDocument)
            document.setMargins(0f, 0f, 0f, 0f)

            return document
    }

    private suspend fun creationTableEntete(): Table {
        val colonetaille = floatArrayOf(300f, 300f)
        val table: Table = Table(colonetaille)

        //paramettre
        table.addCell(Cell(2, 0).add(Paragraph(" ")))
        table.addCell(Cell().add(Paragraph("BULLETIN DE SALAIRE")).setFont(fontBold).setFontSize(14f))
        table.addCell(Cell().add(Paragraph("Période: Janvier 2025")).setFont(fontBold).setFontSize(taille))
        return table
    }

    private suspend fun creationTableauinfo(): Table {
        val colonetaille = floatArrayOf(120f, 100f, 150f, 300f, 200f)
        val table = Table(colonetaille)

        val imgSrc = "images/tayc.png"
        val data = ImageDataFactory.create(imgSrc)
        val image1 = Image(data)



        //1l
        table.addCell(
            Cell().add(Paragraph(
                """
        Siret:
        Urssaf/Msa:
        """.trimIndent()
            )).setFont(font).setFontSize(taille)
        )
        table.addCell(Cell().add(Paragraph("Code Naf:")).setFont(font).setFontSize(taille).setTextAlignment(TextAlignment.RIGHT))
        table.addCell(Cell().add(Paragraph(" ")))
        table.addCell(Cell().add(Paragraph(" ")))
        table.addCell(Cell().add(Paragraph(" ")))


        //2
        table.addCell(
            Cell().add(Paragraph(
                """
        Matricule: 
        N NSS:
        """.trimIndent()
            )).setFont(font).setFontSize(taille).setTextAlignment(TextAlignment.RIGHT)
        )
        table.addCell(Cell().add(Paragraph(" ")))
        table.addCell(Cell().add(Paragraph(" ")))
        table.addCell(Cell().add(Paragraph(" ")))
        table.addCell(Cell().add(Paragraph(" ")))


        //3
        table.addCell(
            Cell().add(Paragraph(
                """
        Emploi: 
        Statut professionnel:
        Niveau: 
        """.trimIndent()
            )).setFont(font).setFontSize(taille).setTextAlignment(TextAlignment.RIGHT)
        )
        table.addCell(
            Cell().add(Paragraph(
                """
                Employé
                Employé
                2N
                """.trimIndent()
            )).setFont(fontBold).setFontSize(taille)
        )
        table.addCell(Cell().add(Paragraph(" ")))
        table.addCell(
            Cell(2, 0).add(Paragraph(
                """
                NGOUBENE EWANE JACK JUNIOR 
                112 Rue Perdue
                """.trimIndent()
            )).setFont(font).setFontSize(14f).setFontColor(whileColor).setBackgroundColor(backgroundColor)
                .setBorderTop(Border.NO_BORDER)
        )
        image1.scaleToFit(70f, 200f)
        table.addCell(Cell(2, 0).add(image1)).setPadding(5f)


        //4
        table.addCell(
            Cell().add(Paragraph(
                """
        Niveau:
        Ancienneté:
        """.trimIndent()
            )).setFont(font).setFontSize(taille).setTextAlignment(TextAlignment.RIGHT)
        )
        table.addCell(Cell().add(Paragraph(" ")))
        table.addCell(Cell().add(Paragraph(" ")))


        //5
        table.addCell(
            Cell().add(Paragraph("Convention collective:")).setFont(font).setFontSize(taille).setTextAlignment(TextAlignment.RIGHT)
        )
        table.addCell(Cell().add(Paragraph(" ")))
        table.addCell(Cell().add(Paragraph(" ")))
        table.addCell(Cell().add(Paragraph(" ")))
        table.addCell(Cell().add(Paragraph(" ")))


        return table
    }

    private suspend fun creationTableinfo2(): Table {
        val colonetaille = floatArrayOf(100f, 100f, 100f, 100f, 100f, 100f, 100f)
        val table = Table(colonetaille)

        //paramettre
        for (i in 0..2) {
            for (j in colonetaille.indices) {
                table.addCell(Cell().add(Paragraph("test")).setFont(font).setFontSize(taille))
            }
        }

        return table
    }

    private suspend fun creationTableau1(): Table {

        val tex = "2600.00" + "  " + "70000" + "  " + "182.00"
        val colonetaille = floatArrayOf(300f, 100f, 100f, 100f, 100f, 200f)
        val entete = arrayOf(
            "Eléments de paie", "Base", "Taux", "A déduire", "A payer", "Charges patronales"
        )
        val elementPaye = arrayOf(
            "Salaire de base", "Salaire brut", "Santé", "Sécurité Sociale - Mal. Mat Inval. Décès",
            "Complémentaire - Santé", "Accidents du travail & mal. professionnelle:", "Retraite", "Sécurité Sociale plafonnée",
            "Sécurité Sociale déplafonnée", "Complémentaire Tranche 1", "Famille", "Assurance chômage",
            "Autres contributions dues par l'employeur", "Autres contributions dues par l'employeur",
            "CSG deduet de l'impôt sur le revenu", "CSG/CRDS non déduet de l'impôt sur le revenu",
            "Exonérations de cotisations employeur", "Total des cotisations et contributions",
            "Montant net social", "Net à payer avant impôt sur le revenu",
            "dont évolution de la rémunération liée à la \nsuppression des cotisations chômage et maladie",
            "Impôt sur le revenu prélevé à la source - PAS\nTaux non personnalisé",
            "Impôt sur le revenu: cumul PAS annuel", "Net payé"
        )

        val base = arrayOf(
            "151.67", "", "", "", "", "", "", "2600.00", "2600.00", "2600.0", "", "", "", "", "2554.50", "2554.50", "", "", "2058.15", "",
            "38.47", "2132.23", "87.42", "1"
        )
        val taux = arrayOf(
            "17.1425", "", "", "", "", "", "", "69000", "04000", "40100", "", "", "", "", "68000", "29000", "", "", "", "",
            "", "", "", "1"
        )
        val deduire = arrayOf(
            "", "", "", "", "", "", "", "179.40", "10.40", "104.26", "", "", "", "", "173.71", "74.08", "", "541.81", "", "",
            "", "", "", "1"
        )
        val payer = arrayOf(
            "2600.00", "2600.00", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "2058.15",
            "", "", "", "1970.73"
        )
        val patronales = arrayOf(
            "", "", "", tex, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "1"
        )

        val table = Table(colonetaille)
        val border1: Border = GrooveBorder(DeviceRgb(255, 255, 255).numberOfComponents.toFloat())

        val taille2 = 8f
        for (i in 0 until entete.size) {
            table.addCell(
                Cell().add(Paragraph(entete[i])).setFont(font).setFontSize(taille1).setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(whileColor).setBackgroundColor(DeviceRgb(182, 102, 210)).setBorderTop(
                        Border.NO_BORDER
                    ).setBorderBottom(Border.NO_BORDER)
            )
        }

        for (j in 0 until elementPaye.size) {

            if(j == 19){
                table.addCell(
                    Cell().add(Paragraph(elementPaye[j])).setFont(fontBold).setFontSize(taille1)
                        .setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER)
                )
                table.addCell(
                    Cell().add(Paragraph(base[j])).setFont(font).setFontSize(taille2).setTextAlignment(TextAlignment.RIGHT)
                        .setBorderTop(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER)
                )
                table.addCell(
                    Cell().add(Paragraph(taux[j])).setFont(font).setFontSize(taille2).setTextAlignment(TextAlignment.RIGHT)
                        .setBorderTop(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER)
                )
                table.addCell(
                    Cell().add(Paragraph(deduire[j])).setFont(font).setFontSize(taille2).setTextAlignment(TextAlignment.RIGHT)
                        .setBorderTop(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER)
                )
                table.addCell(
                    Cell().add(Paragraph(payer[j])).setFont(fontBold).setFontSize(taille1).setTextAlignment(TextAlignment.RIGHT)
                        .setBorderTop(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER)
                )
                table.addCell(
                    Cell().add(Paragraph(patronales[j])).setFont(font).setFontSize(taille2).setTextAlignment(TextAlignment.RIGHT)
                        .setBorderTop(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER)
                )

                j + 1
            }
            table.addCell(
                Cell().add(Paragraph(elementPaye[j])).setFont(font).setFontSize(taille2).setTextAlignment(TextAlignment.CENTER)
                    .setBorder(Border.NO_BORDER)
            )
            table.addCell(
                Cell().add(Paragraph(base[j])).setFont(font).setFontSize(taille2).setTextAlignment(TextAlignment.RIGHT)
                    .setBorderTop(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER)
            )
            table.addCell(
                Cell().add(Paragraph(taux[j])).setFont(font).setFontSize(taille2).setTextAlignment(TextAlignment.RIGHT)
                    .setBorderTop(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER)
            )
            table.addCell(
                Cell().add(Paragraph(deduire[j])).setFont(font).setFontSize(taille2).setTextAlignment(TextAlignment.RIGHT)
                    .setBorderTop(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER)
            )
            table.addCell(
                Cell().add(Paragraph(payer[j])).setFont(font).setFontSize(taille2).setTextAlignment(TextAlignment.RIGHT)
                    .setBorderTop(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER)
            )
            table.addCell(
                Cell().add(Paragraph(patronales[j])).setFont(font).setFontSize(taille2).setTextAlignment(TextAlignment.RIGHT)
                    .setBorderTop(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER)
            )

        }

        return table;
    }

    private suspend fun creationTableau2() : Table {

        val colonneTaille = floatArrayOf(100f, 200f, 200f, 200f, 200f, 200f, 200f, 200f, 200f, 200f)

        val entete = arrayOf(
            " ", "Heures", "Heures suppl.", "Brut", "Plafond S.S.", "Net imposable",
            "Ch. patronales", "Coût Global", "Total versé", "Allègements"
        )

        val elementPaye = arrayOf("Mensuel", "Annuel")

        val mensuel = arrayOf("", "", "", "", "", "", "", "", "")
        val annuel = arrayOf("", "", "", "", "", "", "", "", "")

        val table = Table(colonneTaille)
        val border1: Border = GrooveBorder(DeviceRgb(255, 255, 255).numberOfComponents.toFloat())


        for(i in 0 until entete.size) {
            table.addCell(
                Cell().add(Paragraph(entete[i])).setFont(font).setFontSize(taille).setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(ColorConstants.WHITE).setBackgroundColor(DeviceRgb(182, 102, 210))
            )
        }
        table.addCell(
            Cell().add(Paragraph(elementPaye[0])).setFont(font).setFontSize(TAILLE2).setTextAlignment(TextAlignment.CENTER)
        )

        for(i in 0 until mensuel.size) {
            table.addCell(
                Cell().add(Paragraph(mensuel[i])).setFont(font).setFontSize(TAILLE2).setTextAlignment(TextAlignment.RIGHT)
            )
        }
        table.addCell(
            Cell().add(Paragraph(elementPaye[1])).setFont(font).setFontSize(TAILLE2).setTextAlignment(TextAlignment.CENTER)
        )

        for(i in 0 until annuel.size) {
            table.addCell(
                Cell().add(Paragraph(annuel[i])).setFont(font).setFontSize(TAILLE2).setTextAlignment(TextAlignment.RIGHT)
            )
        }

        return table

    }

    private suspend fun creationTableau3() : Table {
        val colonneTaille = floatArrayOf(100f, 200f, 200f, 200f, 200f, 200f, 200f, 500f)

        val entete = arrayOf("", "Congés N-1", "Congés N", "", "", "", "", "", "", "")
        val elementPaye = arrayOf("Acquis\nPris", "Solde")

        val pris = arrayOf("", "", "", "", "", "", "Net payé: 1970.73 euros")
        val solde = arrayOf("", "", "", "", "", "", "Paiement le 31/01/2024 par Virement")

        val table = Table(colonneTaille)

        val border1 = Border.NO_BORDER


        table.addCell(
            Cell().add(Paragraph(elementPaye[0])).setFont(font).setFontSize(TAILLE2).setTextAlignment(TextAlignment.CENTER)
        )

        for(i in 0 until pris.size) {
            if (i == 6) {
                table.addCell(
                    Cell(0, 3).add(Paragraph(pris[i])).setFont(fontBold).setFontSize(taille1)
                        .setTextAlignment(TextAlignment.CENTER)
                )
                break
            }
            table.addCell(Cell().add(Paragraph(pris[i])).setFont(font).setFontSize(TAILLE2).setTextAlignment(TextAlignment.RIGHT))
        }

        table.addCell(
            Cell().add(Paragraph(elementPaye[1])).setFont(font).setFontSize(TAILLE2).setTextAlignment(TextAlignment.CENTER)
        )


        for(i in 0 until solde.size) {
            if (i == 6) {
                table.addCell(
                    Cell(0, 3).add(Paragraph(solde[i])).setFont(fontBold).setFontSize(TAILLE2)
                        .setTextAlignment(TextAlignment.CENTER)
                )
                break
            }
            table.addCell(Cell().add(Paragraph(solde[i])).setFont(font).setFontSize(TAILLE2).setTextAlignment(TextAlignment.RIGHT))
        }

        return table
    }

    private suspend fun creationTableau4() : Table {
        val colonneTaille = floatArrayOf(100f, 200f, 200f, 200f, 200f, 200f, 200f, 200f, 200f, 200f)
        val entete = arrayOf("", "Congés N-1", "Congés N", "", "", "", "", "", "", "")
        val elementPaye = arrayOf("Acquis\nPris", "Solde")

        val table = Table(colonneTaille)


        for(i in 0 until entete.size){
            table.addCell(
                Cell().add(Paragraph(entete[i])).setBorderTop(Border.NO_BORDER).setFont(font).setFontSize(taille)
                    .setTextAlignment(TextAlignment.CENTER).setFontColor(ColorConstants.WHITE)
                    .setBackgroundColor(DeviceRgb(182, 102, 210))
            )

        }

        return table
    }

}