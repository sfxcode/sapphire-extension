package com.sfxcode.sapphire.extension.skin

import javafx.beans.binding.Bindings
import javafx.scene.control.SkinBase

import scalafx.scene.control._
import scalafx.scene.layout.VBox

import com.sfxcode.sapphire.core.value.FXBean
import com.sfxcode.sapphire.extension.Includes._
import com.sfxcode.sapphire.extension.control.DataListView
import com.sfxcode.sapphire.extension.control.list.FXListCellFactory




class DataListViewSkin[S <: AnyRef](view: DataListView[S]) extends SkinBase[DataListView[S]](view) {

  val box = new VBox() {
    spacing = 5
  }
  val listView = new ListView[FXBean[S]]()

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
    box.children.add(listView)
    if (view.footer)
      box.children.add(view.footer)
  }

  def updateListViewItems(): Unit = {
    listView.getItems.clear()
    view.items.value.foreach(v => {
      listView.getItems.add(FXBean(v))
    })
  }

  def updateCellFactory(): Unit = {
    val cellFactory = new FXListCellFactory[FXBean[S]]
    cellFactory.setProperty(view.cellProperty.value)
    listView.setCellFactory(cellFactory)
  }


}
