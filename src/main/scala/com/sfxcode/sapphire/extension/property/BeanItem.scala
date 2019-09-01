package com.sfxcode.sapphire.extension.property

import java.time.{ Instant, LocalDate, LocalDateTime, ZoneId }
import java.util
import java.util.{ Date, Optional }

import com.sfxcode.sapphire.core.ConfigValues
import javafx.beans.value.ObservableValue
import com.sfxcode.sapphire.core.value._
import javafx.collections.{ FXCollections, ObservableList }
import org.controlsfx.control.PropertySheet.Item
import com.sfxcode.sapphire.core.CollectionExtensions._

import scala.reflect.runtime.universe._

class BeanItem(var bean: FXBean[_ <: AnyRef], key: String, name: String = "", category: String = "", description: String = "") extends Item with ConfigValues {

  override def getType: Class[_] = {
    val memberInfo = FXBeanClassRegistry.memberInfo(bean.bean, key)
    if (isDateType)
      classOf[LocalDate]
    else
      memberInfo.javaClass
  }

  def isDateType: Boolean = FXBeanClassRegistry.memberInfo(bean.bean, key).signature == PropertyType.TypeDate

  override def getValue: AnyRef = {
    if (isDateType)
      asLocalDate(bean.getValue(key).asInstanceOf[Date])
    else
      bean.getValue(key).asInstanceOf[AnyRef]
  }

  override def setValue(value: Any): Unit = {
    if (isDateType)
      bean.updateValue(key, asDate(value.asInstanceOf[LocalDate]))
    else
      bean.updateValue(key, value)
  }

  override def getCategory: String = {
    if (category.isEmpty)
      configStringValue("sapphire.extension.properties.beanItem.defaultCategory")
    else
      category
  }

  override def getName: String = {
    if (name.isEmpty)
      key
    else
      name
  }

  override def getDescription: String = description

  override def isEditable = true

  def asLocalDate(date: java.util.Date): LocalDate = {
    val instant = Instant.ofEpochMilli(date.getTime)
    LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate
  }

  def asDate(date: LocalDate): java.util.Date = {
    val instant = date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant
    Date.from(instant)
  }

  override def getObservableValue: Optional[ObservableValue[_]] = Optional.of(bean.getProperty(key).asInstanceOf[ObservableValue[_]])
}

object BeanItem {

  def apply(bean: FXBean[_ <: AnyRef], key: String, name: String = "", category: String = "", description: String = ""): BeanItem = {
    new BeanItem(bean, key, name, category, description)
  }

  def beanItems[T <: AnyRef](bean: FXBean[T])(implicit t: TypeTag[T]): ObservableList[Item] = {
    val result = FXCollections.observableArrayList[Item]()
    val symbols = ReflectionTools.getMembers[T]()
    symbols.foreach(s => {
      val key = s.name.toString
      result.add(BeanItem(bean, key))
    })
    result
  }

}

class BeanItems(bean: FXBean[_ <: AnyRef]) {
  private val itemBuffer = FXCollections.observableArrayList[BeanItem]()

  def getItems: ObservableList[BeanItem] = itemBuffer

  def addItem(key: String, name: String = "", category: String = "", description: String = ""): Unit = {
    itemBuffer.add(BeanItem(bean, key, name, category, description))
  }

  def updateBean(bean: FXBean[_ <: AnyRef]) {
    itemBuffer.foreach(item => item.bean = bean)
  }

}

object BeanItems {

  def apply(bean: FXBean[_ <: AnyRef] = FXBean(new util.HashMap())): BeanItems = new BeanItems(bean)
}

