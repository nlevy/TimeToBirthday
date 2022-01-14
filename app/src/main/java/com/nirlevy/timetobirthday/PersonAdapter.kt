package com.nirlevy.timetobirthday

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.nirlevy.timetobirthday.dao.PersonDatabaseHandler
import com.nirlevy.timetobirthday.data.Person
import kotlinx.android.synthetic.main.person_row.view.*

class PersonAdapter(private val context: Context, private val personDatabaseHandler: PersonDatabaseHandler, private val items: List<Person>) :
    RecyclerView.Adapter<PersonAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.person_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val person = items[position]

        holder.tvName.text = person.name
        holder.tvBirthday.text = context.getString(R.string.dateTemplate, person.day, person.month)
        holder.ivGender.setImageDrawable(ContextCompat.getDrawable(context, person.gender.icon))

        val color =if (position % 2 == 0) {
             R.color.colorLightGray
        } else {
            R.color.colorWhite
        }
        holder.llMain.setBackgroundColor(ContextCompat.getColor(context, color))
        holder.ivEdit.setOnClickListener {
            val intent = Intent(it.context, AddPersonActivity::class.java)
            intent.putExtra("name", person.name)
            intent.putExtra("day", person.day)
            intent.putExtra("month", person.month)
            intent.putExtra("gender", person.gender.name)
            intent.putExtra("id", person.id)

            it.context.startActivity(intent)
        }

        holder.ivDelete.setOnClickListener {
            personDatabaseHandler.delete(person)
            (context as PersonListActivity).buildList()
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val llMain: LinearLayout = view.llMain
        val tvName: TextView = view.tvName
        val tvBirthday: TextView = view.tvBirthday
        val ivGender: ImageView = view.ivGender
        val ivEdit: ImageView = view.ivEdit
        val ivDelete: ImageView = view.ivDelete
    }

}