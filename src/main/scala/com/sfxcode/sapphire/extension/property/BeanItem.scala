package com.sfxcode.sapphire.extension.property

import java.time.{ ZoneId, LocalDateTime, Instant, LocalDate }
import java.util.{ Optional, Date }
import javafx.beans.value.ObservableValue

import com.sfxcode.sapphire.core.value._
import com.typesafe.config.ConfigFactory
import org.controlsfx.control.PropertySheet.Item

import scala.reflect.runtime.universe._
import scalafx.collections.ObservableBuffer

class BeanItem(var bean: FXBean[_ <: AnyRef], key: String, name: String = "", category: String = "", description: String = "") extends Item {
  val conf = ConfigFactory.load()

  override def getType: Class[_] = {
    val memberInfo = FXBeanClassRegistry.memberInfo(bean.bean, key)
    if (isDateType)
      classOf[LocalDate]
    else
      memberInfo.javaClass
  }

  def isDateType = FXBeanClassRegistry.memberInfo(bean.bean, key).signature == PropertyType.TypeDate

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

  override def getCategory = {
    if (category.isEmpty)
      conf.getString("sapphire.extension.properties.beanItem.defaultCategory")
    else
      category
  }

  override def getName = {
    if (name.isEmpty)
      key
    else
      name
  }

  override def getDescription = description

  override def isEditable = true

  def asLocalDate(date: java.util.Date): LocalDate = {
    val instant = Instant.ofEpochMilli(date.getTime)
    LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate
  }

  def asDate(date: LocalDate): java.util.Date = {
    val instant = date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant
    Date.from(instant)
  }

  override def getObservableValue: Optional[ObservableValue[_]] = Optional.of(bean.getProperty(key).delegate.asInstanceOf[ObservableValue[_]])
}

object BeanItem {

  def apply(bean: FXBean[_ <: AnyRef], key: String, name: String = "", category: String = "", description: String = ""): BeanItem = {
    new BeanItem(bean, key, name, category, description)
  }

  def beanItems[T <: AnyRef](bean: FXBean[T])(implicit t: TypeTag[T]): ObservableBuffer[Item] = {
    val result = new ObservableBuffer[Item]()
    val symbols = ReflectionTools.getMembers[T]()
    symbols.foreach(s => {
      val key = s.name.toString
      result.add(BeanItem(bean, key))
    })
    result
  }

}

class BeanItems {
  private val itemBuffer = new ObservableBuffer[BeanItem]()

  private var bean: FXBean[_ <: AnyRef] = _

  def getItems = itemBuffer

  def addItem(key: String, name: String = "", category: String = "", description: String = ""): Unit = {
    itemBuffer.add(BeanItem(bean, key, name, category, description))
  }

  def updateBean(bean: FXBean[_ <: AnyRef]) {
    itemBuffer.foreach(item => item.bean = bean)
  }

}

object BeanItems {

  def apply(): BeanItems = new BeanItems()

  def apply(bean: FXBean[_ <: AnyRef]): BeanItems = {
    val result = new BeanItems()
    result.updateBean(bean)
    result
  }
}

