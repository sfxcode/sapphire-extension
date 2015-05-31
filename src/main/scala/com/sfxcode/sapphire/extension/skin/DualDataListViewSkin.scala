package com.sfxcode.sapphire.extension.skin

import javafx.scene.control.SkinBase

import com.sfxcode.sapphire.extension.control.DualDataListView
import de.jensd.fx.glyphs.GlyphsDude
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon

import scalafx.Includes._
import scalafx.scene.control.Button
import scalafx.scene.layout.Priority._
import scalafx.scene.layout.{HBox, VBox}


class DualDataListViewSkin[S <: AnyRef](view: DualDataListView[S]) extends SkinBase[DualDataListView[S]](view) {

  val box = new HBox() {
    spacing = 5
    hgrow = Always
  }

  val buttonMoveToTarget: Button = GlyphsDude.createIconButton(FontAwesomeIcon.ANGLE_RIGHT)
  val buttonMoveToTargetAll: Button = GlyphsDude.createIconButton(FontAwesomeIcon.ANGLE_DOUBLE_RIGHT)

  val buttonMoveToSource: Button = GlyphsDude.createIconButton(FontAwesomeIcon.ANGLE_LEFT)
  val buttonMoveToSourceAll: Button = GlyphsDude.createIconButton(FontAwesomeIcon.ANGLE_DOUBLE_LEFT)


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

}
