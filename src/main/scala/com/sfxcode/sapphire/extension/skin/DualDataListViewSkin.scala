package com.sfxcode.sapphire.extension.skin

import javafx.beans.binding.Bindings
import javafx.scene.control.SelectionMode._
import javafx.scene.control.SkinBase

import com.sfxcode.sapphire.core.value.FXBean
import com.sfxcode.sapphire.extension.control.{DataListView, DualDataListView}
import de.jensd.fx.glyphs.GlyphsDude
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon

import scalafx.Includes._
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.geometry.Pos
import scalafx.scene.control.Button
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.Priority._
import scalafx.scene.layout.{HBox, VBox}


class DualDataListViewSkin[S <: AnyRef](view: DualDataListView[S]) extends SkinBase[DualDataListView[S]](view) {

  implicit def observableBufferToList[T <: AnyRef](buffer: ObservableBuffer[FXBean[T]]): Seq[T] = {
    buffer.map(item => item.bean).toSeq
  }

  val box = new HBox() {
    spacing = 5
    hgrow = Always
  }

  val buttonMoveToTarget: Button = GlyphsDude.createIconButton(FontAwesomeIcon.ANGLE_RIGHT)
  val buttonMoveToTargetAll: Button = GlyphsDude.createIconButton(FontAwesomeIcon.ANGLE_DOUBLE_RIGHT)

  val buttonMoveToSource: Button = GlyphsDude.createIconButton(FontAwesomeIcon.ANGLE_LEFT)
  val buttonMoveToSourceAll: Button = GlyphsDude.createIconButton(FontAwesomeIcon.ANGLE_DOUBLE_LEFT)

  buttonMoveToTarget.onAction = (e: ActionEvent) => moveToTarget()
  buttonMoveToSource.onAction = (e: ActionEvent) => moveToSource()
  buttonMoveToTargetAll.onAction = (e: ActionEvent) => moveAllToTarget()
  buttonMoveToSourceAll.onAction = (e: ActionEvent) => moveAllToSource()

  def leftItems = view.leftDataListView.getItems

  def leftSelectionModel = view.leftDataListView.listView.selectionModel()

  def rightItems = view.rightDataListView.getItems

  def rightSelectionModel = view.rightDataListView.listView.selectionModel()

  leftSelectionModel.setSelectionMode(MULTIPLE)
  rightSelectionModel.setSelectionMode(MULTIPLE)

  leftItems.onChange {
    bindButtons()
  }

  rightItems.onChange {
    bindButtons()
  }

  leftSelectionModel.getSelectedItems.onChange {
    bindButtons()
  }

  rightSelectionModel.getSelectedItems.onChange {
    bindButtons()
  }

  def bindButtons() {
    buttonMoveToTargetAll.disableProperty.bind(Bindings.isEmpty(leftSelectionModel.getSelectedItems))
    buttonMoveToSourceAll.disableProperty.bind(Bindings.isEmpty(rightSelectionModel.getSelectedItems))

    buttonMoveToTarget.disableProperty.bind(Bindings.isEmpty(leftSelectionModel.getSelectedItems))
    buttonMoveToSource.disableProperty.bind(Bindings.isEmpty(rightSelectionModel.getSelectedItems))
  }

  view.leftDataListView.listView.onMouseClicked = (e: MouseEvent) => if (e.clickCount == 2) moveToTarget()
  view.rightDataListView.listView.onMouseClicked = (e: MouseEvent) => if (e.clickCount == 2) moveToSource()

  val buttonBox = new VBox {
    alignment = Pos.Center
    spacing = 5
    fillWidth = true
    children = List(buttonMoveToTarget, buttonMoveToTargetAll, buttonMoveToSource, buttonMoveToSourceAll)
  }

  getChildren.add(box)

  bindButtons()
  updateView()

  def updateView(): Unit = {
    box.children.add(view.leftDataListView)
    box.children.add(buttonBox)
    box.children.add(view.rightDataListView)
  }

  def moveToTarget() {
    move(view.leftDataListView, view.rightDataListView, ObservableBuffer(leftSelectionModel.getSelectedItem))
    leftSelectionModel.clearSelection()
  }

  private def moveToSource() {
    move(view.rightDataListView, view.leftDataListView, ObservableBuffer(rightSelectionModel.getSelectedItem))
    rightSelectionModel.clearSelection()
  }

  def moveAllToTarget() {
    move(view.leftDataListView, view.rightDataListView, leftSelectionModel.getSelectedItems)
    leftSelectionModel.clearSelection()
  }

  private def moveAllToSource() {
    move(view.rightDataListView, view.leftDataListView, rightSelectionModel.getSelectedItems)
    rightSelectionModel.clearSelection()
  }

  private def move(source: DataListView[S], target: DataListView[S], items: ObservableBuffer[FXBean[S]]) {
    val sourceItems = ObservableBuffer(source.getItems)
    sourceItems.removeAll(items)
    val targetItems = ObservableBuffer(target.getItems)
    targetItems.addAll(items)
    source.setItems(sourceItems)
    target.setItems(targetItems)
  }

}
