package com.abhisek.rednit.util

import java.util.*
import java.util.Calendar.*

object Helper {
    fun getYears(millis: Long): Int{
        if(millis == 0L)
            return 0;
        val a: Calendar = Calendar.getInstance().apply { timeInMillis = millis }
        val b: Calendar = Calendar.getInstance()
        var diff: Int = b.get(YEAR) - a.get(YEAR)
        if (a.get(MONTH) > b.get(MONTH) ||
            a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE)
        ) {
            diff--
        }
        return diff
    }
}