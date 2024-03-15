package com.example.staffrotaapp

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class TimeTableDC @RequiresApi(Build.VERSION_CODES.O) constructor(
    val shiftID: String = "",
    val shiftDate: String = "", // Keep as String
    val startTime: String = "",
    val endTime: String = "",
    val shiftLength: Double = 0.0,
    val employeeData: String = ""
) {
    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        @JvmStatic
        fun parseLocalTime(timeString: String): LocalTime {
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            return LocalTime.parse(timeString, formatter)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        @JvmStatic
        fun formatLocalDate(date: LocalDate): String {
            return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }

        @RequiresApi(Build.VERSION_CODES.O)
        @JvmStatic
        fun parseLocalDate(dateString: String): LocalDate {
            return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }
    }
}
