package com.sfxcode.sapphire.control.properties

import com.sfxcode.sapphire.core.value.{FXBean, ReflectionTools}
import org.controlsfx.control.PropertySheet.Item

import scala.reflect.runtime.universe._
import scalafx.collections.ObservableBuffer

class BeanItem(bean:FXBean[_ <:AnyRef], name:String, category:String, description:String) extends Item {

  override def getType: Class[_] = {
    bean.memberInfo(name).javaClass
  }

  override def getValue: AnyRef = bean.getValue(name).asInstanceOf[AnyRef]

  override def setValue(value: Any): Unit = bean.updateValue(name, value)

  override def getCategory = category

  override def getName = name

  override def getDescription = description
}

object BeanItem  {

  def apply(bean:FXBean[_ <:AnyRef], name:String, category:String="Basic", description:String=""):BeanItem = {
    new BeanItem(bean, name, category,description)
  }

  def beanItems[T<:AnyRef](bean:FXBean[T])(implicit t: TypeTag[T]): ObservableBuffer[Item] = {
    val result = new ObservableBuffer[Item]()
    val symbols = ReflectionTools.getMembers[T]()
    symbols.foreach(s => {
      val name = s.name.toString
      result.add(BeanItem(bean, name))
    })
    result
  }

}
