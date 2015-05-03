package com.sfxcode.sapphire.extension.skin

import javafx.beans.binding.Bindings
import javafx.scene.control.SkinBase

import com.sfxcode.sapphire.extension.control.DataListView
import org.controlsfx.control.textfield.TextFields

import scalafx.beans.property.ObjectProperty
import scalafx.scene.control._
import scalafx.scene.layout.VBox


class DataListViewSkin[S <: AnyRef](view: DataListView[S]) extends SkinBase[DataListView[S]](view) {

  implicit def objectPropertyToValue[T <: Any](property: ObjectProperty[T]):T = property.get

  val box = new VBox() {
    spacing = 5
  }
  val listView = new ListView[S]()

  val filterField = TextFields.createClearableTextField()
  filterField.setPrefWidth(60)
  filterField.setMaxWidth(100)

  val label = new Label {
    text = "Test"
  }

  view.header.set(filterField)
  view.footer.set(label)

  getChildren.add(box)

  label.text <== Bindings.format(view.footerTextProperty.get, Bindings.size(view.items.get))

  updateView()

  def updateView(): Unit = {
    box.children.clear()
    box.children.addAll(getSkinnable.header, listView, getSkinnable.footer)
  }


}