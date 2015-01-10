package com.sfxcode.sapphire.control.properties

import java.time.{ZoneId, LocalDateTime, Instant, LocalDate}
import java.util.Date

import com.sfxcode.sapphire.core.value.{FXBean, FXBeanClassRegistry, PropertyType, ReflectionTools}
import org.controlsfx.control.PropertySheet.Item

import scala.reflect.runtime.universe._
import scalafx.collections.ObservableBuffer

class BeanItem(var bean: FXBean[_ <: AnyRef], name: String, category: String, description: String) extends Item {

  override def getType: Class[_] = {
    val memberInfo = FXBeanClassRegistry.memberInfo(bean.bean, name)
    if (isDateType)
      classOf[LocalDate]
    else
      memberInfo.javaClass
  }

  def isDateType = FXBeanClassRegistry.memberInfo(bean.bean, name).signature == PropertyType.TypeDate

  override def getValue: AnyRef = {
    if (isDateType)
      asLocalDate(bean.getValue(name).asInstanceOf[Date])
    else
      bean.getValue(name).asInstanceOf[AnyRef]
  }

  override def setValue(value: Any): Unit = {
    if (isDateType)
      bean.updateValue(name, asDate(value.asInstanceOf[LocalDate]))
    else
      bean.updateValue(name, value)
  }

  override def getCategory = category

  override def getName = name

  override def getDescription = description

  override def isEditable = true

  def asLocalDate(date:java.util.Date): LocalDate =  {
    val instant = Instant.ofEpochMilli(date.getTime)
    LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate
  }

  def asDate(date:LocalDate):java.util.Date = {
    val instant = date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant
    Date.from(instant)
  }
}

object BeanItem {

  def apply(bean: FXBean[_ <: AnyRef], name: String, category: String = "Basic", description: String = ""): BeanItem = {
    new BeanItem(bean, name, category, description)
  }

  def beanItems[T <: AnyRef](bean: FXBean[T])(implicit t: TypeTag[T]): ObservableBuffer[Item] = {
    val result = new ObservableBuffer[Item]()
    val symbols = ReflectionTools.getMembers[T]()
    symbols.foreach(s => {
      val name = s.name.toString
      result.add(BeanItem(bean, name))
    })
    result
  }

}
