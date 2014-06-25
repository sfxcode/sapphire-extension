package com.sfxcode.sapphire.control.table

object TableFilterType extends Enumeration {

  type FilterValue = Value
  val FilterEquals, FilterEqualsIgnoreCase, FilterContains, FilterContainsIgnoreCase = Value
}
