package com.sfxcode.sapphire.extension.control

import javafx.scene.Node
import javafx.scene.control.{Control, Skin}

import com.sfxcode.sapphire.core.Includes._
import com.sfxcode.sapphire.core.value.FXBean
import com.sfxcode.sapphire.extension.skin.DataListViewSkin

import scalafx.beans.property._
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.ListView

class DataListView[S <: AnyRef] extends Control{

  val items =  ObjectProperty[ObservableBuffer[FXBean[S]]](this, "listViewItems", ObservableBuffer[FXBean[S]]())

  val header =  ObjectProperty[Node](this, "listViewFooter")
  val footer =  ObjectProperty[Node](this, "listViewFooter")
  val listView = new ListView[FXBean[S]]()

  val footerTextProperty = StringProperty("%d elements")
  val cellProperty = StringProperty("${_self.toString()}")

  protected override def createDefaultSkin: Skin[DataListView[S]] = {
    new DataListViewSkin[S](this)
  }

  def setItems(values:Iterable[S]): Unit = {
    items.value = values
  }

  def getItems:ObservableBuffer[FXBean[S]] = items.value

  def addFilter(values:ObservableBuffer[FXBean[S]] ,property:String): Unit = {

  }

}
