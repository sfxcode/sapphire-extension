package com.sfxcode.sapphire.extension.filter

import com.sfxcode.sapphire.core.value.FXBean
import com.sfxcode.sapphire.extension.control.DataListView

import scalafx.Includes._
import scalafx.scene.layout.HBox

class DataListFilter[S <: AnyRef](dataList: DataListView[S], searchFieldPropertyKey: String = "")
  extends DataFilter[S](dataList.items, dataList.header) {
  var sortFiltered = true
  val box = new HBox(5)
  dataList.header.value = box
  if (!searchFieldPropertyKey.isEmpty)
    addSearchField(searchFieldPropertyKey)

  filterResult.onChange {
    dataList.listView.getItems.clear()
    if (filterResult.nonEmpty)
      filterResult.foreach(v => dataList.listView.getItems.add(v))
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
