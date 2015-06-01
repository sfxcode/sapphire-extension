package com.sfxcode.sapphire.extension.skin

import javafx.beans.binding.Bindings
import javafx.scene.control.SkinBase

import com.sfxcode.sapphire.core.control.FXListCellFactory
import com.sfxcode.sapphire.extension.Includes._
import com.sfxcode.sapphire.extension.control.DataListView

import scalafx.collections.ObservableBuffer
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

  view.items.onChange {
    view.items.value.onChange {
      updateListViewItems()
    }
    updateListViewItems()
  }


  updateCellFactory()
  view.cellProperty.onChange(updateCellFactory())

  view.header.onChange(updateView())
  view.footer.onChange(updateView())
  view.showFooter.onChange(updateView())

  getChildren.add(box)

  updateListViewItems()

  updateView()

  def updateView(): Unit = {
    box.children.clear()
    if (view.header)
      box.children.add(view.header)
    box.children.add(view.listView)
    if (view.showFooter.get && view.footer)
      box.children.add(view.footer)
  }

  def updateListViewItems(): Unit = {
    view.listView.items = ObservableBuffer(view.items.value)
    label.text <== Bindings.format(view.footerTextProperty.get, Bindings.size(view.listView.items.get))
  }

  def updateCellFactory(): Unit = {
    val cellFactory = new FXListCellFactory[S]
    cellFactory.setProperty(view.cellProperty.value)
    view.listView.setCellFactory(cellFactory)
  }

}
