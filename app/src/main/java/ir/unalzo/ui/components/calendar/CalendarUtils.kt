package ir.unalzo.ui.components.calendar

import com.aminography.primecalendar.persian.PersianCalendar
import com.aminography.primecalendar.persian.PersianCalendarUtils
import ir.unalzo.ui.components.Time
import java.util.Calendar
import java.util.Locale


data class Day(
    val year: Int,
    val month: Int,
    val day: Int
) {
    override fun toString(): String {
        return "$year/$month/$day"
    }
}

fun Day.isClosed(): Boolean {
    val calendar = PersianCalendar()
    calendar.set(year, month, day)
    val dayName =
        calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()).orEmpty()
    return dayName.startsWith("ج")
}

fun Day.isToday(): Boolean {
    val today = PersianCalendar()

    return today.dayOfMonth == day && today.month == month && today.year == year
}

fun getToday(): Day {
    val today = PersianCalendar()
    return Day(
        year = today.year,
        month = today.month,
        day = today.dayOfMonth
    )
}

fun getMonthLength(year: Int, month: Int): Int {
    return if (month == 11) {
        if (isLeapYear(year)) 30 else 29
    } else {
        PersianCalendarUtils.monthLength(year, month)
    }
}

private fun isLeapYear(year: Int): Boolean {
    val matches = arrayOf(1, 5, 9, 13, 17, 22, 26, 30)
    val modulus = year - (year / 33) * 33
    var isLeapYear = false

    for (i in 0 until 8) {
        if (matches[i] == modulus) {
            isLeapYear = true
        }
    }

    return isLeapYear
}

fun getCurrentTime(): Time {
    val today = PersianCalendar()
    return Time(
        hour = today.hourOfDay,
        minutes = today.minute
    )
}