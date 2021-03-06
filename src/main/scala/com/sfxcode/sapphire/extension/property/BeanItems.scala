package com.sfxcode.sapphire.extension.property

import com.sfxcode.sapphire.core.CollectionExtensions._
import com.sfxcode.sapphire.core.application.DefaultsTo
import com.sfxcode.sapphire.core.value._
import javafx.collections.{ FXCollections, ObservableList }

import scala.jdk.CollectionConverters._
import scala.reflect.runtime.universe._

class BeanItems[T <: AnyRef]() {
  private val itemBuffer = FXCollections.observableArrayList[BeanItem]()
  private var beanItemBean: FXBean[T] = _

  def getItems: ObservableList[BeanItem] = itemBuffer

  def addItem(
    key: String,
    name: String = "",
    category: String = "",
    description: String = "",
    editable: Boolean = true,
    clazz: Class[_] = EmptyBeanItemClass.ClazzOf): BeanItem = {
    val beanItem = BeanItem(beanItemBean, key, name, category, description, editable, clazz)
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
      value.foreach(value => addItem(key, clazz = value.getClass))
    }

  def updateBeanValue(newValue: T): Unit = {
    beanItemBean = FXBean[T](newValue)
    itemBuffer.foreach(item => item.bean = beanItemBean)
  }

  def updateBean(newBean: FXBean[T]): Unit = {
    beanItemBean = newBean
    itemBuffer.foreach(item => item.bean = beanItemBean)
  }

  def beanItem(key: String): Option[BeanItem] =
    itemBuffer.asScala.find(item => key.equals(item.getKey))

  def bean: FXBean[T] = beanItemBean

  def beanValue: T = beanItemBean.bean

}

object BeanItems {

  def apply[T <: AnyRef]()(implicit e: T DefaultsTo AnyRef): BeanItems[T] = new BeanItems[T]()

  def apply[T <: AnyRef](bean: FXBean[T])(implicit e: T DefaultsTo AnyRef): BeanItems[T] = {
    val result = new BeanItems[T]()
    result.updateBean(bean)
    result
  }

}
