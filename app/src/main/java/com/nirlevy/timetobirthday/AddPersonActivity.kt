package com.nirlevy.timetobirthday

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.nirlevy.timetobirthday.dao.PersonDao
import com.nirlevy.timetobirthday.data.Gender
import com.nirlevy.timetobirthday.data.Person
import com.nirlevy.timetobirthday.data.PersonTransformer
import com.nirlevy.timetobirthday.databinding.ActivityAddPersonBinding
import kotlinx.android.synthetic.main.activity_add_person.*
import kotlinx.coroutines.launch

class AddPersonActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPersonBinding
    private lateinit var personDao: PersonDao
    private lateinit var transformer: PersonTransformer
    private var newPerson = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPersonBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(toolbar_add_person_activity)
        personDao = (application as TTBApplication).db.personDao()
        transformer = (application as TTBApplication).transformer
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar_add_person_activity.setNavigationOnClickListener { onBackPressed() }
        initDateSpinners()
        setPerson(intent)
        setMode(intent)
        butAdd.setOnClickListener { addPerson() }
        butCancel.setOnClickListener { onBackPressed() }
    }

    private fun setMode(intent: Intent) {
        newPerson = !intent.hasExtra("id")
    }

    private fun setPerson(intent: Intent) {
        setPerson(
            intent.getStringExtra("name"),
            intent.getIntExtra("day", 1),
            intent.getIntExtra("month", 1),
            intent.getStringExtra("gender")
        )
    }

    private fun setPerson(name: String?, personDay: Int?, personMonth: Int?, gender: String?) {
        name?.let { etName.text.replace(0, etName.text.length, it) }
        personDay?.let { day.setSelection(it - 1) }
        personMonth?.let { month.setSelection(it - 1) }
        if (gender == Gender.GIRL.name) {
            rg_gender.check(rb_girl.id)
        } else {
            rg_gender.check(rb_boy.id)
        }
    }


    private fun initDateSpinners() {
        ArrayAdapter(this, android.R.layout.simple_spinner_item, IntRange(1, 12).toList())
            .also { adapter ->
                adapter.setDropDownViewResource(R.layout.spinner_item)
                month.adapter = adapter
            }

        ArrayAdapter(this, android.R.layout.simple_spinner_item, IntRange(1, 31).toList())
            .also { adapter ->
                adapter.setDropDownViewResource(R.layout.spinner_item)
                day.adapter = adapter
            }
    }

    private fun addPerson() {
        val person = extractPerson()
        lifecycleScope.launch {
            person?.let {

                if (newPerson) {
                    personDao.insert(transformer.toPersonEntity(person))
                    onBackPressed()
                } else {
                    personDao.update(transformer.toPersonEntity(person))
                    onBackPressed()
                }
            }
        }
    }

    private fun extractPerson(): Person? {
        val name = etName.text.toString()
        if (name.isEmpty()) {
            Toast.makeText(this, "missing name", Toast.LENGTH_SHORT).show()
            return null
        }

        val gender = extractGender(rg_gender.checkedRadioButtonId)
        return Person(
            intent.getIntExtra("id", 0),
            name,
            day.selectedItem as Int,
            month.selectedItem as Int,
            gender
        )
    }

    private fun extractGender(checkedRadioButtonId: Int): Gender {
        return when (findViewById<RadioButton>(checkedRadioButtonId)) {
            rb_boy -> Gender.BOY
            rb_girl -> Gender.GIRL
            else -> throw IllegalArgumentException()
        }
    }
}
