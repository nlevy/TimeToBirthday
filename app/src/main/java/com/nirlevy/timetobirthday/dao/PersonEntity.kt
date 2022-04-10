package com.nirlevy.timetobirthday.dao

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nirlevy.timetobirthday.data.Gender

@Entity(tableName = "person-table")
data class PersonEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = "",
    val day: Int = 0,
    val month: Int = 0,
    val gender: Gender = Gender.BOY
)
