package com.sfxcode.sapphire.extension.controller


import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.TableView
import javafx.scene.layout.HBox

import com.sfxcode.sapphire.extension.table.FXTableViewController
import com.sfxcode.sapphire.core.controller.ViewController
import com.sfxcode.sapphire.core.value.FXBean
import com.typesafe.scalalogging.LazyLogging

import scala.reflect.ClassTag
import scalafx.Includes._


abstract class TableViewController extends ViewController with LazyLogging {

  type R <: AnyRef

  def ct: ClassTag[R]

  @FXML
  var table: TableView[FXBean[R]] = _

  @FXML
  var searchBox: HBox = _

  var tableController: FXTableViewController[R] = _

  def records: ObservableList[FXBean[R]]

  override def didGainVisibilityFirstTime() {
    super.didGainVisibilityFirstTime()
    tableController = FXTableViewController[R](table, records, searchBox)(ct)
    tableController.addColumns()

    initTable(tableController)

    tableController.selectedItem.onChange((_, oldValue, newValue) => selectedTableViewItemDidChange(oldValue, newValue))

  }

  def initTable(tableController: FXTableViewController[R]): Unit = {

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

