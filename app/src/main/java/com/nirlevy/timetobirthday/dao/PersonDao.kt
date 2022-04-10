package com.nirlevy.timetobirthday.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {
    @Insert
    suspend fun insert(personEntity: PersonEntity)

    @Update
    suspend fun update(personEntity: PersonEntity)

    @Delete
    suspend fun delete(personEntity: PersonEntity)

    @Query("select * from `person-table`")
    fun getAll(): Flow<List<PersonEntity>>

}
