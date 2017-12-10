package com.sfxcode.sapphire.extension.demo.controller.table

import com.sfxcode.sapphire.extension.demo.model.{ Person, PersonDatabase }
import com.sfxcode.sapphire.extension.filter.DataTableFilter
import com.sfxcode.sapphire.core.Includes._

import scala.reflect._

class PersonTableController extends AbstractTableViewController {

  type R = Person

  def ct = classTag[R]

  def items = PersonDatabase.bigPersonTable

  override def initTable(tableFilter: DataTableFilter[R]): Unit = {
    super.initTable(tableFilter)
    tableFilter.hideColumn("tags", "friends", "about", "guid", "picture")

    tableFilter.addSearchField("addressFilter", "address").setPromptText("Address")
    tableFilter.addSearchBox("genderFilter", "gender", "male/female")
    tableFilter.addSearchBox("fruitFilter", "favoriteFruit", "all fruits")
  }

}

