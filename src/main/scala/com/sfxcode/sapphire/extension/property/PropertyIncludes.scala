package com.sfxcode.sapphire.extension.property

import scalafx.beans.property.ObjectProperty

trait PropertyIncludes {

  implicit def objectPropertyToBoolean[T <: Any](property: ObjectProperty[T]): Boolean = property.get != null

  implicit def objectPropertyToValue [T](property: ObjectProperty[T]): T = property.get


}
