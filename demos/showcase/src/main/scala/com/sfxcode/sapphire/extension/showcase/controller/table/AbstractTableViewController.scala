package com.sfxcode.sapphire.extension.showcase.controller.table

import javafx.beans.binding.Bindings

import com.sfxcode.sapphire.extension.controller.DataTableController
import com.sfxcode.sapphire.extension.showcase.controller.MainController
import com.sfxcode.sapphire.extension.filter.DataTableFilter
import com.typesafe.scalalogging.LazyLogging

abstract class AbstractTableViewController extends DataTableController with LazyLogging {

  override def initTable(tableFilter: DataTableFilter[R]): Unit = {
    super.initTable(tableFilter)
    tableFilter.hideColumn("metaData")
    tableFilter.addSearchField("nameFilter", "name").setPromptText("Name")
  }

  override def didGainVisibility() {
    mainWindowController.statusBar.textProperty().bind(Bindings.format("%d records found", Bindings.size(tableFilter.filterResult)))
  }

  def mainWindowController: MainController = {
    parent.asInstanceOf[MainController]
  }

  def workspaceManager = mainWindowController.workspaceManager

}

