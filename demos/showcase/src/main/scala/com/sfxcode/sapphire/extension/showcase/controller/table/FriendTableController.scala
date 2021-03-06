package com.sfxcode.sapphire.extension.showcase.controller.table

import com.sfxcode.sapphire.core.value.{ BeanConversions, FXBean }
import com.sfxcode.sapphire.extension.controller.DataTableController
import com.sfxcode.sapphire.extension.filter.DataTableFilter
import com.sfxcode.sapphire.extension.showcase.controller.BaseController
import com.sfxcode.sapphire.extension.showcase.model.{ Friend, PersonDatabase }
import javafx.collections.ObservableList
import javafx.event.ActionEvent

import scala.reflect._

class FriendTableController extends DataTableController with BaseController with BeanConversions {

  type R = Friend

  def ct: ClassTag[Friend] = classTag[R]

  def items: ObservableList[FXBean[Friend]] = PersonDatabase.friends

  override def willGainVisibility(): Unit = {
    super.willGainVisibility()
  }

  override def initTable(tableFilter: DataTableFilter[R]): Unit = {
    super.initTable(tableFilter)
    tableFilter.addSearchField("nameFilter", "name").setPromptText("Name")
  }

  def actionAddItem(event: ActionEvent): Unit = {
    tableFilter.itemValues.add(items.head)
  }

  def actionAddItems(event: ActionEvent): Unit = {
    tableFilter.itemValues.addAll(items)
  }

  def actionRemoveAllItems(event: ActionEvent): Unit = {
    tableFilter.itemValues.clear()
  }

  def actionReplaceItems(event: ActionEvent): Unit = {
    tableFilter.itemsProperty.set(items)
  }
}

