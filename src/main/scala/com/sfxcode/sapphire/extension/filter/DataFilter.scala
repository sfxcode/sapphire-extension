package com.sfxcode.sapphire.extension.filter

import javafx.scene.Node

import com.sfxcode.sapphire.core.value.FXBean
import com.sfxcode.sapphire.extension.filter.FilterType._
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import org.controlsfx.control.textfield.TextFields

import scala.collection.mutable
import scala.reflect.runtime.{universe => ru}
import scalafx.Includes._
import scalafx.beans.property.ObjectProperty
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.scene.control.{ComboBox, Control, TextField}
import scalafx.scene.layout.Pane

class DataFilter[S <: AnyRef](items: ObjectProperty[ObservableBuffer[FXBean[S]]], pane:ObjectProperty[Pane]) extends LazyLogging {
  val conf = ConfigFactory.load()
  implicit def objectPropertyToValue[T <: Any](property: ObjectProperty[T]):T = property.get

  private val controlList = ObservableBuffer[Node]()
  private val controlFilterMap = new mutable.HashMap[Control, Any]()
  private val controlFilterPropertyMap = new mutable.HashMap[Control, String]()
  private val valueMap = new mutable.HashMap[String, Any]()

  private val filterControlNameMapping = new mutable.HashMap[Control, String]()
  private val filterNameControlMapping = new mutable.HashMap[String, Control]()

  val filterResult = ObservableBuffer(items.value)

  pane.onChange((_, oldValue, newValue) => itemUpdated(oldValue, newValue))

  def itemUpdated(oldPane:Pane, newPane:Pane): Unit = {
    oldPane.children.clear()
    newPane.children.addAll(controlList)
  }
  
  def filterControlPane:Pane = pane.value

  //def itemValues:ObservableBuffer[FXBean[S]] = items.value

  def addSearchField(name: String, propertyKey: String, filterType: FilterValue = FilterType.FilterContainsIgnoreCase, searchField: TextField = TextFields.createClearableTextField()): TextField = {
    addCustomSearchField(name, filterFunction(filterType, propertyKey, name), searchField)
  }

  def addCustomSearchField(name: String, p: FXBean[S] => Boolean, searchField: TextField = new TextField()): TextField = {
    if (filterControlPane != null)
      filterControlPane.getChildren.add(searchField)
    searchField.textProperty().onChange((_, oldValue, newValue) => filter())
    controlList.add(searchField)
    controlFilterMap.put(searchField, p)
    updateMapping(name, searchField).asInstanceOf[TextField]
  }

  def addSearchBox(name: String, propertyKey: String, noSelection: String = conf.getString("sapphire.extension.searchBox.noSelection"), searchBox: ComboBox[String] = new ComboBox[String]()): ComboBox[String] = {
    if (filterControlPane != null)
      filterControlPane.getChildren.add(searchBox)

    updateSearchBoxValues(searchBox, noSelection, propertyKey)
    controlList.add(searchBox)
    controlFilterPropertyMap.put(searchBox, propertyKey)
    controlFilterMap.put(searchBox, equalsFunction(propertyKey, name))
    searchBox.onAction = (event: ActionEvent) => filter()
    updateMapping(name, searchBox).asInstanceOf[ComboBox[String]]
  }

  def updateSearchBoxValues(searchBox: ComboBox[String], noSelection: String, propertyKey: String): Unit = {
    val distinctList = items.value.filter(b => b.getValue(propertyKey) != null).map(b => b.getValue(propertyKey).toString).distinct.sorted
    val valueBuffer = new ObservableBuffer[String]()
    valueBuffer.+=(noSelection)
    valueBuffer.++=(distinctList)
    searchBox.items.set(valueBuffer)
    searchBox.getSelectionModel.select(0)
  }

  def getSearchField(name: String): TextField = {
    filterNameControlMapping(name).asInstanceOf[TextField]
  }

  def getSearchBox(name: String): ComboBox[String] = {
    filterNameControlMapping(name).asInstanceOf[ComboBox[String]]
  }

  private def updateMapping(name: String, control: Control): Control = {
    filterControlNameMapping.put(control, name)
    filterNameControlMapping.put(name, control)
    control
  }

  def filter() {
    val start = System.currentTimeMillis()
    var filtered:ObservableBuffer[FXBean[S]] = items

    controlFilterMap.keySet.foreach {
      case textField: TextField =>
        val filter = textField.getText
        if (filter.length > 0) {
          valueMap.put(filterControlNameMapping(textField), filter)
          filtered = filtered.filter(controlFilterMap(textField).asInstanceOf[FXBean[S] => Boolean])
        }
      case searchBox: ComboBox[String] =>
        val model = searchBox.getSelectionModel
        if (model.selectedIndexProperty().get() > 0) {
          val item = model.getSelectedItem
          valueMap.put(filterControlNameMapping(searchBox), item)
          filtered = filtered.filter(controlFilterMap(searchBox).asInstanceOf[FXBean[S] => Boolean])
          logger.debug(item)
        }
      case _ =>
    }

    filterResult.clear()
    filterResult.setAll(filtered)

    logger.debug("filtered (%d) in %d ms".format(filtered.size, System.currentTimeMillis() - start))
  }

  def reset() {
    controlFilterMap.keySet.foreach {
      case textField: TextField => textField.setText("")
      case searchBox: ComboBox[String] =>
        updateSearchBoxValues(searchBox, searchBox.getItems.get(0), controlFilterPropertyMap(searchBox))
      case _ =>
    }
    filter()
  }


  private def filterFunction(function: FilterValue, property: String, valueKey: String): (FXBean[S] => Boolean) = {
    if (function == FilterType.FilterContains)
      containsFunction(property, valueKey)
    else if (function == FilterType.FilterContainsIgnoreCase)
      containsLowerCaseFunction(property, valueKey)
    else if (function == FilterType.FilterEquals)
      equalsFunction(property, valueKey)
    else if (function == FilterType.FilterEqualsIgnoreCase)
      equalsLowerCaseFunction(property, valueKey)
    else
      b => false
  }

  private def containsFunction(property: String, valueKey: String): (FXBean[S] => Boolean) = {
    b => getFilterString(b, property).contains(valueMap(valueKey).toString)
  }

  private def containsLowerCaseFunction(property: String, valueKey: String): (FXBean[S] => Boolean) = {
    b => getFilterString(b, property).toLowerCase.contains(valueMap(valueKey).toString.toLowerCase)
  }

  private def equalsFunction(property: String, valueKey: String): (FXBean[S] => Boolean) = {
    b => getFilterString(b, property).equals(valueMap(valueKey))
  }

  private def equalsLowerCaseFunction(property: String, valueKey: String): (FXBean[S] => Boolean) = {
    b => getFilterString(b, property).toLowerCase.equals(valueMap(valueKey).toString.toLowerCase)
  }

  private def getFilterString(bean: FXBean[S], property: String): String = {
    val value = bean.getValue(property)
    value match {
      case d: java.util.Date => FXBean.defaultDateConverter.toString(d)
      case c: java.util.Calendar => FXBean.defaultDateConverter.toString(c.getTime)
      case c: javax.xml.datatype.XMLGregorianCalendar => FXBean.defaultDateConverter.toString(c.toGregorianCalendar.getTime)
      case v: Any => v.toString
      case _ => ""
    }
  }
}

object DataFilter {

  def apply[S <: AnyRef](items: ObjectProperty[ObservableBuffer[FXBean[S]]], pane:ObjectProperty[Pane]):DataFilter[S] = {
    new DataFilter[S](items, pane)

  }
}
