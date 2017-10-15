package com.sfxcode.sapphire.extension.control

import javafx.scene.control.{ Control, Label, Skin }
import javafx.scene.layout.Pane

import com.sfxcode.sapphire.core.Includes._
import com.sfxcode.sapphire.core.value.FXBean
import com.sfxcode.sapphire.extension.filter.DataListFilter
import com.sfxcode.sapphire.extension.skin.DataListViewSkin

import scalafx.beans.property._
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.ListView

class DataListView[S <: AnyRef] extends Control {

  lazy val css = getClass.getResource("datalistview.css").toExternalForm

  getStyleClass.add("data-list-view")

  val items = ObjectProperty[ObservableBuffer[FXBean[S]]](this, "listViewItems", ObservableBuffer[FXBean[S]]())

  val listView = new ListView[FXBean[S]]()

  val showHeader = BooleanProperty(true)
  val header = ObjectProperty[Pane](this, "listViewHeader")

  val showFooter = BooleanProperty(false)
  val footer = ObjectProperty[Pane](this, "listViewFooter")

  val footerLabel = ObjectProperty[Label](this, "listViewFooterLabel")
  val footerTextProperty = StringProperty("%s of %s items")

  val cellProperty = StringProperty("${_self.toString()}")

  val sortProperty = StringProperty("")
  val shouldSortProperty = BooleanProperty(true)

  val filterPromptProperty = StringProperty("type to filter")
  val filter = ObjectProperty(new DataListFilter[S](this))

  protected override def createDefaultSkin: Skin[DataListView[S]] = {
    new DataListViewSkin[S](this)
  }

  override def getUserAgentStylesheet: String = css

  def setItems(values: Iterable[S]): Unit = {
    items.set(sortedItems(values))
  }

  def remove(bean: FXBean[S]) {
    items.value.remove(bean)
  }

  def add(bean: FXBean[S]): Unit = {
    items.value.remove(bean)
  }

  def getItems: ObservableBuffer[FXBean[S]] = items.value

  footer.onChange((_, _, _) => {
    if (showFooter.value)
      footerLabel.value.setText(footerTextProperty.value.format(filter.value.filterResult.size, filter.value.itemValues.size))
    filter.value.filterResult.onChange({
      if (showFooter.value)
        footerLabel.value.setText(footerTextProperty.value.format(filter.value.filterResult.size, filter.value.itemValues.size))
    })
  })

  private def sortedItems(values: ObservableBuffer[FXBean[S]]): ObservableBuffer[FXBean[S]] = {
    var result = values

    if (shouldSortProperty.value) {
      val sortKey = {
        if (sortProperty.value == null || sortProperty.value.isEmpty)
          cellProperty.value
        else
          sortProperty.value
      }
      result = values.sortBy(f => "" + f.getValue(sortKey))
    }
    result
  }

}
