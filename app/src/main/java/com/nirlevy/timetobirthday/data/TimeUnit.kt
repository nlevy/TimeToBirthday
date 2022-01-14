package com.nirlevy.timetobirthday.data

import java.time.temporal.ChronoUnit

enum class TimeUnit (private val displayName: String, val unit: ChronoUnit) {
    SECONDS("שניות", ChronoUnit.SECONDS),
    MINUTES("דקות", ChronoUnit.MINUTES),
    HOURS("שעות", ChronoUnit.HOURS),
    DAYS("ימים", ChronoUnit.DAYS);

    override fun toString() = displayName
}
