package com.sfxcode.sapphire.extension.showcase.controller.table

import com.sfxcode.sapphire.core.value.{ BeanConversions, FXBean }
import com.sfxcode.sapphire.extension.showcase.model.{ Person, PersonDatabase }
import com.sfxcode.sapphire.extension.filter.DataTableFilter
import javafx.collections.ObservableList

import scala.reflect._

class PersonTableController extends AbstractTableViewController with BeanConversions {

  type R = Person

  def ct: ClassTag[Person] = classTag[R]

  def items: ObservableList[FXBean[Person]] = PersonDatabase.bigPersonTable

  override def initTable(tableFilter: DataTableFilter[R]): Unit = {
    super.initTable(tableFilter)
    tableFilter.hideColumn("tags", "friends", "about", "guid", "picture")

    // #DataFilter
    tableFilter.addSearchField("addressFilter", "address").setPromptText("Address")
    tableFilter.addSearchBox("genderFilter", "gender", "male/female")
    tableFilter.addSearchBox("fruitFilter", "favoriteFruit", "all fruits")
    // #DataFilter
  }

}

