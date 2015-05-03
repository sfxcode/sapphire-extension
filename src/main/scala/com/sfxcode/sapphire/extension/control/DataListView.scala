package com.sfxcode.sapphire.extension.control

import javafx.scene.Node
import javafx.scene.control.{Control, Skin}

import com.sfxcode.sapphire.extension.skin.DataListViewSkin

import scalafx.beans.property._
import scalafx.collections.ObservableBuffer

class DataListView[S <: AnyRef] extends Control{

  val items =  ObjectProperty[ObservableBuffer[S]](this, "listViewItems", ObservableBuffer[S]())

  val header =  ObjectProperty[Node](this, "listViewFooter")
  val footer =  ObjectProperty[Node](this, "listViewFooter")
  val footerTextProperty = StringProperty("%d elements")

  val cellProperty = StringProperty("${_self.toString()}")

  protected override def createDefaultSkin: Skin[DataListView[S]] = {
    new DataListViewSkin[S](this)
  }

}
