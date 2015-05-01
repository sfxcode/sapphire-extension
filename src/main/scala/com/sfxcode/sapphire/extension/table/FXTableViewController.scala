package com.sfxcode.sapphire.extension.table


import com.sfxcode.sapphire.core.value.FXBean
import com.sfxcode.sapphire.extension.filter.Filterable
import com.typesafe.scalalogging.LazyLogging

import scala.collection.mutable
import scala.reflect.ClassTag
import scala.reflect.runtime.{universe => ru}
import scalafx.Includes._
import scalafx.collections.ObservableBuffer
import scalafx.scene.control._
import scalafx.scene.layout.Pane
import scalafx.scene.text.TextAlignment


case class FXTableViewController[S <: AnyRef](table: TableView[FXBean[S]], values: ObservableBuffer[FXBean[S]], searchPane: Pane = null)(implicit ct: ClassTag[S]) extends LazyLogging with Filterable[S] {

  val columnMapping = new mutable.HashMap[String, TableColumn[FXBean[S], _]]()

  val columnPropertyMap = new mutable.HashMap[String, String]()
  val columnHeaderMap = new mutable.HashMap[String, String]()

  // reflection
  val mirror = ru.runtimeMirror(ct.runtimeClass.getClassLoader)
  val members = mirror.classSymbol(ct.runtimeClass).asType.typeSignature.members.toList.reverse

  table.setItems(values)
  logger.debug(members.collect({ case x if x.isTerm => x.asTerm}).filter(t => t.isVal || t.isVar).map(m => m.name.toString).toString())

  override def unfilteredValues: ObservableBuffer[FXBean[S]] = values

  override def filterControlPane: Pane = searchPane

  def insert(item: FXBean[S]) {
    values.add(item)
    reset()
  }

  def remove(item: FXBean[S]) {
    values.remove(item)
    reset()
  }


  def reload(shouldReset:Boolean = false): Unit = {
    table.setItems(null)
    table.layout()
    table.setItems(values)
    if (shouldReset)
      reset()
    else
      filter()
  }

  def filteredValuesHasChanged(values:ObservableBuffer[FXBean[S]]): Unit = {
    table.setItems(values)
    table.sort()
  }



  def addColumns[T]() {
    val symbols = members.collect({ case x if x.isTerm => x.asTerm}).filter(t => t.isVal || t.isVar).map(_.asTerm)
    symbols.foreach(symbol => {
      val name = symbol.name.toString.trim
      val cellFactory = new FXTextFieldCellFactory[FXBean[S], T]()
      val signature = symbol.typeSignature.toString
      if (table.isEditable)
        cellFactory.setConverter(signature.replace("Int", "Integer"))

      if (shouldAlignRight(signature))
        cellFactory.setAlignment(TextAlignment.Right)

      val valueFactory = new FXValueFactory[FXBean[S], T]()
      valueFactory.setProperty(columnPropertyMap.getOrElse(name, name))
      if (!table.isEditable) {
        if (signature.contains("Int") || signature.contains("Long"))
          valueFactory.format = numberFormat()
        else  if (signature.contains("Double") || signature.contains("Float"))
          valueFactory.format = decimalFormat()
      }

      addColumnFromFactories(columnHeaderMap.getOrElse(name, propertyToHeader(name)), valueFactory, Some(cellFactory))
    })
  }

  private def shouldAlignRight(signature:String):Boolean = {
    rightAlignmentList.foreach(s=> {
      if (signature.contains(s))
        return true
    })
    false
  }

  def numberFormat() = "#,##0"
  def decimalFormat() = "#,##0.00"

  def rightAlignmentList = List("Date", "Calendar", "Int", "Long", "Double", "Float")


  def addColumn[T](header: String, property: String, alignment: TextAlignment = TextAlignment.Left, pw: Double = 80.0): TableColumn[FXBean[S], T] = {
    val valueFactory = new FXValueFactory[FXBean[S], T]()
    valueFactory.setProperty(columnPropertyMap.getOrElse(property, property))
    val cellFactory = new FXTextFieldCellFactory[FXBean[S], T]()
    cellFactory.setAlignment(alignment)
    addColumnFromFactories(header, valueFactory, Some(cellFactory), pw)
  }

  def addColumnFromFactories[T](header: String, valueFactory: FXValueFactory[FXBean[S], T], cellFactory: Option[FXCellFactory[FXBean[S], T]] = None, pw: Double = 80.0): TableColumn[FXBean[S], T] = {

    val newColumn = new TableColumn[FXBean[S], T]() {
      text = header
      minWidth = pw
    }
    newColumn.setCellValueFactory(valueFactory)
    if (cellFactory.isDefined)
      newColumn.setCellFactory(cellFactory.get)

    table.columns.+=(newColumn)
    columnMapping.put(valueFactory.getProperty, newColumn)
    newColumn
  }

  def getColumn[T](property: String) = {
    columnMapping.get(property)
  }

  def hideColumn(name: String*) = name.foreach(name => getColumn(name).foreach(c => c.setVisible(false)))

  def showColumn(name: String*) = name.foreach(name =>  getColumn(name).foreach(c => c.setVisible(true)))

  def setColumnText(name: String, text: String) = getColumn(name).foreach(c => c.setText(text))

  def setColumnPrefWidth(name: String, value: Double) = getColumn(name).foreach(c => c.setPrefWidth(value))

  def selectedBean: FXBean[S] = table.selectionModel().selectedItemProperty().get()

  def selectedItem = table.selectionModel().selectedItemProperty()

  def selectedItems = table.selectionModel().selectedItems

  def propertyToHeader(property: String): String = {
    if (property.size == 1)
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


}


