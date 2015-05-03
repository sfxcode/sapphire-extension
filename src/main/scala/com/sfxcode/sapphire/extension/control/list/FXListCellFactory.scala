package com.sfxcode.sapphire.extension.control.list

import javafx.scene.control._
import javafx.util.Callback

import com.sfxcode.sapphire.core.value.FXBean

import scala.beans.BeanProperty

class FXListCellFactory[S] extends Callback[ListView[S], ListCell[S]] {
  @BeanProperty
  var property = ""

  def call(column: ListView[S]): ListCell[S] = {
    // FXListCell[S](property)Con
    new FXListCell[S](property)
  }

}

class FXListCell[S](property: String="") extends ListCell[S] {

  override def updateItem(item: S, empty: Boolean)  {
    super.updateItem(item, empty)
    item match {
      case b:FXBean[_] =>
        val value = b.getValue(property)
        value match {
          case v:Any =>  textProperty().set(v.toString)
          case _ => textProperty().set("NULL VALUE")
        }
      case b:Any => textProperty().set(b.toString)
      case _ =>  textProperty().set("")
    }
  }
}
