package com.sfxcode.sapphire.extension.skin

import javafx.beans.binding.Bindings
import javafx.scene.control.SkinBase

import com.sfxcode.sapphire.core.control.FXListCellFactory
import com.sfxcode.sapphire.extension.Includes._
import com.sfxcode.sapphire.extension.control.DataListView

import scalafx.scene.control._
import scalafx.scene.layout.VBox

class DataListViewSkin[S <: AnyRef](view: DataListView[S]) extends SkinBase[DataListView[S]](view) {

  val box = new VBox() {
    spacing = 5
  }

  val label = new Label {
    text = "Test"
  }

  view.footer.set(label)

  updateListViewItems()
  view.items.onChange(updateListViewItems())

  updateCellFactory()
  view.cellProperty.onChange(updateCellFactory())

  view.header.onChange(updateView())
  view.footer.onChange(updateView())

  getChildren.add(box)

  label.text <== Bindings.format(view.footerTextProperty.get, Bindings.size(view.items.get))

  updateView()

  def updateView(): Unit = {
    box.children.clear()
    if (view.header)
      box.children.add(view.header)
    box.children.add(view.listView)
    if (view.footer)
      box.children.add(view.footer)
  }

  def updateListViewItems(): Unit = {
    view.listView.items = view.items
  }

  def updateCellFactory(): Unit = {
    val cellFactory = new FXListCellFactory[S]
    cellFactory.setProperty(view.cellProperty.value)
    view.listView.setCellFactory(cellFactory)
  }

}
