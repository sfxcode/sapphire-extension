package com.sfxcode.sapphire.extension.property

import com.sfxcode.sapphire.core.value.FXBean
import com.sfxcode.sapphire.extension.test.{Friend, Person, PersonDatabase}
import com.typesafe.scalalogging.LazyLogging
import org.specs2.mutable.Specification


class BeanItemSpec extends Specification with LazyLogging {


  "BeanItem" should {

    "be created with key property" in {
      val person: FXBean[Person] = PersonDatabase.testPerson(1)
      person.bean.name must be equalTo "Bowen Leon"

      val nameItem = BeanItem(person, "name")

      nameItem.setValue("ABC")

      person.bean.name must be equalTo "ABC"

      nameItem.getType.toString must be equalTo "class java.lang.String"

      val ageItem = BeanItem(person, "age")
      ageItem.getType.toString must be equalTo "class java.lang.Integer"

      val activeItem = BeanItem(person, "isActive")
      activeItem.getType.toString must be equalTo "class java.lang.Boolean"

    }

    "be created from bean" in {
      val friend: FXBean[Friend] = PersonDatabase.testFriend(2)

      val items = BeanItem.beanItems(friend)

      items must have size 2
      val nameItem = items(0)
      nameItem.getName must be equalTo "name"
      nameItem.getValue must be equalTo "Wendy Strong"

      val idItem = items(1)
      idItem.getName must be equalTo "id"
      idItem.getValue.asInstanceOf[Long] must be equalTo 2

    }

    "also be created from bean" in {
      val friend: FXBean[Friend] = PersonDatabase.testFriend(2)
      val beanItems = BeanItems(friend)

      beanItems.addItem("id")
      beanItems.addItem("name")

      val items = beanItems.getItems

      items must have size 2

    }

  }


}
