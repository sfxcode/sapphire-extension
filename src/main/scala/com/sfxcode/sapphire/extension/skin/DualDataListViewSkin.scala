package com.sfxcode.sapphire.extension.skin

import javafx.scene.control.SelectionMode._
import javafx.scene.control.SkinBase

import com.sfxcode.sapphire.core.value.FXBean
import com.sfxcode.sapphire.extension.control.{DataListView, DualDataListView}
import de.jensd.fx.glyphs.GlyphsDude
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon

import scalafx.Includes._
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.Button
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.Priority._
import scalafx.scene.layout.{HBox, VBox}


class DualDataListViewSkin[S <: AnyRef](view: DualDataListView[S]) extends SkinBase[DualDataListView[S]](view) {

  implicit def observableBufferToList[T <:AnyRef](buffer:ObservableBuffer[FXBean[T]]):Seq[T] = {
    buffer.map(item =>item.bean).toSeq
  }


  val box = new HBox() {
    spacing = 5
    hgrow = Always
  }

  val buttonMoveToTarget: Button = GlyphsDude.createIconButton(FontAwesomeIcon.ANGLE_RIGHT)
  val buttonMoveToTargetAll: Button = GlyphsDude.createIconButton(FontAwesomeIcon.ANGLE_DOUBLE_RIGHT)

  val buttonMoveToSource: Button = GlyphsDude.createIconButton(FontAwesomeIcon.ANGLE_LEFT)
  val buttonMoveToSourceAll: Button = GlyphsDude.createIconButton(FontAwesomeIcon.ANGLE_DOUBLE_LEFT)

  view.leftDataListView.listView.selectionModel().setSelectionMode(MULTIPLE)
  view.rightDataListView.listView.selectionModel().setSelectionMode(MULTIPLE)

  view.leftDataListView.listView.onMouseClicked = (e: MouseEvent) => if (e.clickCount == 2) moveToTarget()
  view.rightDataListView.listView.onMouseClicked = (e: MouseEvent) => if (e.clickCount == 2) moveToSource()

  val buttonBox = new VBox {
    spacing = 5
    fillWidth = true
    children = List(buttonMoveToTarget, buttonMoveToTargetAll, buttonMoveToSource, buttonMoveToSourceAll)
  }

  getChildren.add(box)

  updateView()

  def updateView(): Unit = {
    box.children.add(view.leftDataListView)
    box.children.add(buttonBox)
    box.children.add(view.rightDataListView)
  }

  def moveToTarget() {
    move(view.leftDataListView, view.rightDataListView)
    view.leftDataListView.listView.getSelectionModel.clearSelection()
  }

  private def moveToSource() {
    move(view.rightDataListView, view.leftDataListView)
    view.rightDataListView.listView.getSelectionModel.clearSelection()
  }

  private def move(source: DataListView[S], target: DataListView[S]) {
    val selectedItems:ObservableBuffer[FXBean[S]] = source.listView.getSelectionModel.getSelectedItems
    move(source, target, selectedItems)
  }

  private def move(source:  DataListView[S], target:  DataListView[S], items: ObservableBuffer[FXBean[S]]) {
    items.foreach(item => {
      source.getItems.remove(item)
      target.getItems.add(item)
    })
   source.setItems(source.getItems)
   target.setItems(target.getItems)
  }

}
