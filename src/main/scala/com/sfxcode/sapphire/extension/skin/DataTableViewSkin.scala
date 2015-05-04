package com.sfxcode.sapphire.extension.skin

import javafx.scene.control.SkinBase

import com.sfxcode.sapphire.core.value.FXBean
import com.sfxcode.sapphire.extension.control.DataTableView

import scalafx.geometry.Insets
import scalafx.scene.control.TableView
import scalafx.scene.layout.{BorderPane, HBox}


class DataTableViewSkin[S <: AnyRef](view: DataTableView[S]) extends SkinBase[DataTableView[S]](view) {

  val searchBox = new HBox {
    prefHeight = 30.0
    spacing = 10.0
  }

  val tableView = new TableView[FXBean[S]]

  val bottom = new HBox {
    prefHeight = 20.0
    spacing = 10.0
  }

  val borderPane = new BorderPane {
    padding = Insets(5)
  }

  getChildren.add(borderPane)

  updateView()

  def updateView(): Unit = {
    borderPane.top.set(searchBox)
    borderPane.bottom.set(bottom)
    borderPane.center = tableView
  }


}
