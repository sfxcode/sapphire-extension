package com.sfxcode.sapphire.extension.showcase.controller.table

import com.sfxcode.sapphire.`extension`.report.{FXBeanDataSource, PdfExporter}
import com.sfxcode.sapphire.core.value.{BeanConversions, FXBean}
import com.sfxcode.sapphire.extension.controller.DataTableController
import com.sfxcode.sapphire.extension.showcase.model.{Person, PersonDatabase}
import com.sfxcode.sapphire.extension.filter.DataTableFilter
import com.sfxcode.sapphire.extension.showcase.controller.BaseController
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import better.files.{File, Resource}
import javafx.scene.control.SelectionMode

import sys.process._
import scala.language.postfixOps
import scala.reflect._

class PersonTableController extends DataTableController with BaseController with BeanConversions {

  type R = Person

  def ct: ClassTag[Person] = classTag[R]

  def items: ObservableList[FXBean[Person]] = PersonDatabase.bigPersonTable

  override def initTable(tableFilter: DataTableFilter[R]): Unit = {
    super.initTable(tableFilter)
    table.getSelectionModel.setSelectionMode(SelectionMode.MULTIPLE)

    tableFilter.addSearchField("nameFilter", "name").setPromptText("Name")

    tableFilter.hideColumn("tags", "friends", "about", "guid", "picture")

    // #DataFilter
    tableFilter.addSearchField("addressFilter", "address").setPromptText("Address")
    tableFilter.addSearchBox("genderFilter", "gender", "male/female")
    tableFilter.addSearchBox("fruitFilter", "favoriteFruit", "all fruits")
    // #DataFilter
  }

  def actionExport(event: ActionEvent): Unit =
    if (tableFilter.selectedItems.nonEmpty) {
      val exporter = PdfExporter(Resource.getUrl("report/personTable.jrxml"))
      val exportResult = exporter.exportReport(
        File.newTemporaryFile(),
        Map("text" -> "All Persons"),
        FXBeanDataSource.fromObservableList[Person](tableFilter.selectedItems)
      )

      if (exportResult.completed) {
        if (System.getProperty("os.name").contains("Mac"))
          "open %s".format(exportResult.exportFile.pathAsString) !
        else
          "xdg-open %s".format(exportResult.exportFile.pathAsString) !
      }

    }
    else {
      logger.warn("empty selection")
    }

}
