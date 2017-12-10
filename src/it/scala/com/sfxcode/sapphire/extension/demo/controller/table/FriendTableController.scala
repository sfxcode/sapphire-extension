package com.sfxcode.sapphire.extension.demo.controller.table

import javafx.event.ActionEvent

import com.sfxcode.sapphire.extension.demo.model.{ Friend, PersonDatabase }
import com.sfxcode.sapphire.core.Includes._

import scala.reflect._

class FriendTableController extends AbstractTableViewController {

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

