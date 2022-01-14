package com.nirlevy.timetobirthday.data

import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime

class PersonTest {

    @Test
    fun nextBirthday_nextYear() {
        val now = LocalDateTime.now()
        val yesterday = now.minusDays(1)
        val person = Person(1, "name", yesterday.dayOfMonth, yesterday.monthValue, Gender.BOY)

        val nextBirthday = person.nextBirthday

        assertEquals(LocalDate.from(yesterday).plusYears(1), LocalDate.from(nextBirthday))
    }

    @Test
    fun nextBirthday_thisYear() {
        val now = LocalDateTime.now()
        val tomorrow = now.plusDays(1)
        val person = Person(1, "name", tomorrow.dayOfMonth, tomorrow.monthValue, Gender.BOY)

        val nextBirthday = person.nextBirthday

        assertEquals(LocalDate.from(tomorrow), LocalDate.from(nextBirthday))
    }
}