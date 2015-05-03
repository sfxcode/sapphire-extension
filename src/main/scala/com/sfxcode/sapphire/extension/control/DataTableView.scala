package com.sfxcode.sapphire.extension.control

import javafx.scene.control.{Control, Skin}

import com.sfxcode.sapphire.core.value.FXBean
import com.sfxcode.sapphire.extension.skin.DataTableViewSkin

import scalafx.Includes._
import scalafx.beans.property.ObjectProperty
import scalafx.collections.ObservableBuffer
import scalafx.scene.layout.Pane


class DataTableView [S <: AnyRef] extends Control  {

  val items =  ObjectProperty[ObservableBuffer[FXBean[S]]](this, "tableViewItems", ObservableBuffer[FXBean[S]]())
  val searchPane = ObjectProperty[Pane](this, "filterPane")

  protected override def createDefaultSkin: Skin[DataTableView[S]] = {
    new DataTableViewSkin[S](this)
  }
  def dataTableViewSkin = getSkin.asInstanceOf[DataTableViewSkin[S]]

  def tableView = dataTableViewSkin.tableView

  def selectedBean: FXBean[S] = tableView.selectionModel().selectedItemProperty().get()

  def selectedItem = tableView.selectionModel().selectedItemProperty()

  def selectedItems = tableView.selectionModel().selectedItems

}
