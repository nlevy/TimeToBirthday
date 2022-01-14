package com.nirlevy.timetobirthday.dao

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.nirlevy.timetobirthday.data.Gender
import com.nirlevy.timetobirthday.data.Person

class PersonDatabaseHandler(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "TimeToBirthdayDatabase"
        private const val TABLE_PERSON = "PersonTable"

        private const val KEY_ID = "_id"
        private const val KEY_NAME = "name"
        private const val KEY_DAY = "day"
        private const val KEY_MONTH = "month"
        private const val KEY_GENDER = "gender"
        private const val CREATE_TABLE_STATEMENT = """CREATE TABLE $TABLE_PERSON (
                $KEY_ID INTEGER PRIMARY KEY,
                $KEY_NAME TEXT,
                $KEY_DAY INTEGER,
                $KEY_MONTH INTEGER,
                $KEY_GENDER TEXT
                )"""
        private const val DROP_TABLE_STATEMENT = "DROP TABLE IF EXISTS $TABLE_PERSON"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_STATEMENT)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(DROP_TABLE_STATEMENT)
        onCreate(db)
    }

    fun add(person: Person): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, person.name)
        contentValues.put(KEY_DAY, person.day)
        contentValues.put(KEY_MONTH, person.month)
        contentValues.put(KEY_GENDER, person.gender.name)
        val success = db.insert(TABLE_PERSON, null, contentValues)

        db.close()
        return success
    }

    fun getAll(): ArrayList<Person> {
        val people: ArrayList<Person> = ArrayList()
        val selectQuery = "SELECT  * FROM $TABLE_PERSON"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val person = Person(id = cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                    name = cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                    day = cursor.getInt(cursor.getColumnIndex(KEY_DAY)),
                    month = cursor.getInt(cursor.getColumnIndex(KEY_MONTH)),
                    gender = Gender.valueOf(cursor.getString(cursor.getColumnIndex(KEY_GENDER))))

                people.add(person)
            } while (cursor.moveToNext())
        }
        cursor.close()

        return people
    }

    fun update(person: Person) : Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, person.name)
        contentValues.put(KEY_DAY, person.day)
        contentValues.put(KEY_MONTH, person.month)
        contentValues.put(KEY_GENDER, person.gender.name)
        val success = db.update(TABLE_PERSON, contentValues, "$KEY_ID=${person.id}", null)
        db.close()

        return success
    }

    fun delete(person: Person): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, person.id)
        val success = db.delete(TABLE_PERSON, "$KEY_ID=${person.id}", null)
        db.close()

        return success
    }
}