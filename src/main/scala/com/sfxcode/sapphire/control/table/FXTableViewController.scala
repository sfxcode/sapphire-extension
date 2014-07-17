package com.sfxcode.sapphire.control.table

import javafx.collections.ObservableList
import javafx.scene.text.TextAlignment

import com.sfxcode.sapphire.control.table.TableFilterType._
import com.sfxcode.sapphire.core.value.FXBean
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.slf4j.LazyLogging
import org.controlsfx.control.textfield.TextFields

import scala.collection.mutable
import scala.reflect.ClassTag
import scala.reflect.runtime.{universe => ru}
import scalafx.Includes._
import scalafx.beans.property.IntegerProperty
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.scene.control._
import scalafx.scene.layout.Pane



case class FXTableViewController[S <: AnyRef](table: TableView[FXBean[S]], values: ObservableList[FXBean[S]], searchPane: Pane = null)(implicit ct: ClassTag[S]) extends LazyLogging  {
  val conf = ConfigFactory.load()

  val filterMap = new mutable.HashMap[Control, Any]()
  val valueMap = new mutable.HashMap[String, Any]()
  val controlNameMapping = new mutable.HashMap[Control, String]()
  val nameControlMapping = new mutable.HashMap[String, Control]()
  val columnMapping = new mutable.HashMap[String, TableColumn[FXBean[S], _]]()

  val columnPropertyMap = new mutable.HashMap[String, String]()
  val columnHeaderMap = new mutable.HashMap[String, String]()

  val filterResult = ObservableBuffer(values)
  val filteredSize = IntegerProperty(values.size())

  // reflection
  val mirror = ru.runtimeMirror(ct.runtimeClass.getClassLoader)
  val members = mirror.classSymbol(ct.runtimeClass).asType.typeSignature.members.toList.reverse

  table.setItems(values)
  logger.debug(members.collect({ case x if x.isTerm => x.asTerm}).filter(t => t.isVal || t.isVar).map(m => m.name.toString).toString())

  def addSearchField(name: String, propertyKey: String, filterType: FilterValue = TableFilterType.FilterContainsIgnoreCase, searchField: TextField = TextFields.createClearableTextField()): TextField = {
    addCustomSearchField(name, filterFunction(filterType, propertyKey, name), searchField)
  }

  def addCustomSearchField(name: String, p: FXBean[S] => Boolean, searchField: TextField = new TextField()): TextField = {
    if (searchPane != null)
      searchPane.getChildren.add(searchField)
    searchField.textProperty().onChange((_, oldValue, newValue) => filter())
    filterMap.put(searchField, p)
    updateMapping(name, searchField).asInstanceOf[TextField]
  }

  def addSearchBox(name: String, propertyKey: String, noSelection: String = conf.getString("sapphire.control.searchBox.noSelection"), searchBox: ComboBox[String] = new ComboBox[String]()): ComboBox[String] = {
    if (searchPane != null)
      searchPane.getChildren.add(searchBox)
    val distinctList = values.filter(b => b.getValue(propertyKey) != null).map(b => b.getValue(propertyKey).toString).distinct
    val valueBuffer = new ObservableBuffer[String]()
    valueBuffer.+=(noSelection)
    valueBuffer.++=(distinctList)
    searchBox.items.set(valueBuffer)
    searchBox.getSelectionModel.select(0)
    filterMap.put(searchBox, equalsFunction(propertyKey, name))
    searchBox.onAction = (event: ActionEvent) => filter()
    updateMapping(name, searchBox).asInstanceOf[ComboBox[String]]
  }

  def getSearchField(name: String): TextField = {
    nameControlMapping(name).asInstanceOf[TextField]
  }

  private def updateMapping(name: String, control: Control): Control = {
    controlNameMapping.put(control, name)
    nameControlMapping.put(name, control)
    control
  }

  private def filter() {
    val start = System.currentTimeMillis()
    var filtered = values

    filterMap.keySet.foreach {
      case textField: TextField =>
        val filter = textField.getText
        if (filter.length > 0) {
          valueMap.put(controlNameMapping(textField), filter)
          filtered = filtered.filter(filterMap(textField).asInstanceOf[FXBean[S] => Boolean])
        }
      case searchBox: ComboBox[String] =>
        val model = searchBox.getSelectionModel
        if (model.selectedIndexProperty().get() > 0) {
          val item = model.getSelectedItem
          valueMap.put(controlNameMapping(searchBox), item)
          filtered = filtered.filter(filterMap(searchBox).asInstanceOf[FXBean[S] => Boolean])
          logger.debug(item)
        }
      case _ =>
    }

    table.setItems(filtered)
    filterResult.clear()
    filterResult.setAll(filtered)
    filteredSize.set(filtered.size())
    table.sort()
    logger.debug("filtered (%d) in %d ms".format(filteredSize.get, System.currentTimeMillis() - start))
  }

  private def filterFunction(function: FilterValue, property: String, valueKey: String): (FXBean[S] => Boolean) = {
    if (function == TableFilterType.FilterContains)
      containsFunction(property, valueKey)
    else if (function == TableFilterType.FilterContainsIgnoreCase)
      containsLowerCaseFunction(property, valueKey)
    else if (function == TableFilterType.FilterEquals)
      equalsFunction(property, valueKey)
    else if (function == TableFilterType.FilterEqualsIgnoreCase)
      equalsLowerCaseFunction(property, valueKey)
    else
      b => false
  }

  private def containsFunction(property: String, valueKey: String): (FXBean[S] => Boolean) = {
    b => b.getValue(property).toString.contains(valueMap(valueKey).toString)
  }

  private def containsLowerCaseFunction(property: String, valueKey: String): (FXBean[S] => Boolean) = {
    b => b.getValue(property).toString.toLowerCase.contains(valueMap(valueKey).toString.toLowerCase)
  }

  private def equalsFunction(property: String, valueKey: String): (FXBean[S] => Boolean) = {
    b => b.getValue(property).equals(valueMap(valueKey))
  }

  private def equalsLowerCaseFunction(property: String, valueKey: String): (FXBean[S] => Boolean) = {
    b => b.getValue(property).toString.toLowerCase.equals(valueMap(valueKey).toString.toLowerCase)
  }

  def addColumns[T]() {
    val symbols = members.collect({ case x if x.isTerm => x.asTerm}).filter(t => t.isVal || t.isVar).map(_.asTerm)
    symbols.foreach(symbol => {
      val name = symbol.name.toString.trim
      val cellFactory = new FXTextFieldCellFactory[FXBean[S], T]()
      val s = symbol.typeSignature.toString
      if (s.contains("Date"))
        cellFactory.setAlignment(TextAlignment.RIGHT)
      val valueFactory = new FXValueFactory[FXBean[S], T]()
      valueFactory.setProperty(columnPropertyMap.getOrElse(name, name))
      addColumn(columnHeaderMap.getOrElse(name,name), valueFactory, Some(cellFactory))
    })
  }

  def addColumn[T](header: String, valueFactory: FXValueFactory[FXBean[S], T], cellFactory: Option[FXCellFactory[FXBean[S], T]] = None, pw: Double = 80.0): TableColumn[FXBean[S], T] = {

    val newColumn = new TableColumn[FXBean[S], T]() {
      text = header
      prefWidth = pw
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

  def hideColumn(name: String) = getColumn(name).foreach(c => c.setVisible(false))

  def showColumn(name: String) = getColumn(name).foreach(c => c.setVisible(true))

  def setColumnText(name: String, text: String) = getColumn(name).foreach(c => c.setText(text))

  def setColumnPrefWidth(name: String, value: Double) = getColumn(name).foreach(c => c.setPrefWidth(value))

  def selectedBean: FXBean[S] = table.selectionModel().selectedItemProperty().get()

  def selectedItem = table.selectionModel().selectedItemProperty()

}


