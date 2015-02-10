package com.sfxcode.sapphire.extension.table

object TableFilterType extends Enumeration {

  type FilterValue = Value
  val FilterEquals, FilterEqualsIgnoreCase, FilterContains, FilterContainsIgnoreCase = Value
}
