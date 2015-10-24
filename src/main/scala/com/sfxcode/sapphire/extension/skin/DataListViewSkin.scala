package com.sfxcode.sapphire.extension.skin

import javafx.beans.binding.Bindings
import javafx.scene.control.SkinBase

import com.sfxcode.sapphire.core.control.FXListCellFactory
import com.sfxcode.sapphire.extension.Includes._
import com.sfxcode.sapphire.extension.control.DataListView

import scalafx.collections.ObservableBuffer
import scalafx.scene.control._
import scalafx.scene.layout.{HBox, VBox}

class DataListViewSkin[S <: AnyRef](view: DataListView[S]) extends SkinBase[DataListView[S]](view) {


  val contentBox = new VBox() {
    spacing = 5
    styleClass.+=("content-box")
  }

  val label = new Label {
    text = "Test"
    styleClass.+=("footer-label")
  }

  val footerBox = new HBox {
    label
    styleClass.+=("footer-box")
  }
  view.footer.set(footerBox)

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

  getChildren.add(contentBox)

  updateListViewItems()

  updateView()

  def updateView(): Unit = {
    contentBox.children.clear()
    if (view.header)
      contentBox.children.add(view.header)
    contentBox.children.add(view.listView)
    if (view.showFooter.get && view.footer)
      contentBox.children.add(view.footer)
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
