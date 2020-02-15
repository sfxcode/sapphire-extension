package com.sfxcode.sapphire.extension.property

import java.time.{ Instant, LocalDate, LocalDateTime, ZoneId }
import java.util.{ Date, Optional }

import com.sfxcode.sapphire.core.ConfigValues
import com.sfxcode.sapphire.core.value._
import javafx.beans.value.ObservableValue
import javafx.collections.{ FXCollections, ObservableList }
import org.controlsfx.control.PropertySheet.Item

import scala.reflect.runtime.universe._

final class EmptyBeanItemClass

private object EmptyBeanItemClass {
  val ClazzOf: Class[_ <: EmptyBeanItemClass] = EmptyBeanItemClass().getClass

  def apply(): EmptyBeanItemClass = new EmptyBeanItemClass
}

class BeanItem(var bean: FXBean[_ <: AnyRef], key: String, name: String = "", category: String = "", description: String = "", editable: Boolean = true, clazz: Class[_] = EmptyBeanItemClass.ClazzOf) extends Item with ConfigValues {
  var classOption: Option[Class[_]] = Some(clazz)

  def getKey: String = key

  override def getType: Class[_] = {
    if (classOption.isDefined && EmptyBeanItemClass.ClazzOf != classOption.get) {
      classOption.get
    } else {
      val underlying = bean.bean
      if (underlying.isInstanceOf[Map[String, Any]] || underlying.isInstanceOf[java.util.Map[String, Any]]) {
        var clazz: Class[_] = classOf[String]
        val valueOption = Option(bean.getValue(key))
        if (valueOption.isDefined) {
          clazz = valueOption.get.getClass
          if (valueOption.get.isInstanceOf[java.util.Date])
            classOf[LocalDate]
        }
        classOption = Some(clazz)
        clazz
      } else {
        val memberInfo = FXBeanClassRegistry.memberInfo(underlying, key)
        val clazz = {
          if (isDateType)
            classOf[LocalDate]
          else
            memberInfo.javaClass
        }
        classOption = Some(clazz)
        clazz
      }
    }
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

  override def isEditable: Boolean = editable

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

  def apply(bean: FXBean[_ <: AnyRef], key: String, name: String = "", category: String = "", description: String = "", editable: Boolean = true, clazz: Class[_] = EmptyBeanItemClass.ClazzOf): BeanItem = {
    new BeanItem(bean, key, name, category, description, editable, clazz)
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

