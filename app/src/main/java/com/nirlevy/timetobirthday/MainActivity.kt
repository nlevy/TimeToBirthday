package com.nirlevy.timetobirthday

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.nirlevy.timetobirthday.dao.PersonDatabaseHandler
import com.nirlevy.timetobirthday.data.Gender
import com.nirlevy.timetobirthday.data.Person
import com.nirlevy.timetobirthday.data.TimeUnit
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {

    lateinit var personDatabaseHandler: PersonDatabaseHandler
    lateinit var person :Person
    var timeUnit = TimeUnit.values()[0]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        personDatabaseHandler = PersonDatabaseHandler(this)
        initTimeUnits()
        settings.setOnClickListener {
            startActivity(Intent(this, PersonListActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        val people = personDatabaseHandler.getAll()
        initPeople(people)
        person = people[0]
    }

    private fun initTimeUnits() {
        ArrayAdapter(this, android.R.layout.simple_spinner_item, TimeUnit.values())
            .also { adapter ->
                adapter.setDropDownViewResource(R.layout.spinner_item)
                timeUnits.adapter = adapter
            }

        timeUnits.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                timeUnit = parent?.selectedItem as TimeUnit
                calculateBirthday()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun initPeople(people: ArrayList<Person>) {
        if (people.isEmpty()) {
            people.add(Person(0,"DEMO", 1, 1, Gender.GIRL))
        }
        ArrayAdapter(this, android.R.layout.simple_spinner_item, people)
            .also { adapter ->
                adapter.setDropDownViewResource(R.layout.spinner_item)
                names.adapter = adapter
            }
        names.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                person = parent?.selectedItem as Person
                calculateBirthday()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    fun calculateBirthday() {
        val nextBirthday = person.nextBirthday
        val now = LocalDateTime.now()
        result.text =
            getString(R.string.result, now.until(nextBirthday, timeUnit.unit), timeUnit, person)
        main.background = ContextCompat.getDrawable(this, person.gender.color)
    }
}

