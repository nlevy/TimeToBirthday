package com.nirlevy.timetobirthday.data

import com.nirlevy.timetobirthday.dao.PersonEntity

class PersonTransformer {

    fun toPersonEntity(person: Person): PersonEntity {
        return PersonEntity(person.id, person.name, person.day, person.month, person.gender)
    }

    fun toPersonList(entities: List<PersonEntity>): List<Person> {
        return entities.map(this::toPerson)
    }

    private fun toPerson(personEntity: PersonEntity): Person {
        return Person(personEntity.id, personEntity.name, personEntity.day, personEntity.month, personEntity.gender)
    }
}