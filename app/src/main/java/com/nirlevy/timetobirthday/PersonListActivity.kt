package com.nirlevy.timetobirthday

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.nirlevy.timetobirthday.dao.PersonDatabaseHandler
import com.nirlevy.timetobirthday.data.Person
import kotlinx.android.synthetic.main.activity_persons_list.*

class PersonListActivity : AppCompatActivity()  {

    private val personDatabaseHandler: PersonDatabaseHandler = PersonDatabaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_persons_list)
        setSupportActionBar(toolbar_persons_list)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar_persons_list.setNavigationOnClickListener { onBackPressed() }
        addButton.setOnClickListener {
            startActivity(Intent(this, AddPersonActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        buildList()
    }

    fun buildList() {
        if (getItemsList().isNotEmpty()) {
            rvPeopleList.visibility = View.VISIBLE
            rvPeopleList.layoutManager = LinearLayoutManager(this)
            val itemAdapter = PersonAdapter(this, personDatabaseHandler, getItemsList())
            rvPeopleList.adapter = itemAdapter
        } else {
            rvPeopleList.visibility = View.GONE
        }
    }

    private fun getItemsList(): List<Person> {
        return personDatabaseHandler.getAll()
    }
}