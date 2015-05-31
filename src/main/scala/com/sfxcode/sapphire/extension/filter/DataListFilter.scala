package com.sfxcode.sapphire.extension.filter

import javafx.scene.layout.Pane

import com.sfxcode.sapphire.core.value.FXBean
import com.sfxcode.sapphire.extension.control.DataListView

import scalafx.Includes._
import scalafx.beans.property.ObjectProperty
import scalafx.scene.layout.HBox

class DataListFilter[ S <: AnyRef](dataList: DataListView[S], searchFieldPropepertyKey:String = "")
                                        extends DataFilter[S](dataList.header.asInstanceOf[ObjectProperty[Pane]]) {
  val box = new HBox(5)
  dataList.header.value = box
  if (!searchFieldPropepertyKey.isEmpty)
    addSearchField(searchFieldPropepertyKey)

  originalData = dataList.items.value.toList

  dataList.items.onChange {
    originalData = dataList.items.value.toList
  }

  filterResult.onChange {
    dataList.getItems.clear()
    filterResult.foreach(v => dataList.getItems.add(v))
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
