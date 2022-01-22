package com.nirlevy.timetobirthday

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nirlevy.timetobirthday.dao.PersonDatabaseHandler
import com.nirlevy.timetobirthday.data.Gender
import com.nirlevy.timetobirthday.data.Person
import com.nirlevy.timetobirthday.data.TimeUnit
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {

    private val viewModel = AndroidViewModel(this.application)
    private var timeUntilBirthday: Long = 0
    private lateinit var personDatabaseHandler: PersonDatabaseHandler
    private lateinit var person :Person
    private var timeUnit = TimeUnit.values()[0]

    private var active = true

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
        startLiveUpdates()
    }

    override fun onPause() {
        super.onPause()
        this.active = false
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
                updateBirthdayFields()
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
                updateBirthdayFields()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    fun updateBirthdayFields() {
        calculateBirthday()
        main.background = ContextCompat.getDrawable(this, person.gender.color)
    }

    private fun calculateBirthday() {
        calculateBirthday(LocalDateTime.now())
    }

    private fun calculateBirthday(now : LocalDateTime) {
        println("in calculate")
        val nextBirthday = person.nextBirthday
        val until = now.until(nextBirthday, timeUnit.unit)
        if (until != timeUntilBirthday) {
            result.text = getString(R.string.result, until, timeUnit, person)
            timeUntilBirthday = until
            println("updating")
        }
    }

    private fun startLiveUpdates() {
        active = true
        flow {
            while (active) {
                emit(LocalDateTime.now())
                delay(1000)
            }
        }.onEach {
            calculateBirthday(it)
        }.launchIn(viewModel.viewModelScope)
    }
}

