package com.sfxcode.sapphire.extension.showcase.controller.table

import com.sfxcode.sapphire.core.value.BeanConversions
import javafx.event.ActionEvent
import com.sfxcode.sapphire.extension.showcase.model.{Friend, PersonDatabase}

import scala.reflect._

class FriendTableController extends AbstractTableViewController with BeanConversions {

  type R = Friend

  def ct = classTag[R]

  def items = PersonDatabase.friends

  override def willGainVisibility(): Unit = {
    super.willGainVisibility()
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

