package com.example.utils

import android.content.Context
import com.example.data.Person
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ExcelExporter {

    suspend fun exportPopulationToExcel(context: Context, populationList: List<Person>): File {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("PopulationData")

        // Create Header Row
        val headerRow = sheet.createRow(0)
        val headers = listOf(
            "ID", "Person UUID", "Household ID", "National ID", "Full Name",
            "Gender", "Birth Date", "Is Birth Year Only", "House Status",
            "Person Status", "Data Status", "Last Modified"
        )
        
        headers.forEachIndexed { index, header ->
            val cell = headerRow.createCell(index)
            cell.setCellValue(header)
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        // Write Data Rows
        populationList.forEachIndexed { rowIndex, person ->
            val row = sheet.createRow(rowIndex + 1)
            row.createCell(0).setCellValue(person.id.toDouble())
            row.createCell(1).setCellValue(person.personUuid)
            row.createCell(2).setCellValue(person.householdId.toDouble())
            row.createCell(3).setCellValue(person.nationalId ?: "")
            row.createCell(4).setCellValue(person.fullName)
            row.createCell(5).setCellValue(person.gender.name)
            row.createCell(6).setCellValue(person.birthDate?.toString() ?: "")
            row.createCell(7).setCellValue(if (person.isBirthYearOnly) "Yes" else "No")
            row.createCell(8).setCellValue(person.houseStatus.name)
            row.createCell(9).setCellValue(person.personStatus.name)
            row.createCell(10).setCellValue(person.dataStatus.name)
            row.createCell(11).setCellValue(dateFormat.format(Date(person.lastModified)))
        }

        // Auto-size columns
        headers.indices.forEach { sheet.autoSizeColumn(it) }

        // Save file
        val file = File(context.filesDir, "PopulationExport_${System.currentTimeMillis()}.xlsx")
        FileOutputStream(file).use { workbook.write(it) }
        workbook.close()
        
        return file
    }
}
