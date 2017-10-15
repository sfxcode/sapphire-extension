package com.sfxcode.sapphire.extension.skin

import javafx.beans.binding.Bindings
import javafx.scene.control.SelectionMode._
import javafx.scene.control.SkinBase

import com.sfxcode.sapphire.core.value.FXBean
import com.sfxcode.sapphire.extension.control.{ DataListView, DualDataListView }
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory

import scalafx.Includes._
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.geometry.Pos
import scalafx.scene.control.Button
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.Priority._
import scalafx.scene.layout._

class DualDataListViewSkin[S <: AnyRef](view: DualDataListView[S]) extends SkinBase[DualDataListView[S]](view) {

  implicit def observableBufferToList[T <: AnyRef](buffer: ObservableBuffer[FXBean[T]]): Seq[T] = {
    buffer.map(item => item.bean).toSeq
  }

  val contentGridPane = new GridPane() {
    styleClass += "content-grid"
  }

  val buttonMoveToTarget: Button = FontAwesomeIconFactory.get().createIconButton(FontAwesomeIcon.ANGLE_RIGHT)
  val buttonMoveToTargetAll: Button = FontAwesomeIconFactory.get().createIconButton(FontAwesomeIcon.ANGLE_DOUBLE_RIGHT)

  val buttonMoveToSource: Button = FontAwesomeIconFactory.get().createIconButton(FontAwesomeIcon.ANGLE_LEFT)
  val buttonMoveToSourceAll: Button = FontAwesomeIconFactory.get().createIconButton(FontAwesomeIcon.ANGLE_DOUBLE_LEFT)

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
    styleClass += "button-box"
    alignment = Pos.Center
    spacing = 5
    fillWidth = true
    children = List(buttonMoveToTarget, buttonMoveToTargetAll, buttonMoveToSource, buttonMoveToSourceAll)
  }

  getChildren.add(contentGridPane)

  bindButtons()
  updateView()

  def updateView(): Unit = {
    addGridPaneConstraints()
    contentGridPane.add(view.leftDataListView, 0, 0)
    contentGridPane.add(buttonBox, 1, 0)
    contentGridPane.add(view.rightDataListView, 2, 0)
  }

  def addGridPaneConstraints(): Unit = {
    val row = new RowConstraints()
    row.setFillHeight(true)
    row.setVgrow(Priority.Never)
    contentGridPane.getRowConstraints.add(row)

    val col1 = new ColumnConstraints

    col1.setFillWidth(true)
    col1.setHgrow(Priority.Always)
    col1.setMaxWidth(Double.MaxValue)
    col1.setPrefWidth(200)

    val col2 = new ColumnConstraints
    col2.setFillWidth(true)
    col2.setHgrow(Priority.Never)
    col2.setMaxWidth(50)
    col2.setMinWidth(50)

    val col3 = new ColumnConstraints
    col3.setFillWidth(true)
    col3.setHgrow(Priority.Always)
    col3.setMaxWidth(Double.MaxValue)
    col3.setPrefWidth(200)

    contentGridPane.getColumnConstraints.addAll(col1, col2, col3)
  }

  private def moveToTarget() {
    move(view.leftDataListView, view.rightDataListView, ObservableBuffer(leftSelectionModel.getSelectedItem))
    leftSelectionModel.clearSelection()
  }

  private def moveToSource() {
    move(view.rightDataListView, view.leftDataListView, ObservableBuffer(rightSelectionModel.getSelectedItem))
    rightSelectionModel.clearSelection()
  }

  private def moveAllToTarget() {
    move(view.leftDataListView, view.rightDataListView, leftSelectionModel.getSelectedItems)
    leftSelectionModel.clearSelection()
  }

  private def moveAllToSource() {
    move(view.rightDataListView, view.leftDataListView, rightSelectionModel.getSelectedItems)
    rightSelectionModel.clearSelection()
  }

  def move(source: DataListView[S], target: DataListView[S], items: ObservableBuffer[FXBean[S]]) {
    val sourceItems = ObservableBuffer(source.getItems)
    sourceItems.removeAll(items)
    val targetItems = ObservableBuffer(target.getItems)
    targetItems.addAll(items)
    source.setItems(sourceItems)
    target.setItems(targetItems)
  }

}
