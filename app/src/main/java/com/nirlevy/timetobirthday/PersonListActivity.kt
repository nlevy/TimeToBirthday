package com.nirlevy.timetobirthday

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.nirlevy.timetobirthday.dao.PersonDao
import com.nirlevy.timetobirthday.dao.PersonEntity
import com.nirlevy.timetobirthday.data.PersonTransformer
import com.nirlevy.timetobirthday.databinding.ActivityPersonsListBinding
import kotlinx.android.synthetic.main.activity_persons_list.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PersonListActivity : AppCompatActivity()  {

    private lateinit var binding: ActivityPersonsListBinding
    private lateinit var personDao: PersonDao
    private lateinit var transformer: PersonTransformer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonsListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(toolbar_persons_list)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        personDao = (application as TTBApplication).db.personDao()
        transformer = (application as TTBApplication).transformer
        toolbar_persons_list.setNavigationOnClickListener { onBackPressed() }
        addButton.setOnClickListener {
            startActivity(Intent(this, AddPersonActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        buildList()
    }

    private fun buildList() {
        lifecycleScope.launch {
            personDao.getAll().collect {
                if (it.isNotEmpty()) {
                    val people = transformer.toPersonList(it)
                    rvPeopleList.visibility = View.VISIBLE
                    rvPeopleList.layoutManager = LinearLayoutManager(this@PersonListActivity)
                    val itemAdapter = PersonAdapter(
                        this@PersonListActivity,
                        this@PersonListActivity::deletePerson,
                        people
                    )
                    rvPeopleList.adapter = itemAdapter
                } else {
                    rvPeopleList.visibility = View.GONE
                }
            }
        }
    }

    private fun deletePerson(id: Int) {
        lifecycleScope.launch {
            personDao.delete(PersonEntity(id))
            buildList()
        }
    }
}