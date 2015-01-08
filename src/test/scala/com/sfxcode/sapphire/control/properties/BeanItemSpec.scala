package com.sfxcode.sapphire.control.properties

import com.sfxcode.sapphire.control.test.{Friend, PersonDatabase}
import com.sfxcode.sapphire.core.value.FXBean
import com.typesafe.scalalogging.LazyLogging
import org.specs2.mutable.Specification


class BeanItemSpec extends Specification with LazyLogging {



  "BeanItem" should {

    "be created from name" in {
      val friend: FXBean[Friend] = PersonDatabase.testFriend(1)
      friend.bean.name must be equalTo "Black Whitaker"

      val nameItem = BeanItem(friend, "name")

      nameItem.setValue("ABC")

      friend.bean.name must be equalTo "ABC"

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
  }

}
