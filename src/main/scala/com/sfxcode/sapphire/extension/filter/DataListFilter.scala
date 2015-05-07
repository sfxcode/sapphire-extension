package com.sfxcode.sapphire.extension.filter

import javafx.scene.layout.Pane

import com.sfxcode.sapphire.core.value.FXBean
import com.sfxcode.sapphire.extension.control.DataListView

import scalafx.Includes._
import scalafx.beans.property.ObjectProperty
import scalafx.collections.ObservableBuffer
import scalafx.scene.layout.HBox

class DataListFilter[ S <: AnyRef](dataList: DataListView[S],items: ObjectProperty[ObservableBuffer[FXBean[S]]], searchFieldPropepertyKey:String = "")
                                        extends DataFilter[S](items, dataList.header.asInstanceOf[ObjectProperty[Pane]]) {
  val box = new HBox(5)
  dataList.header.value = box
  if (!searchFieldPropepertyKey.isEmpty)
    addSearchField(searchFieldPropepertyKey)

  filterResult.onChange {
    listView.getItems.clear()
    filterResult.foreach(v => dataList.getItems.add(v.asInstanceOf[FXBean[S]]))
  }

  filter()

  def reload(shouldReset: Boolean = false): Unit = {
    listView.setItems(null)
    listView.layout()
    if (shouldReset)
      reset()
    else
      filter()
  }

  def listView = dataList.listView

  def selectedBean: FXBean[S] = listView.selectionModel().selectedItemProperty().get()

  def selectedItem = listView.selectionModel().selectedItemProperty()

  def selectedItems = listView.selectionModel().selectedItems

}
