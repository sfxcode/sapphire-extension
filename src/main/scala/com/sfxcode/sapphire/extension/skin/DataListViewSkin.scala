package com.sfxcode.sapphire.extension.skin

import javafx.scene.control.SkinBase

import com.sfxcode.sapphire.core.control.FXListCellFactory
import com.sfxcode.sapphire.extension.Includes._
import com.sfxcode.sapphire.extension.control.DataListView

import scalafx.scene.control._
import scalafx.scene.layout.{ HBox, VBox }

class DataListViewSkin[S <: AnyRef](view: DataListView[S]) extends SkinBase[DataListView[S]](view) {

  val contentBox = new VBox() {
    spacing = 5
    styleClass.+=("content-box")
  }

  val label = new Label {
    text = "Footer Label"
    styleClass.+=("footer-label")
  }
  view.footerLabel.set(label)

  val headerBox = new HBox {
    styleClass.+=("header-box")
  }
  view.header.set(headerBox)

  val footerBox = new HBox {
    styleClass.+=("footer-box")
  }
  footerBox.children.add(label)
  view.footer.set(footerBox)

  updateCellFactory()
  view.cellProperty.onChange(updateCellFactory())

  view.header.onChange(updateView())
  view.showHeader.onChange(updateView())

  view.footer.onChange(updateView())
  view.showFooter.onChange(updateView())

  getChildren.add(contentBox)

  updateView()

  def updateView(): Unit = {
    contentBox.children.clear()
    if (view.showHeader.get && view.header)
      contentBox.children.add(view.header)

    contentBox.children.add(view.listView)

    if (view.showFooter.get && view.footer)
      contentBox.children.add(view.footer)
  }

  def updateCellFactory(): Unit = {
    val cellFactory = new FXListCellFactory[S]
    cellFactory.setProperty(view.cellProperty.value)
    view.listView.setCellFactory(cellFactory)
  }

}
