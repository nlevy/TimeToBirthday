package com.nirlevy.timetobirthday

import android.app.Application
import com.nirlevy.timetobirthday.dao.PersonDatabase
import com.nirlevy.timetobirthday.data.PersonTransformer

class TTBApplication: Application() {

    val db by lazy {
        PersonDatabase.getInstance(this)
    }

    val transformer = PersonTransformer()
}