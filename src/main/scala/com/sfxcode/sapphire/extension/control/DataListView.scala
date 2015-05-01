package com.sfxcode.sapphire.extension.control

import javafx.scene.Node
import javafx.scene.control.{Skin, Control}

import com.sfxcode.sapphire.core.value.FXBean
import com.sfxcode.sapphire.extension.skin.DataListViewSkin

import scalafx.beans.property._
import scalafx.collections.ObservableBuffer

class DataListView[T <: FXBean[_]] extends Control{

  val items =  ObjectProperty[ObservableBuffer[T]](this, "listViewItems", ObservableBuffer[T]())

  val header =  ObjectProperty[Node](this, "listViewFooter")
  val footer =  ObjectProperty[Node](this, "listViewFooter")
  val footerTextProperty = StringProperty("%d elements")

  val cellProperty = StringProperty("${_self.toString()}")

  protected override def createDefaultSkin: Skin[DataListView[T]] = {
    new DataListViewSkin[T](this)
  }

}
