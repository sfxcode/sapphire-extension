package com.sfxcode.sapphire.extension.controller


import javafx.fxml.FXML
import javafx.scene.control.TableView
import javafx.scene.layout.HBox

import com.sfxcode.sapphire.core.controller.ViewController
import com.sfxcode.sapphire.core.value.FXBean
import com.sfxcode.sapphire.extension.table.FXTableViewController
import com.typesafe.scalalogging.LazyLogging

import scala.reflect.ClassTag
import scalafx.Includes._
import scalafx.collections.ObservableBuffer


abstract class TableViewController extends ViewController with LazyLogging {

  type R <: AnyRef

  def ct: ClassTag[R]

  @FXML
  var table: TableView[FXBean[R]] = _

  @FXML
  var searchBox: HBox = _

  var tableController: FXTableViewController[R] = _

  def records: ObservableBuffer[FXBean[R]]

  override def didGainVisibilityFirstTime() {
    super.didGainVisibilityFirstTime()
    tableController = FXTableViewController[R](table, records, searchBox)(ct)
    tableController.addColumns()

    initTable(tableController)

    tableController.selectedItem.onChange((_, oldValue, newValue) => selectedTableViewItemDidChange(oldValue, newValue))
    tableController.selectedItems.onChange((source, changes) => {
      selectedItemsDidChange(source, changes)
    })
  }

  def initTable(tableController: FXTableViewController[R]): Unit = {

  }

  def selectedItemsDidChange(source:ObservableBuffer[FXBean[R]], changes:Seq[ObservableBuffer.Change]): Unit = {
    logger.debug("new values count: %s".format(source.size))
  }

  def selectedTableViewItemDidChange(oldValue: FXBean[R], newValue: FXBean[R]): Unit = {
    logger.debug("new value: %s".format({
      if (newValue != null)
        newValue.bean
      else
        null
    }))
  }


}

