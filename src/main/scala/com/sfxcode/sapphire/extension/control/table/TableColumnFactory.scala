package com.sfxcode.sapphire.extension.control.table

import com.sfxcode.sapphire.core.value.FXBean

import scala.collection.mutable
import scala.reflect.runtime.{universe => ru}
import scalafx.scene.control.TableColumn
import scalafx.scene.text.TextAlignment


object TableColumnFactory {
  val rightAlignmentList = List("Date", "Calendar", "Int", "Long", "Double", "Float")

  def columnFromFactories[S <: AnyRef, T](header: String, valueFactory: FXValueFactory[FXBean[S], T], cellFactory: Option[FXCellFactory[FXBean[S], T]] = None): TableColumn[FXBean[S], T] = {
    val column = new TableColumn[FXBean[S], T]() {
      text = header
      prefWidth = 80
    }
    column.setCellValueFactory(valueFactory)
    if (cellFactory.isDefined)
      column.setCellFactory(cellFactory.get)

    column
  }

  def columnListFromMembers[S <: AnyRef, T](members:List[ru.Symbol],columnHeaderMap:Map[String,String]
                                            ,columnPropertyMap:Map[String,String], editable:Boolean=false
                                           ,numberFormat:String ="#,##0", decimalFormat:String="#,##0.00"
                                             ):Map[String, TableColumn[FXBean[S], T]] = {
    val result = mutable.HashMap[String, TableColumn[FXBean[S], T]]()
    val symbols = members.collect({ case x if x.isTerm => x.asTerm}).filter(t => t.isVal || t.isVar).map(_.asTerm)
    symbols.foreach(symbol => {
      val name = symbol.name.toString.trim
      val cellFactory = new FXTextFieldCellFactory[FXBean[S], T]()
      val signature = symbol.typeSignature.toString
      if (editable)
        cellFactory.setConverter(signature.replace("Int", "Integer"))

      if (shouldAlignRight(signature))
        cellFactory.setAlignment(TextAlignment.Right)


      val property = columnPropertyMap.getOrElse(name, name)
      val valueFactory = new FXValueFactory[FXBean[S], T]()
      valueFactory.setProperty(property)
      if (editable) {
        if (signature.contains("Int") || signature.contains("Long"))
          valueFactory.format = numberFormat
        else  if (signature.contains("Double") || signature.contains("Float"))
          valueFactory.format = decimalFormat
      }

      result.put(property, columnFromFactories[S,T](columnHeaderMap.getOrElse(name, propertyToHeader(name)), valueFactory, Some(cellFactory)))

    })
    result.toMap
  }

  private def propertyToHeader(property: String): String = {
    if (property.length == 1)
      return property.toUpperCase
    val firstUpper = property.charAt(0).toUpper + property.substring(1)
    var result = new mutable.StringBuilder()
    result.append(property.charAt(0).toUpper)
    property.substring(1).toCharArray.foreach(c => {
      if (c.isUpper)
        result.append(" " + c)
      else
        result.append(c)
    })
    result.toString()
  }


  private def shouldAlignRight(signature:String):Boolean = {
    rightAlignmentList.foreach(s=> {
      if (signature.contains(s))
        return true
    })
    false
  }

}
