package com.nirlevy.timetobirthday.data

import java.time.LocalDateTime

data class Person(val id: Int, val name: String, val day: Int, val month: Int, val gender: Gender) {

    var nextBirthday : LocalDateTime

    init {
        val now = LocalDateTime.now()
        nextBirthday  = LocalDateTime.of(now.year, month, day, 0, 0, 0)
        if (nextBirthday.isBefore(now)) {
            nextBirthday = nextBirthday.plusYears(1)
        }
    }

    override fun toString() = " $name (${day}/$month)"
}