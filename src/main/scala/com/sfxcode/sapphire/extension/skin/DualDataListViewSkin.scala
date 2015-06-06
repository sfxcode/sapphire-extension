package com.sfxcode.sapphire.extension.skin

import javafx.scene.control.SelectionMode._
import javafx.scene.control.SkinBase

import com.sfxcode.sapphire.core.value.FXBean
import com.sfxcode.sapphire.extension.control.{DataListView, DualDataListView}
import de.jensd.fx.glyphs.GlyphsDude
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon

import scalafx.Includes._
import javafx.beans.binding.Bindings
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
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

  buttonMoveToTarget.onAction= (e: ActionEvent) =>  moveToTarget()
  buttonMoveToSource.onAction = (e: ActionEvent) => moveToSource()

//  buttonMoveToTargetAll.onAction = (e: ActionEvent) => {
//    move(view.leftDataListView, view.rightDataListView, leftItems)
//    //leftSelectionModel.clearSelection()
//  }
//
//
//  buttonMoveToSourceAll.onAction = (e: ActionEvent) => {
//    move(view.rightDataListView, view.leftDataListView, rightItems)
//   //rightSelectionModel.clearSelection()
//  }


  def leftItems = view.leftDataListView.getItems
  def leftSelectionModel = view.leftDataListView.listView.selectionModel()
  def rightItems = view.rightDataListView.getItems
  def rightSelectionModel =  view.rightDataListView.listView.selectionModel()

  leftSelectionModel.setSelectionMode(MULTIPLE)
  rightSelectionModel.setSelectionMode(MULTIPLE)

  leftItems.onChange {
    bindMoveAllButtonsToDataModel()
  }

  rightItems.onChange {
    bindMoveAllButtonsToDataModel()
  }

  leftSelectionModel.getSelectedItems.onChange {
    bindMoveButtonsToSelectionModel()
  }

  rightSelectionModel.getSelectedItems.onChange {
    bindMoveButtonsToSelectionModel()
  }

  def bindMoveAllButtonsToDataModel() {
    buttonMoveToTargetAll.disableProperty.bind(Bindings.isEmpty(leftItems))
    buttonMoveToSourceAll.disableProperty.bind(Bindings.isEmpty(rightItems))
  }

  def bindMoveButtonsToSelectionModel() {
    buttonMoveToTarget.disableProperty.bind(Bindings.isEmpty(leftSelectionModel.getSelectedItems))
    buttonMoveToSource.disableProperty.bind(Bindings.isEmpty(rightSelectionModel.getSelectedItems))
  }

  view.leftDataListView.listView.onMouseClicked = (e: MouseEvent) => if (e.clickCount == 2) moveToTarget()
  view.rightDataListView.listView.onMouseClicked = (e: MouseEvent) => if (e.clickCount == 2) moveToSource()

  val buttonBox = new VBox {
    spacing = 5
    fillWidth = true
    children = List(buttonMoveToTarget, buttonMoveToTargetAll, buttonMoveToSource, buttonMoveToSourceAll)
  }

  getChildren.add(box)

  bindMoveAllButtonsToDataModel()
  bindMoveButtonsToSelectionModel()
  updateView()

  def updateView(): Unit = {
    box.children.add(view.leftDataListView)
    box.children.add(buttonBox)
    box.children.add(view.rightDataListView)
  }

  def moveToTarget() {
    move(view.leftDataListView, view.rightDataListView)
    leftSelectionModel.clearSelection()
  }

  private def moveToSource() {
    move(view.rightDataListView, view.leftDataListView)
    rightSelectionModel.clearSelection()
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
