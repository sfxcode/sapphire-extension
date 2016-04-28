package com.sfxcode.sapphire.extension.filter

import javafx.scene.layout.Pane

import com.sfxcode.sapphire.core.control.FXValueFactory
import com.sfxcode.sapphire.core.value.FXBean
import com.sfxcode.sapphire.extension.control.table.{FXTextFieldCellFactory, TableColumnFactory}

import scala.collection.mutable
import scala.reflect.ClassTag
import scala.reflect.runtime.{universe => ru}
import scalafx.Includes._
import scalafx.beans.property.ObjectProperty
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.{TableView, _}
import scalafx.scene.text.TextAlignment

class DataTableFilter[ S <: AnyRef] (table: TableView[FXBean[S]],items: ObjectProperty[ObservableBuffer[FXBean[S]]], pane:ObjectProperty[Pane])
                                       (implicit ct: ClassTag[S]) extends DataFilter[S](items, pane) {

  // columns
  val columnMapping = new mutable.HashMap[String, TableColumn[FXBean[S], _]]()

  val columnPropertyMap = new mutable.HashMap[String, String]()
  val columnHeaderMap = new mutable.HashMap[String, String]()

  // reflection
  val mirror = ru.runtimeMirror(ct.runtimeClass.getClassLoader)
  val members = mirror.classSymbol(ct.runtimeClass).asType.typeSignature.members.toList.reverse
  logger.debug(members.collect({ case x if x.isTerm => x.asTerm }).filter(t => t.isVal || t.isVar).map(m => m.name.toString).toString())

  filterResult.onChange {
    table.getItems.clear()
    table.getItems.addAll(filterResult)
    table.sort()
  }

  filter()

  override def itemsChanged(oldItems:ObservableBuffer[FXBean[S]], newItems:ObservableBuffer[FXBean[S]]): Unit = {
    items.value.onChange(filter())
    reset()
    table.autosize()
  }


  def reload(shouldReset: Boolean = false): Unit = {
    table.setItems(null)
    table.layout()
    if (shouldReset)
      reset()
    else
      filter()
  }

  def addColumn(key: String, column: TableColumn[FXBean[S], _]): Unit = {
    table.columns.+=(column)
    columnMapping.put(key, column)
  }

  def addColumns[T](editable: Boolean = false, numberFormat: String = "#,##0", decimalFormat: String = "#,##0.00") {
    val columnList = TableColumnFactory.columnListFromMembers[S, T](members, columnHeaderMap.toMap,
      columnPropertyMap.toMap, editable, numberFormat, decimalFormat)

    columnList._1.foreach(key => addColumn(key, columnList._2(key)))
  }

  def addColumn[T](header: String, property: String, alignment: TextAlignment = TextAlignment.Left): TableColumn[FXBean[S], T] = {
    val valueFactory = new FXValueFactory[FXBean[S], T]()
    valueFactory.setProperty(columnPropertyMap.getOrElse(property, property))
    val cellFactory = new FXTextFieldCellFactory[FXBean[S], T]()
    cellFactory.setAlignment(alignment)

    val result = TableColumnFactory.columnFromFactories[S, T](header, valueFactory, Some(cellFactory))
    addColumn(header, result)
    result
  }

  def getColumn[T](property: String) = {
    columnMapping.get(property)
  }

  def getTable:TableView[FXBean[S]] = table

  def getItems:ObservableBuffer[FXBean[S]] = table.items.value

  def hideColumn(name: String*) = name.foreach(name => getColumn(name).foreach(c => c.setVisible(false)))

  def showColumn(name: String*) = name.foreach(name => getColumn(name).foreach(c => c.setVisible(true)))

  def setColumnText(name: String, text: String) = getColumn(name).foreach(c => c.setText(text))

  def setColumnPrefWidth(name: String, value: Double) = getColumn(name).foreach(c => c.setPrefWidth(value))

  def selectedBean: FXBean[S] = table.selectionModel().selectedItemProperty().get()

  def selectedItem = table.selectionModel().selectedItemProperty()

  def selectedItems = table.selectionModel().selectedItems

}