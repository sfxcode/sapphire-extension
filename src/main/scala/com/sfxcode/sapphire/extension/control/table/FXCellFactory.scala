package com.sfxcode.sapphire.extension.control.table

import javafx.util.{StringConverter, Callback}
import javafx.scene.control.{TableCell, TableColumn}
import javafx.scene.control.cell.{TextFieldTableCell, CheckBoxTableCell}
import beans.BeanProperty
import com.sfxcode.sapphire.core.value.ConverterFactory

import scalafx.geometry.Pos
import scalafx.scene.text.TextAlignment

abstract class FXCellFactory[S, T] extends Callback[TableColumn[S, T], TableCell[S, T]] {

  @BeanProperty
  var alignment = TextAlignment.Left

  @BeanProperty
  var converter: String = _

  def updateCell(column: TableColumn[S, T], cell: TableCell[S, T]): TableCell[S, T] = {
    if (alignment == TextAlignment.Center) {
      cell.setAlignment(Pos.Center)
    }
    else if (alignment == TextAlignment.Right) {
      cell.setAlignment(Pos.CenterRight)
    }
    else
      cell.setAlignment(Pos.CenterLeft)

    if (converter != null) {
      cell match {
        case textFieldCell: TextFieldTableCell[S, T] => textFieldCell.setConverter(getConverterForName(converter))
      }
    }

    def getConverterForName(name:String):StringConverter[T] = {
      ConverterFactory.getConverterByName(name)
    }

    cell
  }
}

class FXCheckBoxCellFactory[S, T] extends FXCellFactory[S, T] {

  def call(column: TableColumn[S, T]): TableCell[S, T] = {
    updateCell(column, new CheckBoxTableCell[S, T]())
  }
}

class FXTextFieldCellFactory[S, T] extends FXCellFactory[S, T] {

  def call(column: TableColumn[S, T]): TableCell[S, T] = {
    updateCell(column, new TextFieldTableCell[S, T]())
  }
}
