package com.example.staffrotaapp

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
data class TimeTableDC(
    var shiftID: Int = 0,
    var shiftDate: LocalDate = LocalDate.MIN,
    var firstName: String = "",
    var lastName: String = "",
    var employeeId: Int = 0,
    var startTime: LocalTime = LocalTime.MIN,
    var endTime: LocalTime = LocalTime.MIN,
    var shiftLength: Double = 0.0
)