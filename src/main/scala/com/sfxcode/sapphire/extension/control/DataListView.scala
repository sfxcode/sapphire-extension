package com.sfxcode.sapphire.extension.control

import javafx.scene.Node
import javafx.scene.control.{Control, Skin}

import com.sfxcode.sapphire.core.Includes._
import com.sfxcode.sapphire.core.value.FXBean
import com.sfxcode.sapphire.extension.filter.{DataFilter, DataListFilter}
import com.sfxcode.sapphire.extension.skin.DataListViewSkin

import scalafx.beans.property._
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.ListView

class DataListView[S <: AnyRef] extends Control {

  val items = ObjectProperty[ObservableBuffer[FXBean[S]]](this, "listViewItems", ObservableBuffer[FXBean[S]]())
  val filter = ObjectProperty[DataFilter[S]](this, "listFilter")


  val header = ObjectProperty[Node](this, "listViewFooter")
  val footer = ObjectProperty[Node](this, "listViewFooter")
  val listView = new ListView[FXBean[S]]()

  val footerTextProperty = StringProperty("%d elements")
  val cellProperty = StringProperty("${_self.toString()}")
  val sortProperty = StringProperty("")
  val shouldSortProperty = BooleanProperty(true)

  val showFooter = BooleanProperty(false)


  protected override def createDefaultSkin: Skin[DataListView[S]] = {
    new DataListViewSkin[S](this)
  }

  def setItems(values: Iterable[S]): Unit = {
    setItemValues(values)
  }

  def remove(bean: FXBean[S]) {
    items.value.remove(bean)
    setItemValues(items.value)
  }

  def add(bean: FXBean[S]): Unit = {
    items.value.remove(bean)
    setItemValues(items.value)
  }

  def getItems: ObservableBuffer[FXBean[S]] = items.value

  def addFilter(property: String = cellProperty.value): Unit = {
    filter.value = new DataListFilter[S](this, property)
  }

  private def setItemValues(values:ObservableBuffer[FXBean[S]]): Unit = {
    items.set(sortedItems(values))
    if (filter.value != null)
      filter.value.filter()
  }

  private def sortedItems(values:ObservableBuffer[FXBean[S]]): ObservableBuffer[FXBean[S]] = {
    var result = values

    if (shouldSortProperty.value) {
      val sortKey = {
        if (sortProperty.value == null || sortProperty.value.isEmpty)
          cellProperty.value
        else
          sortProperty.value
      }
      result = values.sortBy(f => "" + f.getValue(sortKey))
    }
   result
  }

}
