package com.sfxcode.sapphire.extension.demo.controller.master

import com.sfxcode.sapphire.core.Includes._
import com.sfxcode.sapphire.extension.controller.{BaseDetailController, BaseMasterController}
import com.sfxcode.sapphire.extension.demo.controller.MainWindowController
import com.sfxcode.sapphire.extension.demo.model.{Person, PersonDatabase}
import com.sfxcode.sapphire.extension.filter.DataTableFilter

import scala.reflect._

class PersonMasterController extends BaseMasterController {
  lazy val personDetailController = getController[PersonDetailController]()

  type R = Person

  def ct = classTag[R]

  def items = PersonDatabase.smallPersonTable

  override def didGainVisibilityFirstTime(): Unit = {
    super.didGainVisibilityFirstTime()
    detailController = Some(personDetailController)
  }

  override def initTable(tableFilter: DataTableFilter[R]): Unit = {
    super.initTable(tableFilter)
    tableFilter.hideColumn("metaData")
    tableFilter.addSearchField("nameFilter", "name").setPromptText("Name")

    tableFilter.hideColumn("tags", "friends", "about", "guid", "picture")

    tableFilter.addSearchField("addressFilter", "address").setPromptText("Address")
    tableFilter.addSearchBox("genderFilter", "gender", "male/female")
    tableFilter.addSearchBox("fruitFilter", "favoriteFruit", "all fruits")
  }

  override def navigateToDetailController(detailController: BaseDetailController): Unit = {
    parent.asInstanceOf[MainWindowController].workspaceManager.updatePaneContent(detailController)
  }

  def mainWindowController: MainWindowController = {
    parent.asInstanceOf[MainWindowController]
  }

}

