package com.sfxcode.sapphire.extension.control

import javafx.scene.layout.Pane
import com.sfxcode.sapphire.core.value.{BeanConversions, FXBean}
import com.sfxcode.sapphire.extension.filter.DataListFilter
import com.sfxcode.sapphire.extension.skin.DataListViewSkin
import javafx.beans.property._
import javafx.collections.{FXCollections, ObservableList}
import javafx.scene.control.{Control, Label, ListView, Skin}

import scala.collection.JavaConverters._

class DataListView[S <: AnyRef] extends Control with BeanConversions {

  lazy val css: String = getClass.getResource("datalistview.css").toExternalForm

  getStyleClass.add("data-list-view")

  val items = new SimpleObjectProperty[ObservableList[FXBean[S]]](this, "listViewItems", FXCollections.observableArrayList[FXBean[S]]())

  val listView = new ListView[FXBean[S]]()

  val showHeader = new SimpleBooleanProperty(true)
  val header = new SimpleObjectProperty[Pane](this, "listViewHeader")

  val showFooter = new SimpleBooleanProperty(false)
  val footer = new SimpleObjectProperty[Pane](this, "listViewFooter")

  val footerLabel = new SimpleObjectProperty[Label](this, "listViewFooterLabel")
  val footerTextProperty = new SimpleStringProperty("%s of %s items")

  val cellProperty = new SimpleStringProperty("${_self.toString()}")

  val sortProperty = new SimpleStringProperty("")
  val shouldSortProperty = new SimpleBooleanProperty(true)

  val filterPromptProperty = new SimpleStringProperty("type to filter")
  val filter = new SimpleObjectProperty(new DataListFilter[S](this))

  protected override def createDefaultSkin: Skin[DataListView[S]] = {
    new DataListViewSkin[S](this)
  }

  override def getUserAgentStylesheet: String = css

  def setItems(values: Iterable[S]): Unit = {
    items.set(sortedItems(values))
  }

  def remove(bean: FXBean[S]) {
    items.get.remove(bean)
  }

  def add(bean: FXBean[S]): Unit = {
    items.get.add(bean)
  }

  def getItems: ObservableList[FXBean[S]] = items.get

  footer.addListener((_, _, _) => {
    if (showFooter.get)
      footerLabel.get.setText(footerTextProperty.get.format(filter.get.filterResult.size, filter.get.itemValues.size))
    filter.get.filterResult.addListener(_ => {
      if (showFooter.get)
        footerLabel.get.setText(footerTextProperty.get.format(filter.get.filterResult.size, filter.get.itemValues.size))
    })
  })

  private def sortedItems(values: ObservableList[FXBean[S]]): ObservableList[FXBean[S]] = {
    var result = values

    if (shouldSortProperty.get) {
      val sortKey = {
        if (sortProperty.get == null || sortProperty.get.isEmpty)
          cellProperty.get
        else
          sortProperty.get
      }
      result = FXCollections.observableArrayList(values.asScala.sortBy(f => "" + f.getValue(sortKey)).asJava)
    }
    result
  }

}
