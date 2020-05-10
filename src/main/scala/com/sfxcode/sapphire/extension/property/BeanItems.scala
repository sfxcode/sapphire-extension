package com.sfxcode.sapphire.extension.property

import java.util

import com.sfxcode.sapphire.core.CollectionExtensions._
import com.sfxcode.sapphire.core.value._
import javafx.collections.{ FXCollections, ObservableList }
import scala.reflect.runtime.universe._
import scala.collection.JavaConverters._

class BeanItems(bean: FXBean[_ <: AnyRef]) {
  private val itemBuffer = FXCollections.observableArrayList[BeanItem]()

  def getItems: ObservableList[BeanItem] = itemBuffer

  def addItem(
    key: String,
    name: String = "",
    category: String = "",
    description: String = "",
    editable: Boolean = true,
    clazz: Class[_] = EmptyBeanItemClass.ClazzOf): BeanItem = {
    val beanItem = BeanItem(bean, key, name, category, description, editable, clazz)
    itemBuffer.add(beanItem)
    beanItem
  }

  def addItems[T <: AnyRef](implicit t: TypeTag[T]): Unit = {
    val symbols = ReflectionTools.getMembers[T]()
    symbols.foreach { s =>
      val key = s.name.toString
      addItem(key)
    }
  }

  def addItemsFromMap(scalaMap: Map[String, Any]): Unit =
    scalaMap.keys.foreach { key =>
      val value = scalaMap.get(key)
      value.foreach { value =>
        addItem(key, clazz = value.getClass)
      }
    }

  def updateBean(bean: FXBean[_ <: AnyRef]) {
    itemBuffer.foreach(item => item.bean = bean)
  }

  def beanItem(key: String): Option[BeanItem] =
    itemBuffer.asScala.find(item => key.equals(item.getKey))

}

object BeanItems {

  def apply(bean: FXBean[_ <: AnyRef] = FXBean(new util.HashMap())): BeanItems = new BeanItems(bean)
}
